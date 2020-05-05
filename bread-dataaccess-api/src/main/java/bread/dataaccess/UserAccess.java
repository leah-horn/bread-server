package bread.dataaccess;

import bread.object.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserAccess {

  User retrieve(String email, char[] password);
  boolean create(User newUser, char[] password);

}
