package de.rerecipe.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.rerecipe.model.Comment;
import de.rerecipe.model.Ingredient;
import de.rerecipe.model.Recipe;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;

public class RecipesDatabase {

	public static List<RecipeResult> getResults(Search search) {
		String ingredientIds = getIngredientIds(search.getIngredients());
		String notAllowedIngredients = getNotAllowedIngredientIds(search
				.getFilterOptions());
		String filtered_r_ids = filteredRecipes(search.getFilterOptions(),
				ingredientIds, notAllowedIngredients);
		String ingredientCount = getIngredientCount(filtered_r_ids);
		String availableIngredientCount = getAvailableIngredientCount(
				filtered_r_ids, ingredientIds, search.getIngredients());
		String rating = getRating();
		String orderedRecipes = getOrderedRecipes(ingredientCount,
				availableIngredientCount, rating, search.getOrderBy(),
				search.getStart(), search.getAmount());

		StringBuilder builder = new StringBuilder();

		builder.append("SELECT orderedRecipes.r_id, orderedRecipes.r_name, ");
		builder.append(" orderedRecipes.r_time, orderedRecipes.rating, orderedRecipes.missingIngredients, ");
		builder.append(" T_Recipe_Ingredient.i_id, T_Recipe_Ingredient.ri_amount, i_Name, i_amountType, ");
		builder.append(" i_Vegetarian, i_Vegan, i_NutFree, i_GlutenFree ");
		builder.append(" FROM T_Recipe_Ingredient, T_Ingredient, ");
		builder.append(orderedRecipes);
		builder.append(" AS orderedRecipes ");
		builder.append(" WHERE orderedRecipes.r_id = T_Recipe_Ingredient.r_id ");
		builder.append(" AND T_Ingredient.i_id = T_Recipe_Ingredient.i_id ");
		builder.append(" ORDER BY missingIngredients, ");
		builder.append(search.getOrderBy());
		builder.append(",r_id, i_id ");

		String select = builder.toString();

		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			try (ResultSet result = stmt.executeQuery()) {

				List<RecipeResult> searchResults = new ArrayList<RecipeResult>();

				int id = -1;
				Map<Ingredient, Integer> ingredients = null;

				while (result.next()) {
					if (result.getInt("r_id") != id) {
						id = result.getInt("r_id");
						ingredients = new LinkedHashMap<Ingredient, Integer>();
						String name = result.getString("r_name");
						int preparationTime = result.getInt("r_time");
						double recipeRating = result.getDouble("rating");
						int missingIngredients = result
								.getInt("missingIngredients");
						searchResults.add(new RecipeResult(id, name,
								preparationTime, recipeRating,
								missingIngredients, ingredients));
					}

					Ingredient ingredient = new Ingredient(
							result.getInt("i_id"), result.getString("i_Name"),
							result.getString("i_amountType"),
							result.getBoolean("i_Vegetarian"),
							result.getBoolean("i_Vegan"),
							result.getBoolean("i_NutFree"),
							result.getBoolean("i_GlutenFree"));
					ingredients.put(ingredient, result.getInt("ri_amount"));
				}
				return searchResults;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static String getOrderedRecipes(String ingredientCount,
			String availableIngredientCount, String rating, String orderBy,
			int start, int amount) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT ingredientCountO.r_id, T_Recipe.r_name, T_Recipe.r_time, rating, (ingredientCount - availableIngredientCount) as missingIngredients "
				+ "FROM T_Recipe,");
		builder.append(ingredientCount);
		builder.append(" AS ingredientCountO, ");
		builder.append(availableIngredientCount);
		builder.append(" AS availableIngredientCountO, ");
		builder.append(rating);
		builder.append(" AS ratingO ");
		builder.append(" WHERE T_Recipe.r_id = availableIngredientCountO.r_id ");
		builder.append(" AND  T_Recipe.r_id = ingredientCountO.r_id");
		builder.append(" AND  T_Recipe.r_id = ratingO.r_id ORDER BY missingIngredients, ");
		builder.append(orderBy);
		builder.append(" LIMIT ");
		builder.append(start - 1);
		builder.append(", ");
		builder.append(amount);
		builder.append(" )");

		return builder.toString();
	}

	private static String getIngredientCount(String filtered_r_ids) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT T_Recipe_Ingredient.r_id, count(T_Recipe_Ingredient.r_id) as ingredientCount "
				+ "FROM T_Ingredient, T_Recipe_Ingredient, ");
		builder.append(filtered_r_ids);
		builder.append(" AS filtered_r_idsR ");
		builder.append(" WHERE ");
		builder.append(" T_Recipe_Ingredient.i_id = T_Ingredient.i_id ");
		builder.append(" AND T_Recipe_Ingredient.r_id = filtered_r_idsR.r_id ");

		builder.append("GROUP BY r_id )  ");

		return builder.toString();

	}

	private static String getAvailableIngredientCount(String filtered_r_ids,
			String ingredientIds, List<EnteredIngredient> ingredients) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT filtered_r_idsR.r_id, count(filtered_r_idsR.r_id) AS availableIngredientCount FROM T_Ingredient, T_Recipe_Ingredient, ");
		builder.append(filtered_r_ids);
		builder.append(" AS filtered_r_idsR ");
		builder.append(" WHERE ");
		builder.append(" T_Recipe_Ingredient.i_id = T_Ingredient.i_id ");
		builder.append(" AND T_Recipe_Ingredient.r_id = filtered_r_idsR.r_id ");
		builder.append(" AND (T_Ingredient.i_id IN");
		builder.append(ingredientIds);
		builder.append(" AND(");
		for (EnteredIngredient ingredient : ingredients) {
			builder.append(" (T_Ingredient.i_Name = '");
			builder.append(ingredient.getName());
			builder.append("' AND T_Recipe_Ingredient.ri_amount <= ");
			builder.append(ingredient.getAmount());
			builder.append(") OR ");
		}
		builder.delete(builder.length() - 3, builder.length());
		builder.append("))GROUP BY r_id) ");

		return builder.toString();

	}

	private static String filteredRecipes(List<String> filterOptions,
			String ingredientIds, String notAllowedIngredients) {

		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT DISTINCT r_id FROM T_Recipe_Ingredient ");
		builder.append(" WHERE T_Recipe_Ingredient.i_id IN ");
		builder.append(ingredientIds);

		if (!filterOptions.isEmpty()) {
			builder.append("AND r_id NOT IN (SELECT r_id FROM T_Recipe_Ingredient WHERE i_id IN ");
			builder.append(notAllowedIngredients);
			builder.append(")");
		}

		builder.append(")");
		return builder.toString();
	}

	private static String getNotAllowedIngredientIds(List<String> filterOptions) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT i_id FROM T_Ingredient WHERE ");

		for (String filterOption : filterOptions) {
			builder.append("i_");
			builder.append(filterOption);
			builder.append(" = 0 AND ");
		}
		builder.delete(builder.length() - 4, builder.length());
		builder.append(")");

		return builder.toString();
	}

	private static String getIngredientIds(List<EnteredIngredient> ingredients) {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT i_id FROM T_Ingredient WHERE ");

		for (EnteredIngredient ingredient : ingredients) {
			builder.append("i_Name = '");
			builder.append(ingredient.getName());
			builder.append("' OR ");
		}
		builder.delete(builder.length() - 3, builder.length());
		builder.append(")");

		return builder.toString();
	}

	private static String getRating() {
		StringBuilder builder = new StringBuilder();

		builder.append("(SELECT r_id, AVG(r_rate) AS rating FROM T_Rating");
		builder.append(" GROUP BY r_id)");

		return builder.toString();
	}

	public static Recipe getRecipe(int r_id) {
		String ratingSql = getRating();

		StringBuilder builder = new StringBuilder();

		builder.append(" SELECT r_name, r_author, r_time, r_description, rating , ");
		builder.append(" T_Ingredient.i_id, ri_amount, i_Name, i_amountType, ");
		builder.append(" i_Vegetarian, i_Vegan, i_NutFree, i_GlutenFree ");
		builder.append(" FROM T_Recipe, T_Ingredient, T_Recipe_Ingredient, ");
		builder.append(ratingSql);
		builder.append(" AS rating ");
		builder.append(" WHERE T_Recipe.r_id = ?");
		builder.append(" AND T_Recipe.r_id = rating.r_id ");
		builder.append(" AND T_Recipe.r_id = T_Recipe_Ingredient.r_id ");
		builder.append(" AND T_Ingredient.i_id = T_Recipe_Ingredient.i_id ");
		
		String select = builder.toString();
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			stmt.setInt(1, r_id);
			try (ResultSet result = stmt.executeQuery()) {
				if (!result.next())
					return null;

				String name = result.getString("r_name");
				int preparationTime = result.getInt("r_time");
				double rating = result.getDouble("rating");

				String author = result.getString("r_author");
				String description = result.getString("r_description");
				Map<Ingredient, Integer> ingredients = new HashMap<Ingredient, Integer>();

				do {
					int i_id = result.getInt("i_id");
					String i_name = result.getString("i_Name");
					String amountType = result.getString("i_amountType");
					boolean isVegetarian = result.getBoolean("i_Vegetarian");
					boolean isVegan = result.getBoolean("i_Vegan");
					boolean isNutFree = result.getBoolean("i_NutFree");
					boolean isGlutenFree = result.getBoolean("i_GlutenFree");
					int amount = result.getInt("ri_amount");
					Ingredient ingredient = new Ingredient(i_id, i_name, amountType, isVegetarian, isVegan, isNutFree, isGlutenFree);
					ingredients.put(ingredient, amount);
				} while (result.next());
				
				return new Recipe(r_id, name, preparationTime, rating, 0,
						ingredients, author, description);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Comment> getComments(int r_id, int start, int amount) {
		String select = "SELECT c_author, r_rate, r_comment "
				+ " FROM T_Rating WHERE r_id = ? ORDER BY c_time DESC "
				+ " LIMIT " + (start - 1) + ", " + amount;
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			stmt.setInt(1, r_id);
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

	public static void addRecipe(Recipe recipe) {// TODO
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
