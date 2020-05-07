package bread.dataaccess;

import bread.object.Roles;
import bread.object.User;
import java.util.Collection;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserAccess {

  /**
   * Retrieves user information by e-mail and password. If password is supplied, authorization
   * details will be checked and the resultant User object will include authorization
   * success/failure
   *
   * @param email    User principal. Must not be blank
   * @param password User password or authorization details. May be null or empty
   * @return User details. Will not be null. If password was blank or incorrect, authorization will
   * always be false
   */
  User retrieve(String email, char[] password);

  /**
   * Creates a user with given user details and credentials
   *
   * @param newUser  User details representing new user to create. Must not be null
   * @param password User password or authorization details. Must not be null or empty
   * @return true if the provided user was able to be created, false otherwise
   */
  boolean create(User newUser, char[] password);

  /**
   * Retrieves authorizations applied to a user principal
   *
   * @param email User principal. Must not be blank
   * @return Collection of roles available to the user principal. Will not be null
   */
  Collection<Roles> retrieveAuthorizations(String email);
}
