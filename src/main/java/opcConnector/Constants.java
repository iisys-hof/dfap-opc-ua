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

package opcConnector;

public final class Constants {
    private Constants() {
    }
    // opc.tcp://Kokytos.hof-university.de:53530/OPCUA/SimulationServer
    // public static final String HOST = "apollo.muc.redhat.com";

    public static final String HOST = "your-server";
    public static final int PORT = 53530;

    // public static final String HOST = "[fe80::2c82:aeff:fe0b:ac2%enp0s31f6]";
    // public static final String HOST = System.getProperty("host", "localhost");
    // public static final int PORT = Integer.getInteger("port", 4840);

	//ToDo: read keys from config file
	
    public static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            
            "-----END RSA PRIVATE KEY-----";
    public static final String PUPLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3trIaKu6oMNmdsKvvpJD/9P1R\n" +
            "Q8ln88IMA7m1R8DQ5We5k7T6OoIiQv987a8qg8UxfZdIhBwxrFC7VGROsaMEXj3E\n" +
            "ELnf6eH+rdXkxhunQ11W+m7F/Cpex4lSadbQ3DQUgfXohJio120jczJgYmgb4fwk\n" +
            "QIfXrclPNFgFzp16XwIDAQAB\n" +
            "-----END PUBLIC KEY-----";
}
