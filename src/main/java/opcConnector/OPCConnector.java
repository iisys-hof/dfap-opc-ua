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

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Arrays.asList;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class OPCConnector {

    private OpcUaClient client;
    public OPCConnector() {

    }

    public void connect() {
        try {
            this.client = Connect.connect().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        this.client.disconnect();
    }

    public void browse(NodeId nodeId) {
        try {
            this.browse(client, nodeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void browse() {
       this.browse(Identifiers.RootFolder);
    }

    private void browse(OpcUaClient client, NodeId browseRoot) throws Exception{
        OPCNode n = new OPCNode("");
        browse(client, browseRoot, "", n);
        System.out.println("d" + n);
    }
    private void browse(final OpcUaClient client, final NodeId browseRoot, final String indent, OPCNode node) throws Exception {

        final BrowseDescription browse = new BrowseDescription(
                browseRoot,
                BrowseDirection.Forward,
                Identifiers.References,
                true,
                uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
                uint(BrowseResultMask.All.getValue()));

        BrowseResult browseResult = client.browse(browse).get();


        do {
            node.setNodeId(browseRoot);



            node.setChildNodes(new ArrayList<>());
            for (final ReferenceDescription ref : browseResult.getReferences()) {
                dumpRef(indent, ref);
                final NodeId childId = ref.getNodeId().local().orElse(null);
                if (childId != null) {
                    node.getChildNodes().add(new OPCNode(indent + "  "));
                    browse(client, childId, indent + "  ", node.getChildNodes().get(node.getChildNodes().size() - 1));
                }
            }

            if (browseResult.getContinuationPoint().isNotNull()) {
                System.out.println("BROWSE NEXT");
                browseResult = client.browseNext(true, browseResult.getContinuationPoint()).get();
            } else {
                browseResult = null;
            }

        } while (browseResult != null);
    }

    private static void dumpRef(final String indent, final ReferenceDescription ref) {
        System.out.format("%-60s %-15s %-200s %-90s  %n",
                indent + ref.getBrowseName().getName(),
                ref.getNodeClass().toString(),
                ref.getNodeId().local().map(NodeId::toParseableString).orElse(""),
                ref.getNodeId().getType().getValue());


    }

    public List<OPCValue> read(NodeId... moreIds ) {
        if (moreIds.length == 0 )
            return null;

        final CompletableFuture<List<DataValue>> future = Read.read(client, AttributeId.Value, moreIds);

        final List<DataValue> values;
        try {
            values = future.get();
            //Values.dumpValues(System.out, asList(moreIds), values);
            List<OPCValue> opcValues =  Values.getValues(asList(moreIds), values);
            return opcValues;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }



}
