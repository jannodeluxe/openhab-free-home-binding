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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link FreeAtHomeSysApDeviceList} is responsible for device lists from free@home bridge.
 *
 * @author Andras Uhrin - Initial contribution
 *
 */

public class FreeAtHomeSysApDeviceList implements FreeAtHomeDeviceList {

    private final Logger logger = LoggerFactory.getLogger(FreeAtHomeSysApDeviceList.class);

    private @Nullable HttpClient httpClient = null;

    private @Nullable String componentListString;

    private @Nullable List<String> listOfComponentId;

    private String sysApUID;

    private String baseUrl;

    int numberOfComponents;

    public FreeAtHomeSysApDeviceList(HttpClient client, String forIpAddress, String forSysAP) {
        httpClient = client;
        baseUrl = "http://" + forIpAddress + "/fhapi/v1/api/rest";
        sysApUID = forSysAP;

        // Create list of component IDs
        listOfComponentId = new ArrayList<String>();
    }

    @Override
    public boolean buildComponentList() {

        listOfComponentId.clear();

        String url = baseUrl + "/devicelist";

        // Perform a simple GET and wait for the response.
        try {
            Request req = httpClient.newRequest(url);
            ContentResponse response = req.send();

            // Get component List
            componentListString = new String(response.getContent());

            JsonParser parser = new JsonParser();

            JsonElement jsonTree = parser.parse(this.componentListString);

            // check the output
            if (jsonTree.isJsonObject()) {
                JsonObject jsonObject = jsonTree.getAsJsonObject();

                // Get the main object
                JsonElement listOfComponents = jsonObject.get(sysApUID);

                if (null != listOfComponents) {
                    JsonArray array = listOfComponents.getAsJsonArray();

                    this.numberOfComponents = array.size();

                    for (int i = 0; i < array.size(); i++) {
                        JsonElement basicElement = array.get(i);

                        listOfComponentId.add(basicElement.getAsString());
                    }
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Error to build up the Component list [ {} ]", e.getMessage());
        }

        // Scan finished
        return true;
    }

    @Override
    public FreeAtHomeDeviceDescription getDeviceDescription(String id) {
        FreeAtHomeDeviceDescription device = null;

        String url = baseUrl + "/device/" + sysApUID + "/" + id;
        try {

            Request req = httpClient.newRequest(url);
            ContentResponse response;
            try {
                response = req.send();

                // Get component List
                String deviceString = new String(response.getContent());

                JsonParser parser = new JsonParser();
                JsonElement jsonTree = parser.parse(deviceString);

                // check the output
                if (null != jsonTree) {
                    if (jsonTree.isJsonObject()) {
                        JsonObject jsonObject = jsonTree.getAsJsonObject();

                        jsonObject = jsonObject.getAsJsonObject(sysApUID);
                        jsonObject = jsonObject.getAsJsonObject("devices");

                        device = new FreeAtHomeDeviceDescription(jsonObject, id);
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                logger.info("No communication possible to get device list - Communication interrupt [ {} ]",
                        e.getMessage());
            } catch (TimeoutException e) {
                // TODO Auto-generated catch block
                logger.info("No communication possible to get device list - Communication timeout [ {} ]",
                        e.getMessage());
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                logger.info("No communication possible to get device list - exception [ {} ]", e.getMessage());
            }
        } catch (NullPointerException e) {
            logger.info("Invalid Json file [ {} ]", e.getMessage());
        }

        return device;
    }

    public String getDeviceIdByIndex(int index) {
        return listOfComponentId.get(index);
    }

    public int getNumberOfDevices() {
        return this.numberOfComponents;
    }
}
