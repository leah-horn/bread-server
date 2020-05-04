package bread.object;

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface User {

  List<Token> getTokens();
  String getEmail();
  String getDisplayName();
  boolean isAuthorized();
  boolean isAccountLocked();
  List<String> getRoles();

}
