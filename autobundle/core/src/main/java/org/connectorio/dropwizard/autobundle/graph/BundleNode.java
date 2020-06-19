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
package org.connectorio.dropwizard.autobundle.graph;

import java.util.LinkedList;
import java.util.Objects;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleNode<T> implements Node<T> {

  private final Logger logger = LoggerFactory.getLogger(BundleNode.class);
  private final LinkedList<Node<T>> dependencies = new LinkedList<>();

  private final AutomaticBundle<T> bundle;

  public BundleNode(AutomaticBundle<T> bundle) {
    this.bundle = bundle;
    for (Class<?> type : bundle.getDependencies()) {
      dependencies.add(new ClassNode<>(type));
    }
  }

  @Override
  public Class<?> getType() {
    return bundle.getClass();
  }

  @Override
  public void link(DependencyGraph<T> graph) {
    for (int index = 0, limit = dependencies.size(); index < limit; index++) {
      Node<T> node = dependencies.get(index);
      if (graph.containsKey(node.getType())) {
        BundleNode<T> linked = graph.get(node.getType());
        logger.debug("{} add dependency to {}", this, linked);
        dependencies.set(index, linked);
      } else {
        throw new IllegalStateException("Unresolved graph dependency to " + node.getType());
      }
    }
  }

  public boolean dependsOn(Node<T> other) {
    if (equals(other)) {
      return true;
    }
    for (Node<T> dependency : dependencies) {
      if (dependency.dependsOn(other)) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    return "Bundle Node [" + bundle.getClass().getSimpleName() + "]";
  }

  public AutomaticBundle<T> get() {
    return bundle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BundleNode)) {
      return false;
    }
    BundleNode<?> that = (BundleNode<?>) o;
    return Objects.equals(getType(), that.getType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getType());
  }
}
