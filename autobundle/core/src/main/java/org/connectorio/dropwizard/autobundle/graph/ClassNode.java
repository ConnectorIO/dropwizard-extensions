package org.connectorio.dropwizard.autobundle.graph;

public class ClassNode<T> implements Node<T> {

  private final Class<?> clazz;

  public ClassNode(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public boolean dependsOn(Node<T> other) {
    return getType().equals(other.getType());
  }

  @Override
  public Class<?> getType() {
    return clazz;
  }

  @Override
  public void link(DependencyGraph<T> tDependencyGraph) {

  }

  public String toString() {
    return "Class Node [" + clazz.getSimpleName() + "]";
  }
}
