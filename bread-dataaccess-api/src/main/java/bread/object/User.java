package bread.object;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface User {
    List<Token> getTokens();
    String getEmail();
    String getDisplayName();
    String getPassword();
}
