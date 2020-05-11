package bread.auth;

import bread.dataaccess.UserAccess;
import bread.object.Roles;
import bread.object.User;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class BreadAuth extends AuthorizingRealm {

  private static final String REALM_NAME = "user";

  @Inject
  private UserAccess userAccess;

  public BreadAuth() {
    setCachingEnabled(true);
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    // If no token was provided, no auth info can be found
    if (token == null) {
      return null;
    }
    try {
      UsernamePasswordToken upToken = (UsernamePasswordToken) token;
      User user = userAccess.retrieve(upToken.getUsername(), upToken.getPassword());
      if (!user.isAuthorized() || user.isAccountLocked()) {
        throw new NotAuthorizedException("User credentials not valid");
      }

      SimplePrincipalCollection principles = new SimplePrincipalCollection(user.getEmail(),
          REALM_NAME);
      SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principles,
          upToken.getPassword());
      return authenticationInfo;
    } catch (Exception e) {
      throw new AuthorizationException(e);
    }
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String principal = (String) principals.getPrimaryPrincipal();
    Collection<Roles> authorizations = userAccess.retrieveAuthorizations(principal);
    return new SimpleAuthorizationInfo(
        authorizations.stream().map(role -> role.toString()).collect(Collectors.toSet()));
  }
}
