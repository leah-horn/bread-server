package bread.binder;

import bread.auth.BreadAuth;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AuthBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bindAsContract(BreadAuth.class).to(BreadAuth.class);
  }
}
