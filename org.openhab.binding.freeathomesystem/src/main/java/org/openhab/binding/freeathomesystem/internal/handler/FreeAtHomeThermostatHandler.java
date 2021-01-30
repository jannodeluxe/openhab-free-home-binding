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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
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
import ValueStateConverters.DecimalValueStateConverter;

/**
 * The {@link FreeAtHomeThermostatHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Andras Uhrin - Initial contribution
 *
 */

public class FreeAtHomeThermostatHandler extends FreeAtHomeSystemBaseHandler {

    private String deviceID;
    private String deviceChannel;
    private String measuredTempOdp;
    private String heatingDemandOdp;
    private String heatingActiveOdp;
    private String setpointTempOdp;
    private String statesOdp;
    private String setpointTempIdp;
    private String onoffSwitchIdp;
    private String ecoSwitchIdp;
    private String onoffIndicationOdp;
    private String ecoIndicationOdp;

    private final Logger logger = LoggerFactory.getLogger(FreeAtHomeThermostatHandler.class);

    private FreeAtHomeBridgeHandler freeAtHomeBridge = null;

    // private ScheduledFuture pollingJob = null;

    public FreeAtHomeThermostatHandler(Thing thing) {
        super(thing);
    }

    public void setupUpdateChannelsForEvents() {

        // Register device and specific channel for event based state updated
        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, measuredTempOdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_MEASUREDTEMP_ID),
                new DecimalValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, heatingDemandOdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_HEATDEMAND_ID),
                new DecimalValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, heatingActiveOdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_HEATINGACTIVE_ID),
                new DecimalValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, setpointTempOdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_SETPOINTTEMP_ID),
                new DecimalValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, setpointTempIdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_SETPOINTTEMP_ID),
                new DecimalValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, onoffSwitchIdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ONOFFWITCH_ID),
                new BooleanValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, ecoSwitchIdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ECOSWITCH_ID),
                new BooleanValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, onoffIndicationOdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ONOFFWITCH_ID),
                new BooleanValueStateConverter());

        freeAtHomeBridge.channelUpdateHandler.registerChannel(deviceID, deviceChannel, ecoIndicationOdp, this,
                new ChannelUID(this.getThing().getUID(),
                        FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ECOSWITCH_ID),
                new BooleanValueStateConverter());
    }

    public void setupInitialState() {
        // update measured temp
        String value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, measuredTempOdp);
        DecimalType dec = new DecimalType(value);

        updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_MEASUREDTEMP_ID, dec);

        value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, heatingDemandOdp);
        dec = new DecimalType(value);
        updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_HEATDEMAND_ID, dec);

        value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, heatingActiveOdp);
        dec = new DecimalType(value);
        updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_HEATINGACTIVE_ID, dec);

        // 54 out states: on/off; heating/cooling; eco/comfort; frost/not frost bitmask:
        // 0x01 - comfort mode
        // 0x02 - standby
        // 0x04 - eco mode
        // 0x08 - building protect
        // 0x10 - dew alarm
        // 0x20 - heat (set) / cool (unset)
        // 0x40 - no heating/cooling (set)
        // 0x80 - frost alarm
        value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, statesOdp);
        int intValue = Integer.decode(value);
        int result;

        result = intValue & 0x01;

        if (0x01 == result) {
            updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ONOFFWITCH_ID, OnOffType.ON);
        } else {
            updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ONOFFWITCH_ID, OnOffType.OFF);

        }

        result = intValue & 0x04;

        if (0x04 == result) {
            updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ECOSWITCH_ID, OnOffType.ON);
        } else {
            updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ECOSWITCH_ID, OnOffType.OFF);
        }

        // update setpoint temp
        value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, setpointTempOdp);
        dec = new DecimalType(value);
        updateState(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_SETPOINTTEMP_ID, dec);
    }

    @Override
    public void initialize() {
        Map<String, String> properties = getThing().getProperties();

        Bridge bridge = this.getBridge();
        deviceID = properties.get("deviceId");

        if (null != bridge) {
            ThingHandler handler = bridge.getHandler();

            if (handler instanceof FreeAtHomeBridgeHandler) {
                freeAtHomeBridge = (FreeAtHomeBridgeHandler) handler;

                deviceChannel = "ch0000";
                measuredTempOdp = "odp0007";
                heatingDemandOdp = "odp0009";
                heatingActiveOdp = "odp0008";
                setpointTempOdp = "odp0000";
                statesOdp = "odp0003";
                setpointTempIdp = "idp0009";
                onoffSwitchIdp = "idp0005";
                ecoSwitchIdp = "idp0004";
                onoffIndicationOdp = "odp0002";
                ecoIndicationOdp = "odp000d";

                setupInitialState();

                // Register device and specific channel for event based state updated
                if (null != freeAtHomeBridge.channelUpdateHandler) {
                    setupUpdateChannelsForEvents();

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

        // Unregister device and specific channel for event based state updated
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, measuredTempOdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, heatingDemandOdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, heatingActiveOdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, setpointTempOdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, setpointTempIdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, onoffSwitchIdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, ecoSwitchIdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, onoffIndicationOdp);
        freeAtHomeBridge.channelUpdateHandler.unregisterChannel(deviceID, deviceChannel, ecoIndicationOdp);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        Map<String, String> properties = getThing().getProperties();

        if (command instanceof RefreshType) {
            if (channelUID.getId()
                    .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_MEASUREDTEMP_ID)) {
                String value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, measuredTempOdp);
                DecimalType dec = new DecimalType(value);

                updateState(channelUID, dec);
            }

            if (channelUID.getId()
                    .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_SETPOINTTEMP_ID)) {
                String value = freeAtHomeBridge.getDatapoint(deviceID, deviceChannel, setpointTempOdp);
                DecimalType dec = new DecimalType(value);

                updateState("thermostat_setpoint_temperature", dec);
            }
        }

        if (command instanceof OnOffType) {
            OnOffType locCommand = (OnOffType) command;

            if (locCommand.equals(OnOffType.ON)) {
                if (channelUID.getId()
                        .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ONOFFWITCH_ID)) {
                    System.out.print("Thermostat Activation " + command.toFullString() + "\n");

                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel, onoffSwitchIdp, "1");
                    updateState(channelUID, OnOffType.ON);
                }

                if (channelUID.getId()
                        .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ECOSWITCH_ID)) {
                    System.out.print("Eco Activation " + command.toFullString() + "\n");

                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel, ecoSwitchIdp, "1");
                    updateState(channelUID, OnOffType.ON);
                }
            }

            if (locCommand.equals(OnOffType.OFF)) {
                if (channelUID.getId()
                        .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ONOFFWITCH_ID)) {
                    System.out.print("Thermostat Detivation " + command.toFullString() + "\n");

                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel, onoffSwitchIdp, "0");
                    updateState(channelUID, OnOffType.OFF);
                }

                if (channelUID.getId()
                        .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_ECOSWITCH_ID)) {
                    System.out.print("Eco Dectivation " + command.toFullString() + "\n");

                    freeAtHomeBridge.setDatapoint(deviceID, deviceChannel, ecoSwitchIdp, "0");
                    updateState(channelUID, OnOffType.OFF);
                }
            }
        }

        if (command instanceof QuantityType) {
            if (channelUID.getId()
                    .equalsIgnoreCase(FreeAtHomeSystemBindingConstants.THERMOSTAT_CHANNEL_SETPOINTTEMP_ID)) {
                // updateState(channelUID, OnOffType.ON);
                QuantityType value = new QuantityType(((QuantityType) command).toString());
                double v = value.doubleValue();

                String valueString = new String(String.valueOf(v));

                freeAtHomeBridge.setDatapoint(deviceID, deviceChannel, setpointTempIdp, valueString);

                updateState(channelUID, value);

                System.out.print("Set temp\t" + ((QuantityType) command).floatValue() + " " + valueString + "\n");
            }
        }

        System.out.print("Handle command switch " + deviceID + " " + channelUID.getAsString() + " "
                + command.toFullString() + " \n");
    }
}
