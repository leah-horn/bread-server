package bread.object;

import org.immutables.value.Value;

@Value.Immutable
public interface Description {

  enum Format {
    XHTML,
    TEXT,
    MARKDOWN
  }

  Format getFormat();

  String getValue();
}
