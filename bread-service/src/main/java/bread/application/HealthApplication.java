package bread.application;

import bread.resource.HealthResource;
import com.google.common.collect.ImmutableSet;

import javax.ws.rs.core.Application;
import java.util.Set;

public class HealthApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return ImmutableSet.of(HealthResource.class);
    }
}
