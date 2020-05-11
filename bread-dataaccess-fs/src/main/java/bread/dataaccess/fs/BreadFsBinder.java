package bread.dataaccess.fs;

import bread.dataaccess.UserAccess;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class BreadFsBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bindAsContract(UserAccessImpl.class).to(UserAccess.class);
  }
}
