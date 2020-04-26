package bread.application;

import bread.resource.HealthResource;
import bread.resource.UserResource;
import com.google.common.collect.ImmutableSet;

import javax.ws.rs.core.Application;
import java.util.Set;

public class BreadApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return ImmutableSet.of(UserResource.class);
    }
}
