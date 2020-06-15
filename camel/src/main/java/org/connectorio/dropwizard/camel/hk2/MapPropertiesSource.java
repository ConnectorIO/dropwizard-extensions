/**
 * Copyright (C) 2020 Connectorio Sp. z o.o.
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
package org.connectorio.dropwizard.camel.hk2;

import java.util.Map;
import org.apache.camel.spi.PropertiesSource;

public class MapPropertiesSource implements PropertiesSource {

    private final Map<String, Object> properties;

    public MapPropertiesSource(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return "dropwizard";
    }

    @Override
    public String getProperty(String name) {
        Object value = properties.get(name);

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

}
