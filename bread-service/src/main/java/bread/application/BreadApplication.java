package bread.application;

import bread.binder.AuthBinder;
import bread.resource.LoginResource;
import bread.resource.RecipeResource;
import bread.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;

public class BreadApplication extends ResourceConfig {

  public BreadApplication() {
    super();
    register(UserResource.class)
      .register(RecipeResource.class)
      .register(UserResource.class)
      .register(LoginResource.class)
      .register(new AuthBinder());
  }
}
