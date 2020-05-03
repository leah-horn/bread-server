package bread.rest.object;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
public interface Recipe {

  String getIdentifier();

  String getTitle();

  String getDescription();

  /**
   * @return Contents of recipe. May be null
   */
  @Nullable
  Contents getContents();

  @Value.Immutable
  public interface Contents {

    List<IngredientAmount> getTotalIngredients();

    List<Step> getSteps();
  }

  @Value.Immutable
  public interface IngredientAmount {

    Ingredient getIngredient();

    /**
     * @return mapping of system of measurement to quantity of ingredient
     */
    Map<String, Amount> getAmount();
  }
}
