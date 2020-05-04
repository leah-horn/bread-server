package bread.dataaccess.fs;

import bread.dataaccess.UserAccess;
import bread.object.ImmutableUser;
import bread.object.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jvnet.hk2.annotations.Service;
import org.yaml.snakeyaml.Yaml;

@Service
public class UserAccessImpl implements UserAccess {

  private final static File sourceFile;

  static {
    File breadServiceFolder = FileUtils.getFile(FileUtils.getUserDirectory(), "bread-service");
    if (!breadServiceFolder.exists()) {
      breadServiceFolder.mkdir();
    } else if (!breadServiceFolder.isDirectory()) {
      throw new RuntimeException(breadServiceFolder.getAbsolutePath() + " is not a directory");
    }

    sourceFile = FileUtils.getFile(breadServiceFolder, "user.yaml");
    if (!sourceFile.exists()) {
      try {
        sourceFile.createNewFile();
        createDummyUserDocument();
      } catch (IOException e) {
        throw new RuntimeException("Could not create user data file " + sourceFile.getAbsolutePath(), e);
      }
    } else if (!sourceFile.isFile()) {
      throw new RuntimeException(sourceFile.getAbsolutePath() + " exists but is not a file");
    }
  }

  public UserAccessImpl() {

  }


  @Override
  public User retrieve(String username, char[] password) {
    synchronized(sourceFile) {
      Yaml yaml = new Yaml();
      try (InputStream is = new FileInputStream(sourceFile)) {
        Map<String, User> userData = yaml.load(is);

      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return null;
    }
  }

  @Override
  public boolean create(User newUser) {
    synchronized(sourceFile) {
      return false;
    }
  }

  private static void createDummyUserDocument() throws IOException {
    Yaml yaml = new Yaml();
    Map<String, User> users = new HashMap<String, User>();
    users.put("dummy", ImmutableUser.builder()
        .displayName("Dummy User")
        .email("dummy_user")
        .isAuthorized(false)
        .isAccountLocked(true)
        .build());
    String dump = yaml.dump(users);
    FileUtils.writeStringToFile(sourceFile, dump, StandardCharsets.UTF_8);

  }
}
