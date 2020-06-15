package org.connectorio.dropwizard.autobundle.graph;

public interface Node<T> {

  boolean dependsOn(Node<T> other);

  Class<?> getType();

  void link(DependencyGraph<T> tDependencyGraph);
}
