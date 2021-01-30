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
package org.openhab.binding.freeathomesystem.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.freeathomesystem.internal.handler.FreeAtHomeBridgeHandler;
import org.openhab.binding.freeathomesystem.internal.handler.FreeAtHomeDeviceDescription;
import org.openhab.binding.freeathomesystem.internal.handler.FreeAtHomeDeviceList;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;

/**
 * @author andras
 *
 */

@Component(service = DiscoveryService.class)
public class FreeAtHomeSystemDiscoveryService extends AbstractDiscoveryService {

    // public AbstractDiscoveryService(@Nullable Set<ThingTypeUID> supportedThingTypes, int timeout,
    // boolean backgroundDiscoveryEnabledByDefault) throws IllegalArgumentException {

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
        }
    };

    public FreeAtHomeSystemDiscoveryService(int timeout) {
        super(FreeAtHomeSystemBindingConstants.SUPPORTED_THING_TYPES_UIDS, 90, false);
    }

    public FreeAtHomeSystemDiscoveryService() {
        super(FreeAtHomeSystemBindingConstants.SUPPORTED_THING_TYPES_UIDS, 90, false);
    }

    @Override
    protected void startScan() {

        this.removeOlderResults(getTimestampOfLastScan());

        scheduler.execute(runnable);

        FreeAtHomeBridgeHandler bridge = FreeAtHomeBridgeHandler.freeAtHomeSystemHandler;

        if (null != bridge) {
            ThingUID bridgeUID = bridge.getThing().getUID();

            FreeAtHomeDeviceList deviceList = bridge.getDeviceDeviceList();

            for (int i = 0; i < deviceList.getNumberOfDevices(); i++) {

                FreeAtHomeDeviceDescription device = deviceList.getDeviceDescription(deviceList.getDeviceIdByIndex(i));

                if (null != device) {

                    switch (device.thingsTypeOfDevice) {

                        case FreeAtHomeSystemBindingConstants.SWITCH_TYPE_ID: {
                            ThingUID uid = new ThingUID(FreeAtHomeSystemBindingConstants.SWITCH_TYPE_UID, bridgeUID,
                                    device.deviceId);
                            Map<String, Object> properties = new HashMap<>(1);
                            properties.put("deviceId", device.deviceId);
                            properties.put("numberOfSensorChannels", "1");
                            properties.put("numberOfActutorChannels", "1");

                            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(uid)
                                    .withLabel(device.deviceType + " - " + device.deviceLabel + " - " + device.deviceId)
                                    .withBridge(bridgeUID).withProperties(properties).build();

                            thingDiscovered(discoveryResult);
                            break;
                        }

                        case FreeAtHomeSystemBindingConstants.THERMOSTAT_TYPE_ID: {
                            ThingUID uid = new ThingUID(FreeAtHomeSystemBindingConstants.THERMOSTAT_TYPE_UID, bridgeUID,
                                    device.deviceId);
                            Map<String, Object> properties = new HashMap<>(1);
                            properties.put("deviceId", device.deviceId);
                            properties.put("numberOfSensorChannels", "1");
                            properties.put("numberOfActutorChannels", "1");

                            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(uid)
                                    .withLabel(device.deviceType + " - " + device.deviceLabel + " - " + device.deviceId)
                                    .withBridge(bridgeUID).withProperties(properties).build();
                            thingDiscovered(discoveryResult);

                            thingDiscovered(discoveryResult);
                            break;
                        }
                    }
                }
            }
        }
    }
}
