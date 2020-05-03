package bread.rest.object;

import org.immutables.value.Value;

@Value.Immutable
public interface Amount {

  long getQuantity();

  String getUnitName();
}
