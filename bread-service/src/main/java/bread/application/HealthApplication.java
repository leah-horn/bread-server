package bread.application;

import bread.resource.HealthResource;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.ws.rs.core.Application;

public class HealthApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.of(HealthResource.class);
  }
}
