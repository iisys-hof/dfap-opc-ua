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
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonList;

public class Lookup {

    public static CompletableFuture<BrowsePathResult> translate(
            final OpcUaClient client,
            final NodeId startingNode,
            final String... path) {

        Objects.requireNonNull(startingNode);
        Objects.requireNonNull(path);

        // convert to elements

        final RelativePathElement[] elements = new RelativePathElement[path.length];
        for (int i = 0; i < path.length; i++) {
            elements[i] = new RelativePathElement(
                    Identifiers.HierarchicalReferences,
                    false, true,
                    QualifiedName.parse(path[i]));
        }

        // translate

        final BrowsePath request = new BrowsePath(startingNode, new RelativePath(elements));
        return client.translateBrowsePaths(singletonList(request)).thenApply(response -> response.getResults()[0]);
    }

    public static void main(final String[] args) throws Exception {

        Connect.connect()

                .thenCompose(client -> {

                    return translate(client, Identifiers.ObjectsFolder, "MyBigNodeManager","DataItem_0072")

                            .thenAccept(Lookup::dumpResult)
                            .thenCompose(c -> client.disconnect());

                }) // .thenCompose

                .get();

    }

    public static void dumpResult(final BrowsePathResult result) {
        if (result.getStatusCode().isGood()) {

            for (final BrowsePathTarget target : result.getTargets()) {
                System.out.println(target.getTargetId().local().get());
            }

        } else {

            System.out.println(result.getStatusCode());

        }
    }

}
