package de.rerecipe.persistence;

import java.util.ArrayList;
import java.util.List;

import de.rerecipe.model.Comment;
import de.rerecipe.model.Recipe;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;

public class DBTest {

	public static void main(String[] args) {
		test3();
	}

	public static void test1() {
		RecipeResult recipeResult = new RecipeResult(1, "Kartoffelbrei",
				"null", 20, 3.5, 0);
		Recipe recipe = RecipesDatabase.getRecipe(recipeResult);
		System.out.println(recipe.getAuthor() + recipe.getDescription());
		List<Comment> comments = RecipesDatabase.getComments(recipe);
		for (Comment comment : comments) {
			System.out.println(comment.getAuthor() + "(" + comment.getRating()
					+ "): " + comment.getContent());
		}
	}

	public static void test2() {
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Vegan");
		List<EnteredIngredient> ingredients = new ArrayList<>();
		ingredients.add(new EnteredIngredient("Kartoffel", 1000));
		ingredients.add(new EnteredIngredient("Zucker", 1000));
		List<RecipeResult> results = RecipesDatabase
				.getMissIngrResults(new Search(ingredients, filterOptions,
						"r_name"));
		for (RecipeResult missSearchResult : results) {
			System.out.println(missSearchResult);
		}
	}

	public static void test3() {
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Vegan");
		List<EnteredIngredient> ingredients = new ArrayList<>();
		ingredients.add(new EnteredIngredient("Kartoffel", 1));
		ingredients.add(new EnteredIngredient("Zucker", 1));
//		ingredients.add(new EnteredIngredient("Wasser", 1));
		List<RecipeResult> results = RecipesDatabase
				.getDoableResults(new Search(ingredients, filterOptions,
						"r_name"));
		for (RecipeResult missSearchResult : results) {
			System.out.println(missSearchResult);
		}
	}

	public static void test4() {
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Vegan");
		List<EnteredIngredient> ingredients = new ArrayList<>();
		ingredients.add(new EnteredIngredient("Kartoffel", 1));
		ingredients.add(new EnteredIngredient("Zucker", 1));
//		ingredients.add(new EnteredIngredient("Wasser", 1));
		List<RecipeResult> results = RecipesDatabase
				.getResults(new Search(ingredients, filterOptions,
						"r_name"));
		for (RecipeResult missSearchResult : results) {
			System.out.println(missSearchResult);
		}
	}
}
