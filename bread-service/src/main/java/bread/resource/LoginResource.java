package bread.resource;

import java.util.Arrays;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

@Path("/user")
public class LoginResource {

  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void login(@HeaderParam("username") String username,
      @HeaderParam("password") String password, @FormParam("remember-me") boolean rememberMe) {

    Subject currentUser = SecurityUtils.getSubject();

    if (!currentUser.isAuthenticated()) {
      UsernamePasswordToken token = new UsernamePasswordToken(username, password);
      //Arrays.fill(password, '\0'); TODO would be nice to not keep this around in memory

      token.setRememberMe(rememberMe);
      currentUser.login(token);
    }
  }

  @Path("/logout")
  @GET
  public void logout() {
    SecurityUtils.getSubject().logout();
  }
}
