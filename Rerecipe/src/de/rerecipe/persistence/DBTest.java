package de.rerecipe.persistence;

import java.util.ArrayList;
import java.util.List;

import de.rerecipe.model.Comment;
import de.rerecipe.model.Recipe;
import de.rerecipe.model.RecipeResult;

public class DBTest {

	public static void main(String[] args) {
		test2();
	}

	private static void test1() {
		RecipeResult recipeResult = new RecipeResult(1, "Kartoffelbrei",
				"null", 20, 3.5, null);
		Recipe recipe = RecipesDatabase.getRecipe(recipeResult);
		System.out.println(recipe.getAuthor() + recipe.getDescription());
		List<Comment> comments = RecipesDatabase.getComments(recipe);
		for (Comment comment : comments) {
			System.out.println(comment.getAuthor() + "(" + comment.getRating()
					+ "): " + comment.getContent());
		}
	}

	private static void test2() {
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Vegan");
		List<String> ingredients = new ArrayList<>();
		ingredients.add("Kartoffel");
		ingredients.add("Zucker");
//		String str = RecipesDatabase.getRecipeResults(filterOptions, ingredients);
//		System.out.println(str);
	}

}
