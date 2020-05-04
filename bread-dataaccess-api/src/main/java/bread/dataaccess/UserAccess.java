package bread.dataaccess;

import bread.object.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserAccess {

  User retrieve(String username, char[] password);
  boolean create(User newUser);

}
