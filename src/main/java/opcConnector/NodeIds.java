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

package opcConnector; import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class NodeIds {

    private NodeIds() {
    }

    private static final Map<NodeId, String> IDS;

    static {
        final LinkedHashMap<NodeId, String> ids = new LinkedHashMap<>();

        for (final Field field : Identifiers.class.getDeclaredFields()) {

            try {
                final Object value = field.get(null);
                if (value instanceof NodeId) {
                    ids.put((NodeId) value, field.getName());
                }
            } catch (final Exception e) {
                continue;
            }
        }

        IDS = Collections.unmodifiableMap(ids);
    }

    public static Optional<String> lookup(final NodeId id) {
        return Optional.ofNullable(IDS.get(id));
    }

}
