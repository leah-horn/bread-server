package bread.dataaccess.fs;

import bread.dataaccess.UserAccess;
import bread.object.ImmutableUser;
import bread.object.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.jvnet.hk2.annotations.Service;

@Service
public class UserAccessImpl implements UserAccess {

  private final static File sourceFile;
  private final static File passwordFile;

  static {
    File breadServiceFolder = FileUtils.getFile(FileUtils.getUserDirectory(), "bread-service");
    if (!breadServiceFolder.exists()) {
      breadServiceFolder.mkdir();
    } else if (!breadServiceFolder.isDirectory()) {
      throw new RuntimeException(breadServiceFolder.getAbsolutePath() + " is not a directory");
    }
    passwordFile = FileUtils.getFile(breadServiceFolder, "user_passwords.yaml");
    if (!passwordFile.exists()) {
      try {
        passwordFile.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException(
            "Could not create user password file " + passwordFile.getAbsolutePath(), e);
      }
    } else if (!passwordFile.isFile()) {
      throw new RuntimeException(passwordFile.getAbsolutePath() + " exists but is not a file");
    }

    sourceFile = FileUtils.getFile(breadServiceFolder, "user.yaml");
    if (!sourceFile.exists()) {
      try {
        sourceFile.createNewFile();
        createDummyUserDocument();
      } catch (IOException e) {
        throw new RuntimeException(
            "Could not create user data file " + sourceFile.getAbsolutePath(), e);
      }
    } else if (!sourceFile.isFile()) {
      throw new RuntimeException(sourceFile.getAbsolutePath() + " exists but is not a file");
    }
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

  private static void createDummyUserDocument() throws IOException {
    ObjectMapper mapper = new ObjectMapper(
        new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
    Map<String, User> users = new HashMap<>();
    users.put("dummy_user", ImmutableUser.builder()
        .displayName("Dummy User")
        .email("dummy_user")
        .isAuthorized(false)
        .isAccountLocked(true)
        .build());
    mapper.writeValue(sourceFile, users);
    Map<String, String> passwords = new HashMap<>();
    passwords.put("dummy_user", "dummy_password");
    mapper.writeValue(passwordFile, passwords);
  }
}
