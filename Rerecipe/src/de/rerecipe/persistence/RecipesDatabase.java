package de.rerecipe.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.rerecipe.model.Comment;
import de.rerecipe.model.Ingredient;
import de.rerecipe.model.Recipe;
import de.rerecipe.model.RecipeResult;

public class RecipesDatabase {
	
	public static List<RecipeResult> getRecipeResults(List<String> filterOptions,
			List<String> ingredients) { // FIXME
		List<RecipeResult> result = new ArrayList<RecipeResult>();
		List<Ingredient> ingredient = new ArrayList<Ingredient>();
		ingredient.add(new Ingredient(1, "test11", "test12", false, false));
		ingredient.add(new Ingredient(2, "test21", "test22", false, false));
		ingredient.add(new Ingredient(3, "test31", "test32", false, false));
		for (int i = 0; i<10; i++) {
			result.add(new RecipeResult(1,"Kuchen","img/kirby.png",20,3,ingredient));
		}
		return result;
	}

	public static Recipe getRecipe(RecipeResult recipeResult) {
		String select = "SELECT r_author, r_description "
				+ " FROM T_Recipe WHERE r_id = ?";
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			stmt.setInt(1, recipeResult.getId());
			try (ResultSet result = stmt.executeQuery()) {
				if (result.next()) {
					Recipe recipe = new Recipe(recipeResult,
							result.getString("r_author"),
							result.getString("r_description"));
					return recipe;
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Comment> getComments(Recipe recipe) {
		String select = "SELECT c_author, r_rate, r_comment "
				+ " FROM T_Rating WHERE r_id = ?";
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			stmt.setInt(1, recipe.getId());
			try (ResultSet result = stmt.executeQuery()) {
				List<Comment> comments = new ArrayList<>();
				while (result.next()) {
					Comment comment = new Comment(result.getString("c_author"),
							result.getInt("r_rate"),
							result.getString("r_comment"));
					comments.add(comment);
				}
				return comments;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public static void addRecipe(Recipe recipe) {
	}

	public static String[] getIngredientNames(String keyword) {
		String select = "SELECT i_Name FROM T_Ingredient WHERE i_Name LIKE '%"
				+ keyword + "%'";

		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			List<String> ingredients = new ArrayList<String>();

			try (ResultSet result = stmt.executeQuery()) {
				while (result.next()) {
					ingredients.add(result.getString("i_Name"));
				}
				String[] recipes = (String[]) ingredients
						.toArray(new String[ingredients.size()]);
				return recipes;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}

}
