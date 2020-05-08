package bread.auth;

import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.spi.Contract;

@Contract
public class AuthenticationLifecycle implements ContainerLifecycleListener {

  @Inject
  private BreadAuth breadAuth;

  @Override
  public void onStartup(Container container) {
    SecurityManager securityManager = new DefaultSecurityManager(breadAuth);
    SecurityUtils.setSecurityManager(securityManager);
  }

  @Override
  public void onReload(Container container) {
  }

  @Override
  public void onShutdown(Container container) {
  }
}
