package bread.resource;

import java.util.Arrays;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

@Path("/user")
public class LoginResource {

  @Path("/login")
  @POST
  public String login(@HeaderParam("username") String username, @HeaderParam("password") char[] password) {

    Subject currentUser = SecurityUtils.getSubject();

    if (username.equals("test") && Arrays.equals(password, "password".toCharArray())) {
      UsernamePasswordToken token = new UsernamePasswordToken("username", "password");
      Arrays.fill(password, '\0');

      token.setRememberMe(true);
      currentUser.login(token);

      return "";
    } else {
      throw new NotAuthorizedException("Could not log in");
    }
  }

  @Path("/logout")
  @GET
  public void logout() {
    SecurityUtils.getSubject().logout();
  }
}
