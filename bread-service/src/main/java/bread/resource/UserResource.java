package bread.resource;

import bread.dataaccess.UserAccess;
import bread.rest.object.ImmutableUser;
import bread.rest.object.User;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@Path("/user")
public class UserResource {

  @Inject
  private UserAccess userAccess;

  @GET
  @Produces("application/json")
  public User getUser() {
    Subject currentUser = SecurityUtils.getSubject();
    if (!currentUser.isAuthenticated()) {
      throw new NotAuthorizedException("Unauthorized");
    }
    bread.object.User userInfo = userAccess.retrieve((String)currentUser.getPrincipal(), null);
    return ImmutableUser.builder()
        .displayName(userInfo.getDisplayName())
        .email(userInfo.getEmail())
        .build();
  }
}
