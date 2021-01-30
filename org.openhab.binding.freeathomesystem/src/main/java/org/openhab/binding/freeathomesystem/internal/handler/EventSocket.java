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

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EventSocket} is responsible for handling socket events, which are
 * sent from the free@home bridge.
 *
 * @author Andras Uhrin - Initial contribution
 *
 */

public class EventSocket extends WebSocketAdapter {

    private final Logger logger = LoggerFactory.getLogger(EventSocket.class);

    private FreeAtHomeBridgeHandler freeAtHomeBridge = null;

    private CountDownLatch closureLatch = new CountDownLatch(1);

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        logger.debug("Socket Connected: {}", sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        logger.debug("Received TEXT message: {}", message);
        System.out.println("Received TEXT message: " + message);

        if (message.toLowerCase(Locale.US).contains("bye")) {
            getSession().close(StatusCode.NORMAL, "Thanks");
        } else {
            freeAtHomeBridge.processSocketEvent(message);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        logger.debug("Socket Closed: [ {} ] {}", statusCode, reason);
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        logger.debug("Socket Error: {}", cause.getLocalizedMessage());
        closureLatch.countDown();
    }

    public void awaitEndCommunication() throws InterruptedException {
        logger.debug("Awaiting ending the communication from remote or error");
        closureLatch.await();
    }

    public CountDownLatch getLatch() {
        return closureLatch;
    }

    public void setBridge(FreeAtHomeBridgeHandler bridge) {
        freeAtHomeBridge = bridge;
    }
}
