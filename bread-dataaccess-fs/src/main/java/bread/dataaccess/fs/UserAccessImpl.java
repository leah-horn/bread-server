package bread.dataaccess.fs;

import bread.dataaccess.UserAccess;
import bread.object.ImmutableUser;
import bread.object.Roles;
import bread.object.User;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.jvnet.hk2.annotations.Service;

@Service
public class UserAccessImpl implements UserAccess {

  private final static File sourceFile;
  private final static File passwordFile;
  private final static File authorizationsFile;

  static {
    File breadServiceFolder = FileUtils.getFile(FileUtils.getUserDirectory(), "bread-service");
    if (!breadServiceFolder.exists()) {
      breadServiceFolder.mkdir();
    } else if (!breadServiceFolder.isDirectory()) {
      throw new RuntimeException(breadServiceFolder.getAbsolutePath() + " is not a directory");
    }

    // Create local source files if needed, otherwise use existing yaml from previous sessions. sourceFile should be initialized
    // last, since it relies on the password and authorizations files existing. This implementation is a series of awful hacks
    passwordFile = createFileIfNonExistent(breadServiceFolder, "user_passwords.yaml", null);
    authorizationsFile = createFileIfNonExistent(breadServiceFolder, "user_passwords.yaml", null);
    sourceFile = createFileIfNonExistent(breadServiceFolder, "user.yaml",
        UserAccessImpl::createDummyUserDocument);
  }

  private static File createFileIfNonExistent(File breadServiceFolder, String filename,
      Runnable afterCreate) {
    File newFile = FileUtils.getFile(breadServiceFolder, filename);
    if (!newFile.exists()) {
      try {
        newFile.createNewFile();
        if (afterCreate != null) {
          afterCreate.run();
        }
      } catch (IOException e) {
        throw new RuntimeException(
            "Could not create user password file " + newFile.getAbsolutePath(), e);
      }
    } else if (!newFile.isFile()) {
      throw new RuntimeException(newFile.getAbsolutePath() + " exists but is not a file");
    }
    return newFile;
  }

  public UserAccessImpl() {

  }


  @Override
  public User retrieve(String email, char[] password) {
    synchronized (sourceFile) {

      ObjectMapper mapper = new ObjectMapper(
          new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
      try (InputStream is = new FileInputStream(sourceFile)) {
        MapType typeReference = TypeFactory.defaultInstance()
            .constructMapType(HashMap.class, String.class, User.class);
        Map<String, User> userData = mapper.readValue(is, typeReference);
        if (!userData.containsKey(email)) {
          throw new NotFoundException("Could not find user " + email);
        }

        // Definitely don't read the entire plaintext password set into memory in a real implementation
        MapType passwordReference = TypeFactory.defaultInstance()
            .constructMapType(HashMap.class, String.class, String.class);
        Map<String, String> passwords = mapper.readValue(passwordFile, Map.class);
        if (!passwords.containsKey(email)) {
          throw new IllegalStateException("Could not find password for user " + email);
        }
        User savedUser = userData.get(email);
        return ImmutableUser.builder().from(savedUser)
            .isAuthorized(passwords.get(email).equals(password))
            .build();
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public boolean create(User newUser, char[] password) {
    synchronized (sourceFile) {
      ObjectMapper mapper = new ObjectMapper(
          new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
      try {
        Map<String, User> userData = mapper.readValue(sourceFile, Map.class);
        if (userData.containsKey(newUser.getEmail())) {
          return false;
        }
        userData.put(newUser.getEmail(), newUser);
        mapper.writeValue(sourceFile, userData);

        Map<String, String> passwords = new HashMap<>();
        passwords.put(newUser.getEmail(), String.valueOf(password));
        // Do not write plaintext passwords in a real implementation!
        mapper.writeValue(passwordFile, passwords);

        return true;
      } catch (IOException e) {
        throw new RuntimeException("Could not read from user data file", e);
      }
    }
  }

  @Override
  public Collection<Roles> retrieveAuthorizations(String email) {
    ObjectMapper mapper = new ObjectMapper(
        new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
    try {
      CollectionType rolesList = TypeFactory.defaultInstance()
          .constructCollectionType(List.class, Roles.class);
      JavaType stringType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
      MapType typeReference = TypeFactory.defaultInstance()
          .constructMapType(HashMap.class, stringType, rolesList);

      Map<String, List<Roles>> authorizationData = mapper.readValue(sourceFile, typeReference);
      if (!authorizationData.containsKey(email)) {
        return Collections.EMPTY_LIST;
      }
      return authorizationData.get(email);
    } catch (IOException e) {
      throw new RuntimeException("Could not retrieve authorizations for user " + email);
    }
  }

  private static void createDummyUserDocument() {
    ObjectMapper mapper = new ObjectMapper(
        new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
    Map<String, User> users = new HashMap<>();
    users.put("dummy_user", ImmutableUser.builder()
        .displayName("Dummy User")
        .email("dummy_user")
        .isAuthorized(false)
        .isAccountLocked(true)
        .build());
    try {
      mapper.writeValue(sourceFile, users);
      Map<String, String> passwords = new HashMap<>();
      passwords.put("dummy_user", "dummy_password");
      mapper.writeValue(passwordFile, passwords);
    } catch (IOException e) {
      throw new RuntimeException("Could not write dummy details to file");
    }
  }
}
