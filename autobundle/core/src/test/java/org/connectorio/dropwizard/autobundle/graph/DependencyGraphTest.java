package org.connectorio.dropwizard.autobundle.graph;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.setup.Environment;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.graph.DependencyGraph.DependencyComparator;
import org.junit.jupiter.api.Test;

class DependencyGraphTest {

  DependencyGraph<Object> dependencyMap = new DependencyGraph<>(new DependencyComparator<>());

  @Test
  void dependencyTest() {
    dependencyMap.put(new JWTAutoBundle());
    dependencyMap.put(new DataSourceAutoBundle());
    dependencyMap.put(new TenantClientAutoBundle());
    dependencyMap.put(new TenantResolverAutoBundle());
    dependencyMap.put(new JerseyClientTracingAutoBundle());
    dependencyMap.put(new ZipkinAutoBundle());
    dependencyMap.put(new MigrationsAutoBundle());
    dependencyMap.put(new GatewayClientAutoBundle());
    dependencyMap.put(new JerseyTracingAutoBundle());
    dependencyMap.put(new JerseyClientAutoBundle());
    dependencyMap.put(new ServletTracingAutoBundle());

    List<BundleNode<Object>> sort = dependencyMap.sort();
    System.out.println(sort);
    assertThat(sort).last().extracting(Node::getType)
      .isEqualTo(TenantResolverAutoBundle.class);
  }


  static class BaseBundle implements AutomaticBundle<Object> {
    @Override
    public void run(Object configuration, Environment environment, Map<Class<?>, AutomaticBundle<Object>> bundles) throws Exception {
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  static class ZipkinAutoBundle extends BaseBundle {
  }

  static class DataSourceAutoBundle extends BaseBundle {
  }

  static class MigrationsAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Collections.singletonList(DataSourceAutoBundle.class);
    }
  }

  static class JWTAutoBundle extends BaseBundle {
  }

  static class JerseyClientAutoBundle extends BaseBundle {
  }

  static class JerseyTracingAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Collections.singletonList(ZipkinAutoBundle.class);
    }
  }

  static class ServletTracingAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Collections.singletonList(ZipkinAutoBundle.class);
    }
  }

  static class JerseyClientTracingAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Arrays.asList(ZipkinAutoBundle.class, JerseyClientAutoBundle.class);
    }
  }

  static class GatewayClientAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Arrays.asList(JerseyClientTracingAutoBundle.class);
    }
  }

  static class TenantClientAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Arrays.asList(JerseyClientTracingAutoBundle.class);
    }
  }

  static class TenantResolverAutoBundle extends BaseBundle {
    @Override
    public List<Class<?>> getDependencies() {
      return Arrays.asList(TenantClientAutoBundle.class);
    }
  }

}