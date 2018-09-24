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

package opcConnector; import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Both;

public class Read {

    private static final NodeId NODE_TO_READ = new NodeId(0, 2261);

    public static CompletableFuture<DataValue> read(
            final OpcUaClient client,
            final NodeId nodeId) {

        return client.readValue(0, TimestampsToReturn.Both, nodeId);
    }

    public static CompletableFuture<List<DataValue>> read(
            final OpcUaClient client,
            final AttributeId attributeId,
            final NodeId... nodeIds) {


        return client
                .read(
                        0,
                        Both,
                        asList(nodeIds),
                        nCopies(nodeIds.length, attributeId.uid()));
    }

    public static void main(final String[] args) throws Exception {

        final OpcUaClient client = Connect.connect().get();


        final NodeId[] moreIds = new NodeId[] {
                Identifiers.Server_NamespaceArray,
                Identifiers.Server_Namespaces,
                Identifiers.Server_ServerStatus_BuildInfo_ManufacturerName,
                Identifiers.Server_ServerStatus_BuildInfo_ProductName,
                Identifiers.Server_ServerStatus_CurrentTime,
                new NodeId(6, "DataItem_0684")
        };

        // read values

        final CompletableFuture<List<DataValue>> future = read(
                client,
                AttributeId.Value,
                moreIds);

        final List<DataValue> values = future.get();
        Values.dumpValues(System.out, asList(moreIds), values);

        // read browse name

        final CompletableFuture<List<DataValue>> future2 = read(
                client,
                AttributeId.BrowseName,
                moreIds);

        final List<DataValue> values2 = future2.get();
        Values.dumpValues(System.out, asList(moreIds), values2);

        for (int i = 0; i< values.size(); i++) {
            if (values.get(i).getValue().getValue() != null)
                System.out.println("VALUE  " + values.get(i).getValue().getValue().getClass().getName());
            if (values.get(i).getValue().getValue() instanceof String[]) {
                String[] array = (String[]) values.get(i).getValue().getValue();
                System.out.println(Arrays.toString(array));
            }

        }

        // disconnect

        client.disconnect().get();
    }
}
