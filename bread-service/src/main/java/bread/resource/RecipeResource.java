package bread.resource;

import bread.rest.object.ImmutableAmount;
import bread.rest.object.ImmutableContents;
import bread.rest.object.ImmutableIngredient;
import bread.rest.object.ImmutableIngredientAmount;
import bread.rest.object.ImmutableRecipe;
import bread.rest.object.ImmutableStep;
import bread.rest.object.Ingredient;
import bread.rest.object.Recipe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/recipe")
public class RecipeResource {

  private static final List<Recipe> sampleRecipes = new ArrayList<>();
  private static final Map<String, Recipe> recipeMap = new HashMap<>();

  static {
    sampleRecipes.add(ImmutableRecipe.builder()
        .identifier("test_1")
        .title("Bread")
        .description("Description 1")
        .build());
    sampleRecipes.add(ImmutableRecipe.builder()
        .identifier("test_2")
        .title("Sourdough")
        .description("Description 2")
        .build());

    Ingredient flour = ImmutableIngredient.builder().name("Flour").build();
    Ingredient water = ImmutableIngredient.builder().name("Water").build();
    Ingredient salt = ImmutableIngredient.builder().name("Salt").build();
    Ingredient yeast = ImmutableIngredient.builder().name("Yeast").build();
    Ingredient starter = ImmutableIngredient.builder().name("Starter").build();

    recipeMap.put("test_1",
        ImmutableRecipe.builder()
            .identifier("test_1")
            .title("Bread")
            .description("Description 1")
            .contents(ImmutableContents.builder()
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(flour)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(1000)
                        .unitName("grams")
                        .build())
                    .build())
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(water)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(650)
                        .unitName("grams")
                        .build())
                    .build())
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(salt)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(20)
                        .unitName("grams")
                        .build())
                    .build())
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(yeast)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(8)
                        .unitName("grams")
                        .build())
                    .build())
                .addSteps(ImmutableStep.builder()
                    .description("Step 1")
                    .build())
                .addSteps(ImmutableStep.builder()
                    .description("Step 2")
                    .build())
                .addSteps(ImmutableStep.builder()
                    .description("Step 3")
                    .build())
                .build())
            .build());
    recipeMap.put("test_2",
        ImmutableRecipe.builder()
            .identifier("test_2")
            .title("Sourdough")
            .description("Description 2")
            .contents(ImmutableContents.builder()
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(flour)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(1000)
                        .unitName("grams")
                        .build())
                    .build())
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(water)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(650)
                        .unitName("grams")
                        .build())
                    .build())
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(salt)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(20)
                        .unitName("grams")
                        .build())
                    .build())
                .addTotalIngredients(ImmutableIngredientAmount.builder()
                    .ingredient(yeast)
                    .putAmount("metric", ImmutableAmount.builder()
                        .quantity(8)
                        .unitName("grams")
                        .build())
                    .build())
                .addSteps(ImmutableStep.builder()
                    .description("Step 1")
                    .build())
                .addSteps(ImmutableStep.builder()
                    .description("Step 2")
                    .build())
                .addSteps(ImmutableStep.builder()
                    .description("Step 3")
                    .build())
                .build())
            .build());
  }

  @GET
  @Produces("application/json")
  public Collection<Recipe> getRecipes() {
    return sampleRecipes;
  }

  @GET
  @Produces("application/json")
  @Path("/{identifier}")
  public Recipe getRecipes(
      @PathParam("identifier") String identifier) {

    Recipe recipe = recipeMap.get(identifier);
    if (recipe == null) {
      throw new NotFoundException("Could not find [" + identifier + "]");
    }

    return recipe;
  }

}
