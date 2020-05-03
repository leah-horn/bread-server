package bread.resource;

import bread.rest.object.ImmutableUser;
import bread.rest.object.User;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/user")
public class UserResource {

  @GET
  @Produces("application/json")
  public User getUser() {
    return ImmutableUser.builder().displayName("Test User").email("hello@world.com").build();
  }
}
