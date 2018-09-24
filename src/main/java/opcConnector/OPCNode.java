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

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import javax.xml.soap.Node;
import java.util.List;

public class OPCNode{

    private List<OPCNode> childNodes;
private String intend;
    private int namespaceIndex;
    private String identifier;

    private NodeId nodeId;

    private OPCValue value;

    public OPCNode(int namespaceIndex, String identifier) {
        this.namespaceIndex = namespaceIndex;
        this.identifier = identifier;
    }
    public OPCNode(String intend){
        this.intend = intend;
    }

    public NodeId getNodeId() {
        return nodeId;
    }

    public void setNodeId(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public List<OPCNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<OPCNode> childNodes) {
        this.childNodes = childNodes;
    }

    public int getNamespaceIndex() {
        return namespaceIndex;
    }

    public void setNamespaceIndex(int namespaceIndex) {
        this.namespaceIndex = namespaceIndex;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public OPCValue getValue() {
        return value;
    }

    public void setValue(OPCValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\n" + intend + "OPCNode{" +
                "nodeId=" + nodeId +
                "childNodes=" + childNodes +
                "Value=" + value +
                '}';
    }
}
