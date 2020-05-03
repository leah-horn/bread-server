package bread.rest.object;

import org.immutables.value.Value;

@Value.Immutable
public interface Step {

  String getDescription();
}
