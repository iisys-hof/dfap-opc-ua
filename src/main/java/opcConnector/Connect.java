/*
 * Copyright 2018 Thomas Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opcConnector; import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;


public class Connect {

    private static OpcUaClientConfig buildConfiguration(final EndpointDescription[] endpoints) {

        final OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();

        try {

            FileInputStream is = new FileInputStream("keystore.jks");

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, "Delta86".toCharArray());

            String alias = "CAPS";

            Key key = keystore.getKey(alias, "Delta86".toCharArray());
            if (key instanceof PrivateKey) {
                // Get certificate of public key
                Certificate cert = keystore.getCertificate(alias);

                // Get public key
                PublicKey publicKey = cert.getPublicKey();

                // Return a key pair
                KeyPair k = new KeyPair(publicKey, (PrivateKey) key);


                cfg.setKeyPair(k);
                cfg.setCertificate((X509Certificate) cert);
            }

        } catch (Exception e) {
            System.out.println("Error");
        }
        cfg.setApplicationUri("");
        cfg.setApplicationName(new LocalizedText(""));
        cfg.setEndpoint(findBest(endpoints));


        return cfg.build();

    }

    public static EndpointDescription findBest(final EndpointDescription[] endpoints) {
        /*
         * We simply assume we have at least one and pick the first one. In a more
         * productive scenario you would actually evaluate things like ciphers and
         * security.
         */
        return endpoints[0];
    }

    // create client

    public static CompletableFuture<OpcUaClient> createClient() {
        final String endpoint = String.format("opc.tcp://%s:%s/OPCUA/SimulationServer", opcConnector.Constants.HOST, opcConnector.Constants.PORT); 

        return UaTcpStackClient
                .getEndpoints(endpoint) // look up endpoints from remote
                .thenApply(endpoints -> new OpcUaClient(buildConfiguration(endpoints)));
    }

    // connect

    public static CompletableFuture<OpcUaClient> connect() {
        return createClient()
                .thenCompose(OpcUaClient::connect) // trigger connect
                .thenApply(c -> (OpcUaClient) c); // cast result of connect from UaClient to OpcUaClient
    }

    // main entry point

    public static void main(final String[] args) throws InterruptedException, ExecutionException {

        final Semaphore s = new Semaphore(0);

        connect()
                .whenComplete((client, e) -> {
                    // called when the connect operation finished ... either way

                    if (e == null) {
                        System.out.println("Connected");
                    } else {
                        System.err.println("Failed to connect");
                        e.printStackTrace();
                    }
                })
                .thenCompose(OpcUaClient::disconnect)
                .thenRun(s::release); // wake up s.acquire() below

        System.out.println("Wait for completion");

        s.acquire(); // what could could wrong?

        System.out.println("Bye bye");
    }

    // synchronous way of doing things

    public static OpcUaClient createClientSync() throws InterruptedException, ExecutionException {
        final String endpoint = String.format("opc.tcp://%s:%s/OPCUA/SimulationServer", opcConnector.Constants.HOST, opcConnector.Constants.PORT);

        final EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(endpoint)
                .get();

        return new OpcUaClient(buildConfiguration(endpoints));
    }

    public static OpcUaClient connectSync() throws InterruptedException, ExecutionException {
        final OpcUaClient client = createClientSync();

        client.connect()
                .get();

        return client;
    }

}
