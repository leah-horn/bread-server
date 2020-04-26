package bread.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class HealthResource {
    @GET
    public boolean isHealthy() {
        return true;
    }
}
