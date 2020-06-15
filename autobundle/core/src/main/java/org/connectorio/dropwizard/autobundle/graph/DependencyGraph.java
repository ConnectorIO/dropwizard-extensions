package org.connectorio.dropwizard.autobundle.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;

public class DependencyGraph<T> extends HashMap<Class<?>, BundleNode<T>> {

  private final Comparator<BundleNode<T>> comparator;

  public DependencyGraph() {
    this(new DependencyComparator<>());
  }

  public DependencyGraph(Comparator<BundleNode<T>> comparator) {
    this.comparator = comparator;
  }

  public void put(AutomaticBundle<T> bundle) {
    put(bundle.getClass(), new BundleNode<>(bundle));
  }

  public List<BundleNode<T>> sort() {
    List<BundleNode<T>> nodes = new ArrayList<>();
    for (BundleNode<T> node : this.values()) {
      node.link(this);
      nodes.add(node);
    }

    Collections.sort(nodes, comparator);
    return nodes;
  }

  static class DependencyComparator<T> implements Comparator<BundleNode<T>> {
    @Override
    public int compare(BundleNode<T> o1, BundleNode<T> o2) {
      // check direct dependencies
      if (o1.dependsOn(o2)) {
        return 1;
      }
      if (o2.dependsOn(o1)) {
        return -1;
      }

      // above nodes are not dependant on each other, so in theory they're equal, but in order to
      // distinguish these and avoid side effects we make sure that sorting uses name of the wrapped
      // bundle types. By this way we will return "0=equal" only if we have duplicate bundle
      // registration
      return o1.getType().getName().compareTo(o2.getType().getName());
    }

  }

}
