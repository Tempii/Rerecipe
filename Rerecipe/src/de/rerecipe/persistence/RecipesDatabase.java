package de.rerecipe.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.rerecipe.model.Comment;
import de.rerecipe.model.Recipe;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;

public class RecipesDatabase {

	public static void main(String[] args) {
		DBTest.test4();
	}

	public static List<RecipeResult> getResults(Search search) {
		String filtered_ids = getFilteredIds(search.getFilterOptions());
		String ingredient_i_ids = getIngredientIds(search.getIngredients());
		String missIngr = getMissIngr(filtered_ids, ingredient_i_ids,
				search.getIngredients());
		String notDoableResults = getNotDoableResults(missIngr);
		String rating = getRating();
		String doableResults = getDoableResult(rating, notDoableResults,
				filtered_ids);

		StringBuilder builder = new StringBuilder();
		builder.append(doableResults);
		builder.append(" UNION ");
		builder.append(notDoableResults);
		
		builder.append(" ORDER BY missing_ingredients, ");
		builder.append(search.getOrderBy());
		String select = builder.toString();

		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			try (ResultSet result = stmt.executeQuery()) {

				List<RecipeResult> searchResults = new ArrayList<RecipeResult>();
				while (result.next()) {
					searchResults.add(new RecipeResult(result.getInt("r_id"),
							result.getString("r_name"), null, result
									.getInt("r_time"), result
									.getFloat("rating"), result
									.getInt("missing_ingredients")));
				}
				return searchResults;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<RecipeResult> getDoableResults(Search search) {
		String filtered_ids = getFilteredIds(search.getFilterOptions());
		String ingredient_i_ids = getIngredientIds(search.getIngredients());
		String missIngr = getMissIngr(filtered_ids, ingredient_i_ids,
				search.getIngredients());
		String notDoableResults = getNotDoableResults(missIngr);
		String rating = getRating();
		String doableResults = getDoableResult(rating, notDoableResults,
				filtered_ids);

		StringBuilder builder = new StringBuilder();
		
		builder.append(doableResults);
		
		String select = builder.toString();

		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			try (ResultSet result = stmt.executeQuery()) {

				List<RecipeResult> searchResults = new ArrayList<RecipeResult>();
				while (result.next()) {
					searchResults.add(new RecipeResult(result.getInt("r_id"),
							result.getString("r_name"), null, result
									.getInt("r_time"), result
									.getFloat("rating"), result.getInt("missing_ingredients")));
				}
				return searchResults;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getDoableResult(String rating,
			String notDoableResults, String filtered_ids) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT T_Recipe.r_id, T_Recipe.r_name, T_Recipe.r_time, ratingDoable.rating, 0 as missing_ingredients FROM T_Recipe, ");
		builder.append(rating);
		builder.append("AS ratingDoable ");
		builder.append(" WHERE T_Recipe.r_id = ratingDoable.r_id");
		builder.append(" AND T_Recipe.r_id NOT IN ");
		builder.append("(SELECT r_id FROM ");
		builder.append(notDoableResults);
		builder.append("AS notDoableResults");
		builder.append(")");

		builder.append(" AND T_Recipe.r_id IN ");

		builder.append("(SELECT r_id FROM ");
		builder.append(filtered_ids);
		builder.append(" as filtered_idsDoable ");
		builder.append(" GROUP BY r_id))");

		return builder.toString();
	}

	public static List<RecipeResult> getMissIngrResults(Search search) {
		String filtered_ids = getFilteredIds(search.getFilterOptions());
		String ingredient_i_ids = getIngredientIds(search.getIngredients());
		String missIngr = getMissIngr(filtered_ids, ingredient_i_ids,
				search.getIngredients());
		String notDoableResults = getNotDoableResults(missIngr);

		StringBuilder builder = new StringBuilder();

		builder.append(notDoableResults);

		builder.append(" ORDER BY missing_ingredients, ");
		builder.append(search.getOrderBy());
		String select = builder.toString();

		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			try (ResultSet result = stmt.executeQuery()) {

				List<RecipeResult> searchResults = new ArrayList<RecipeResult>();
				while (result.next()) {
					searchResults.add(new RecipeResult(result.getInt("r_id"),
							result.getString("r_name"), null, result
									.getInt("r_time"), result
									.getFloat("rating"), result
									.getInt("missing_ingredients")));
				}
				return searchResults;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getNotDoableResults(String missIngr) {
		String rating = getRating();

		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT T_Recipe.r_id, T_Recipe.r_name, T_Recipe.r_time, ratingNotDoable.rating, missIngr.missing_ingredients FROM T_Recipe, ");
		builder.append(missIngr);
		builder.append(" , ");
		builder.append(rating);
		builder.append(" AS ratingNotDoable ");
		builder.append(" WHERE T_Recipe.r_id = missIngr.r_id");
		builder.append(" AND T_Recipe.r_id = ratingNotDoable.r_id)");

		return builder.toString();
	}

	private static String getMissIngr(String filtered_ids,
			String ingredient_i_ids, List<EnteredIngredient> list) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT filtered_ids_MS.r_id, count(*) AS missing_ingredients FROM T_Recipe_Ingredient, T_Ingredient,");
		builder.append(filtered_ids);
		builder.append(" AS filtered_ids_MS ");
		builder.append(" WHERE filtered_ids_MS.i_id = T_Recipe_Ingredient.i_id ");
		builder.append(" AND filtered_ids_MS.r_id = T_Recipe_Ingredient.r_id ");
		builder.append(" AND T_Recipe_Ingredient.i_id = T_Ingredient.i_id ");
		builder.append(" AND filtered_ids_MS.i_id NOT IN ");
		builder.append(ingredient_i_ids);

		// builder.append("  OR ( ");
		// for (EnteredIngredient enteredIngredient : list) {
		// builder.append(" (T_Ingredient.i_Name = '");
		// builder.append(enteredIngredient.getName());
		// builder.append("' AND T_Recipe_Ingredient.ri_amount <= ");
		// builder.append(enteredIngredient.getAmount());
		// builder.append(" ) OR ");
		//
		// }
		//
		// builder.delete(builder.length() - 3, builder.length());
		// builder.append(" )) ");

		builder.append(" GROUP BY r_id)");
		builder.append(" AS missIngr ");

		return builder.toString();
	}

	private static String getIngredientIds(List<EnteredIngredient> list) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT i_id FROM T_Ingredient WHERE ");

		for (EnteredIngredient ingredient : list) {
			builder.append("i_Name = '");
			builder.append(ingredient.getName());
			builder.append("' OR ");
		}
		builder.delete(builder.length() - 3, builder.length());
		builder.append(") ");

		return builder.toString();
	}

	private static String getFilteredIds(List<String> filterOptions) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT r_id, i_id FROM T_Recipe_Ingredient WHERE r_id NOT IN "
				+ "(SELECT r_id FROM T_Recipe_Ingredient WHERE i_id IN "
				+ "(SELECT i_id FROM T_Ingredient WHERE ");

		for (String filterOption : filterOptions) {
			builder.append("i_");
			builder.append(filterOption);
			builder.append(" = 0,");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")))");

		return builder.toString();
	}

	private static String getRating() {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT r_id, AVG(r_rate) AS rating FROM T_Rating");
		builder.append(" GROUP BY r_id)");

		return builder.toString();
	}

	public static List<RecipeResult> getRecipeResults(
			List<String> filterOptions, List<String> ingredients) {
		return null;
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
