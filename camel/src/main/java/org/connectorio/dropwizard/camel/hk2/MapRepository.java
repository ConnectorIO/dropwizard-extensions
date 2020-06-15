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
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.camel.spi.BeanRepository;

public class MapRepository implements BeanRepository {

    private final Map<String, Object> beans;

    public MapRepository(Map<String, Object> beans) {
        this.beans = beans;
    }

    @Override
    public Object lookupByName(String name) {
        return beans.get(name);
    }

    @Override
    public <T> T lookupByNameAndType(String name, Class<T> type) {
        Object value = beans.get(name);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    @Override
    public <T> Map<String, T> findByTypeWithName(Class<T> type) {
        return beans.entrySet().stream().filter(e -> type.isInstance(e.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, e -> type.cast(e.getValue()), (e1, e2) -> e1));
    }

    @Override
    public <T> Set<T> findByType(Class<T> type) {
        return beans.values().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toSet());
    }

}
