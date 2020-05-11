package bread.binder;

import bread.auth.AuthenticationLifecycle;
import bread.auth.BreadAuth;
import bread.dataaccess.UserAccess;
import javax.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

public class AuthBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bindAsContract(BreadAuth.class).to(BreadAuth.class);
    bindAsContract(AuthenticationLifecycle.class)
        .to(ContainerLifecycleListener.class).in(Singleton.class);
  }
}
