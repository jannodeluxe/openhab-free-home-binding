/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.openhab.binding.freeathomesystem.internal.handler;

import java.util.Map;

import org.openhab.binding.freeathomesystem.internal.FreeAtHomeSystemBindingConstants;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ValueStateConverters.BooleanValueStateConverter;

/**
 * The {@link FreeAtHomeSwitchHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Andras Uhrin - Initial contribution
 *
 */

public class FreeAtHomeSwitchHandler_22 extends FreeAtHomeSystemBaseHandler {

    private String deviceID;
    private String deviceChannel1;
    private String deviceOdp1;
    private String deviceIdp1;
    private String deviceChannel2;
    private String deviceOdp2;
    private String deviceIdp2;

    private FreeAtHomeBridgeHandler freeAtHomeBridge = null;

    private final Logger logger = LoggerFactory.getLogger(FreeAtHomeSwitchHandler_22.class);

    public FreeAtHomeSwitchHandler_22(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        Map<String, String> properties = getThing().getProperties();

        Bridge bridge = this.getBridge();
        deviceID = properties.get("deviceId");

        String deviceInterface = properties.get("interface");

        if (null != bridge) {
            ThingHandler handler = bridge.getHandler();

            if (handler instanceof FreeAtHomeBridgeHandler) {
                freeAtHomeBridge = (FreeAtHomeBridgeHandler) handler;

                // Initialize the communication device channel properties
                if (deviceInterface.equalsIgnoreCase(FreeAtHomeDeviceDescription.DEVICE_INTERFACE_WIRELESS_TYPE)) {
                    deviceChannel1 = "ch0006";
                    deviceOdp1 = "odp0000";
                    deviceIdp1 = "idp0000";
                    deviceChannel2 = "ch0007";
                    deviceOdp2 = "odp0000";
                    deviceIdp2 = "idp0000";
                } else if (deviceInterface
                        .equalsIgnoreCase(FreeAtHomeDeviceDescription.DEVICE_INTERFACE_VIRTUAL_TYPE)) {
                    deviceChannel1 = "ch0000";
                    deviceOdp1 = "odp0000";
                    deviceIdp1 = "idp0000";
                    // not complete
                }

                logger.debug("Initialize switch 2/2 - {}", deviceID);

                // Channel 1: Get initial state of the switch directly from the free@home switch
                String valueString = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel1, deviceOdp1);

                int value;

                try {
                    value = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    value = 0;
                }

                // set the initial state
                if (1 == value) {
                    updateState(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_1_ID, OnOffType.ON);
                } else {
                    updateState(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_1_ID, OnOffType.OFF);
                }

                // Channel 2: Get initial state of the switch directly from the free@home switch
                valueString = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel2, deviceOdp2);

                try {
                    value = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    value = 0;
                }

                // set the initial state
                if (1 == value) {
                    updateState(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_2_ID, OnOffType.ON);
                } else {
                    updateState(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_2_ID, OnOffType.OFF);
                }

                // Register device and specific channel for event based state updated
                if (null != freeAtHomeBridge.channelUpdateHandler) {
                    freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel1, deviceOdp1, this,
                            new ChannelUID(this.getThing().getUID(),
                                    FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_1_ID),
                            new BooleanValueStateConverter());

                    freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel2, deviceOdp2, this,
                            new ChannelUID(this.getThing().getUID(),
                                    FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_2_ID),
                            new BooleanValueStateConverter());

                    logger.debug("Device - online: {}", deviceID);

                    updateStatus(ThingStatus.ONLINE);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                            "No online updates are received");
                }

            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR);

                logger.debug("Incorrect bridge class: {}", deviceID);
            }
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "No bridge available");

            logger.debug("No bridge for device: {}", deviceID);
        }
    }

    @Override
    public void dispose() {
        // Unegister device and specific channel for event based state updated
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel1, deviceOdp1);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel2, deviceOdp2);

        logger.debug("Device removed {}", deviceID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        updateStatus(ThingStatus.ONLINE);

        if (command instanceof RefreshType) {
            if (channelUID.getId().equalsIgnoreCase(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_1_ID)) {
                String valueString = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel1, deviceOdp1);

                int value;

                try {
                    value = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    value = 0;
                }

                if (1 == value) {
                    updateState(channelUID, OnOffType.ON);
                } else {
                    updateState(channelUID, OnOffType.OFF);
                }
            }
            if (channelUID.getId().equalsIgnoreCase(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_2_ID)) {
                String valueString = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel2, deviceOdp2);

                int value;

                try {
                    value = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    value = 0;
                }

                if (1 == value) {
                    updateState(channelUID, OnOffType.ON);
                } else {
                    updateState(channelUID, OnOffType.OFF);
                }
            }
        }

        if (command instanceof OnOffType) {
            OnOffType locCommand = (OnOffType) command;

            if (channelUID.getId().equalsIgnoreCase(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_1_ID)) {
                if (locCommand.equals(OnOffType.ON)) {
                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel1, deviceIdp1, "1");
                    updateState(channelUID, OnOffType.ON);
                }

                if (locCommand.equals(OnOffType.OFF)) {
                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel1, deviceIdp1, "0");
                    updateState(channelUID, OnOffType.OFF);
                }
            }

            if (channelUID.getId().equalsIgnoreCase(FreeAtHomeSystemBindingConstants.SWITCH_CHANNEL_2_ID)) {
                if (locCommand.equals(OnOffType.ON)) {
                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel2, deviceIdp2, "1");
                    updateState(channelUID, OnOffType.ON);
                }

                if (locCommand.equals(OnOffType.OFF)) {
                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel2, deviceIdp2, "0");
                    updateState(channelUID, OnOffType.OFF);
                }
            }

        }

        logger.debug("Handle command switch {} - at channel {} - full command {}", deviceID, channelUID.getAsString(),
                command.toFullString());
    }
}
