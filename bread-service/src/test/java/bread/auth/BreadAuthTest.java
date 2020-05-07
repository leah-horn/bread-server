package bread.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import bread.dataaccess.UserAccess;
import bread.object.ImmutableUser;
import bread.object.Roles;
import bread.object.User;
import bread.test.util.TestBinder;
import java.util.Collection;
import java.util.Collections;
import javax.ws.rs.NotAuthorizedException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BreadAuthTest {

  private static final String USERNAME = "username";
  private static final char[] PASSWORD = "password".toCharArray();
  private static final UsernamePasswordToken TOKEN = new UsernamePasswordToken(USERNAME, PASSWORD);
  private static final User DEFAULT_USER_RESPONSE = ImmutableUser.builder()
      .email("username")
      .displayName("User Name")
      .isAuthorized(true)
      .isAccountLocked(false)
      .build();
  private static final PrincipalCollection PRINCIPAL_COLLECTION = new SimplePrincipalCollection(
      USERNAME, "user");

  @Mock
  private UserAccess userAccessService;
  private ServiceLocator locator;

  private BreadAuth testInstance;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);

    ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
    locator = ServiceLocatorUtilities
        .bind(TestBinder.bindInstance(UserAccess.class, userAccessService));
    testInstance = locator.createAndInitialize(BreadAuth.class);
  }

  @AfterEach
  public void tearDown() {
    locator.shutdown();
  }

  @Test
  public void testAuthenticationSuccess() {
    when(userAccessService.retrieve(USERNAME, PASSWORD)).thenReturn(DEFAULT_USER_RESPONSE);
    AuthenticationInfo authInfo = testInstance.doGetAuthenticationInfo(TOKEN);
    assertThat(authInfo.getPrincipals().getPrimaryPrincipal(), equalTo(USERNAME));
    assertThat(authInfo.getCredentials(), equalTo(PASSWORD));
  }

  @Test
  public void testAuthenticationFailureUnauthorized() {
    when(userAccessService.retrieve(USERNAME, PASSWORD))
        .thenReturn(ImmutableUser.copyOf(DEFAULT_USER_RESPONSE)
            .withIsAuthorized(false));
    NotAuthorizedException expected = assertThrows(NotAuthorizedException.class,
        () -> testInstance.doGetAuthenticationInfo(TOKEN));
    assertThat(expected.getResponse().getStatus(), equalTo(401));
  }

  @Test
  public void testAuthenticationFailureLocked() {
    when(userAccessService.retrieve(USERNAME, PASSWORD))
        .thenReturn(ImmutableUser.copyOf(DEFAULT_USER_RESPONSE)
            .withIsAccountLocked(true));
    NotAuthorizedException expected = assertThrows(NotAuthorizedException.class,
        () -> testInstance.doGetAuthenticationInfo(TOKEN));
    assertThat(expected.getResponse().getStatus(), equalTo(401));
  }

  @Test
  public void testAuthenticationNoToken() {
    when(userAccessService.retrieve(USERNAME, PASSWORD)).thenReturn(DEFAULT_USER_RESPONSE);
    AuthenticationInfo authInfo = testInstance.doGetAuthenticationInfo(null);
    assertThat(authInfo, equalTo(null));
  }

  @Test
  public void testAuthorizationSuccess() {
    Collection<Roles> expectedRoles = Collections.singleton(Roles.ADMIN);
    when(userAccessService.retrieveAuthorizations(USERNAME)).thenReturn(expectedRoles);

    AuthorizationInfo authInfo = testInstance.doGetAuthorizationInfo(PRINCIPAL_COLLECTION);
    assertThat(authInfo.getRoles(), equalTo(Collections.singleton("ADMIN")));
  }
}
