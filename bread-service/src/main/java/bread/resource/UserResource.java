package bread.resource;

import org.glassfish.jersey.server.model.Resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/user")
public class UserResource {
    @GET
    public String getUser() {
        return "Hello";
    }
}
