package bread.dataaccess.fs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import bread.dataaccess.UserAccess;
import bread.object.User;
import org.junit.jupiter.api.Test;

public class UserAccessImplTest {

  @Test
  public void delme() {
    UserAccess userAccess = new UserAccessImpl();
    User dummyUser = userAccess.retrieve("dummy_user", "incorrect".toCharArray());
    assertThat(dummyUser.getEmail(), equalTo("dummy_user"));
    assertThat(dummyUser.isAuthorized(), equalTo(false));
  }
}
