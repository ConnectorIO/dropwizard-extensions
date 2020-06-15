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
