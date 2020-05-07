package bread.object;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUser.class)
@JsonSerialize(as = ImmutableUser.class)
public interface User {

  List<Token> getTokens();

  String getEmail();

  String getDisplayName();

  boolean isAuthorized();

  boolean isAccountLocked();

  List<String> getRoles();

}
