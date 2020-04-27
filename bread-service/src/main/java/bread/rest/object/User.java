package bread.rest.object;

import org.immutables.value.Value;

@Value.Immutable
public interface User {
    String getDisplayName();
    String getEmail();
}
