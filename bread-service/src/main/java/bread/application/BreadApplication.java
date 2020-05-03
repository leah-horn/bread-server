package bread.application;

import bread.resource.RecipeResource;
import bread.resource.UserResource;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.ws.rs.core.Application;

public class BreadApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.of(UserResource.class, RecipeResource.class);
  }
}
