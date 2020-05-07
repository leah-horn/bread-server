package bread.test.util;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Binds a specific instance of a class as a service
 *
 * @param <T> Service instance to bind
 */
public class TestBinder<T> extends AbstractBinder {

  private final Class<? super T> clazz;
  private final T service;

  private TestBinder(Class<? super T> clazz, T service) {
    this.clazz = clazz;
    this.service = service;
  }

  /**
   * Creates a binder for an individual instance, possibly a mock
   *
   * @param clazz   Service type to bind
   * @param service Service instance to bind
   * @param <T>     Class type of instance to be bound
   * @return HK2 binder for given instance and service class
   */
  public static <T> TestBinder<T> bindInstance(Class<? super T> clazz, T service) {
    return new TestBinder(clazz, service);
  }

  @Override
  protected void configure() {
    bind(service)
        .to(clazz);
  }
}
