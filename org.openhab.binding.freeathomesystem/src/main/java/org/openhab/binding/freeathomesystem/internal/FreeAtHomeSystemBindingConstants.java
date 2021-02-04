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

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link FreeAtHomeSystemBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Andras Uhrin - Initial contribution
 */
@NonNullByDefault
public class FreeAtHomeSystemBindingConstants {

    private static final String BINDING_ID = "freeathomesystem";

    // List of all Thing Type UIDs
    public static final String BRIDGE_TYPE_ID = "bridge";
    public static final String SWITCH_TYPE_ID = "switch";
    public static final String SWITCH_2_1_TYPE_ID = "switch_2_1";
    public static final String SWITCH_2_2_TYPE_ID = "switch_2_2";

    public static final String SENSOR_TYPE_ID = "sensor";
    public static final String THERMOSTAT_TYPE_ID = "thermostat";
    public static final String WINDOWSENSOR_TYPE_ID = "windowsensor";

    // List of all Thing Type UIDs
    public static final ThingTypeUID BRIDGE_TYPE_UID = new ThingTypeUID(BINDING_ID, BRIDGE_TYPE_ID);
    public static final ThingTypeUID SWITCH_TYPE_UID = new ThingTypeUID(BINDING_ID, SWITCH_TYPE_ID);
    public static final ThingTypeUID SWITCH_2_1_TYPE_UID = new ThingTypeUID(BINDING_ID, SWITCH_2_1_TYPE_ID);
    public static final ThingTypeUID SWITCH_2_2_TYPE_UID = new ThingTypeUID(BINDING_ID, SWITCH_2_2_TYPE_ID);
    public static final ThingTypeUID THERMOSTAT_TYPE_UID = new ThingTypeUID(BINDING_ID, THERMOSTAT_TYPE_ID);
    public static final ThingTypeUID WINDOWSENSOR_TYPE_UID = new ThingTypeUID(BINDING_ID, WINDOWSENSOR_TYPE_ID);

    // sensor Channel ids
    public static final String SENSOR_1_CHANNEL_ID = "sensor_channel_1";
    public static final String SENSOR_2_CHANNEL_ID = "sensor_channel_2";

    // switch Channel ids
    public static final String SWITCH_CHANNEL_ID = "switch_channel";
    // multiple switch/sensor Channel ids
    public static final String SWITCH_CHANNEL_1_ID = "switch_channel_1";
    public static final String SWITCH_CHANNEL_2_ID = "switch_channel_2";

    // Thermostat Channel ids
    public static final String THERMOSTAT_CHANNEL_SETPOINTTEMP_ID = "thermostat_setpoint_temperature";
    public static final String THERMOSTAT_CHANNEL_MEASUREDTEMP_ID = "thermostat_measured_temperature";
    public static final String THERMOSTAT_CHANNEL_HEATDEMAND_ID = "thermostat_heating_demand";
    public static final String THERMOSTAT_CHANNEL_HEATINGACTIVE_ID = "thermostat_heating_active";
    public static final String THERMOSTAT_CHANNEL_STATE_ID = "thermostat_state";
    public static final String THERMOSTAT_CHANNEL_ONOFFWITCH_ID = "thermostat_onoff_switch";
    public static final String THERMOSTAT_CHANNEL_ECOSWITCH_ID = "thermostat_eco_switch";

    // window sensor Channel ids
    public static final String WINDOWSENSOR_CHANNEL_STATE_ID = "window_state_channel";
    public static final String WINDOWSENSOR_CHANNEL_POS_ID = "window_pos_channel";

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(SWITCH_TYPE_UID, SWITCH_2_2_TYPE_UID,
            BRIDGE_TYPE_UID, WINDOWSENSOR_TYPE_UID, THERMOSTAT_TYPE_UID);
}
