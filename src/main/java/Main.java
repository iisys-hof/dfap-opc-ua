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

import opcConnector.OPCConnector;
import opcConnector.OPCValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.*;

public class Main {

    public static void main (String[] args) {


        OPCConnector opc = new OPCConnector();
        opc.connect();
        opc.browse(new NodeId(2, "MyLevel.Alarm/0:EnabledState"));


        System.out.println(opc.read(new NodeId(6, "DataItem_0684")));
        opc.disconnect();
    }
}