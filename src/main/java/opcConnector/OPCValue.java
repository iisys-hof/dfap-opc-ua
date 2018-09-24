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

public class OPCValue<T> {

    private NodeId identifier;
    private Class aClass;

    private T value;

    public OPCValue() {

    }



    public OPCValue(NodeId identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public OPCValue(NodeId identifier, Class aClass, T value) {
        this.identifier = identifier;
        this.aClass = aClass;
        this.value = value;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public NodeId getIdentifier() {
        return identifier;
    }

    public void setIdentifier(NodeId identifier) {
        this.identifier = identifier;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OPCValue{" +
                "identifier='" + identifier.toParseableString() + '\'' +
                ", aClass=" + aClass.getCanonicalName() +
                ", value=" + value +
                '}';
    }
}
