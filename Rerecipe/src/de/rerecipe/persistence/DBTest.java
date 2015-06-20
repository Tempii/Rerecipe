package de.rerecipe.persistence;

import java.util.ArrayList;
import java.util.List;

import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;

public class DBTest {

	public static void main(String[] args) {
		commentTest();
	}

	public static void recipeTest() {
		System.out.println(RecipesDatabase.getRecipe(3));

	}
	
	public static void commentTest() {
		RecipesDatabase.getComments(9,1,2).stream().forEach(System.out::println);
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
}
