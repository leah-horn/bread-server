package bread.exception;

public class ResourceExistsException extends RuntimeException {

  public ResourceExistsException(String message) {
    super(message);
  }

  public ResourceExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceExistsException(Throwable cause) {
    super(cause);
  }
}
