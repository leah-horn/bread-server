package bread.resource;

import bread.auth.BreadAuth;
import bread.dataaccess.UserAccess;
import bread.test.util.TestBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginResourceTest {

  public static final String USERNAME = "username";
  @Mock
  private BreadAuth authService;
  private ServiceLocator locator;

  private LoginResource testInstance;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);

    ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
    locator = ServiceLocatorUtilities
        .bind(TestBinder.bindInstance(BreadAuth.class, authService));
    testInstance = locator.createAndInitialize(LoginResource.class);

  }

  // TODO either going to need to abstract away Shiro code or make a test implementation
  /*
  @Test
  public void testSomething() {
    testInstance.login(USERNAME, "password".toCharArray());
  }
   */
}
