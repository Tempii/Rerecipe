package de.rerecipe.persistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.rerecipe.model.Comment;
import de.rerecipe.model.Ingredient;
import de.rerecipe.model.Recipe;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;

public class DBTest {

	public static void main(String[] args) {
		getRating();
	}

	public static void recipeTest() {
		System.out.println(RecipesDatabase.getRecipe(3));

	}

	public static void commentTest() {
		RecipesDatabase.getComments(9, 1, 2).stream()
				.forEach(System.out::println);
	}

	public static void recipeResultTest() {
		List<String> filterOptions = new ArrayList<String>();
		filterOptions.add("Vegetarian");
		List<EnteredIngredient> ingredients = new ArrayList<>();
		ingredients.add(new EnteredIngredient("Kartoffel", 1000));
		ingredients.add(new EnteredIngredient("Zucker", 1000));
		ingredients.add(new EnteredIngredient("Wasser", 1000));
		ingredients.add(new EnteredIngredient("Milch", 1000));
		List<RecipeResult> results = RecipesDatabase.getResults(new Search(
				ingredients, filterOptions, "rating desc", 1, 5));
		results.stream().forEach(System.out::println);
	}

	public static void addRecipeTest() {
		String name = "testName";
		int preparationTime = 20;
		String author = "testAuthor";
		String description = "testDescription";
		Map<Ingredient, Integer> ingredients = new LinkedHashMap<Ingredient, Integer>();
		for (int i = 0; i < 5; i++)
			ingredients.put(new Ingredient(i), i);

		Recipe recipe = new Recipe(0, name, preparationTime, 0, 0, ingredients,
				author, description);
		RecipesDatabase.addRecipe(recipe);
		System.out.println("success!");
	}

	public static void addCommentTest(){
		int r_id = 1;
		String author = "testAuthor";
		int rating = 5;
		String content = "mega geil!";
		
		Comment comment = new Comment(r_id, author, rating, content);
		
		RecipesDatabase.addComment(comment);
		System.out.println("success!");
	}
	
	public static void getRating(){
		System.out.println(RecipesDatabase.getRecipeRating(1));
	}
	
	
}
