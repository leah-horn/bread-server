package bread.dataaccess.fs;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import bread.dataaccess.UserAccess;
import bread.object.User;
import org.junit.jupiter.api.Test;

public class UserAccessImplTest {

  @Test
  public void delme() {
    UserAccess userAccess = new UserAccessImpl();
    User dummyUser = userAccess.retrieve("dummy", "incorrect".toCharArray());
    assertNotNull(dummyUser);
  }
}
