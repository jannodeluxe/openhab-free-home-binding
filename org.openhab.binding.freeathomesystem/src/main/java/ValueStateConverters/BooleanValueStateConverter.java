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

package ValueStateConverters;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;

/**
 * The {@link DecimalValueStateConverter} is a value converter for boolean values
 *
 * @author Andras Uhrin - Initial contribution
 *
 */

public class BooleanValueStateConverter implements ValueStateConverter {

    @Override
    public State convertToState(String value) {

        if (value.equals("1")) {
            return OnOffType.ON;
        } else {
            return OnOffType.OFF;
        }
    }
}
