package bread.object;

import org.immutables.value.Value;

@Value.Immutable
public interface Token {

  enum Type {
    GITHUB
  }

  Type getType();

  String getToken();
}
