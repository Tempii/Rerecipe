package de.rerecipe.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class RecipesDatabase {

	public static String[] findIngredients() {
		String select = "SELECT i_Name FROM T_Ingredient";

		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			List <String>rowValues = new ArrayList<String>();
			
			try (ResultSet result = stmt.executeQuery()) {
				while (result.next()) {
					rowValues.add(result.getString("i_Name"));
				}
				String[] recipes = (String[]) rowValues.toArray(new String[rowValues.size()]);
				return recipes;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	public static String getRecipeName(String r_id) {
		String select = "SELECT r_name FROM T_Recipe WHERE r_id='"+r_id+"'";
		System.out.println(select);
		String resultQuery ="";
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();

			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					resultQuery = result.getString("r_name");
				}
				return resultQuery;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String();
		}
	}
	
	public static String getRecipeDescription(String r_id) {
		String select = "SELECT r_description FROM T_Recipe WHERE r_id='"+r_id+"'";
		System.out.println(select);
		String resultQuery ="";
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();

			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					resultQuery = result.getString("r_description");
				}
				return resultQuery;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String();
		}
	}
	
	public static String getRecipeAuthor(String r_id) {
		String select = "SELECT r_author FROM T_Recipe WHERE r_id='"+r_id+"'";
		System.out.println(select);
		String resultQuery ="";
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();

			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					resultQuery = result.getString("r_author");
				}
				return resultQuery;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String();
		}
	}
	
	public static String getRecipePrepTime(String r_id) {
		String select = "SELECT r_time FROM T_Recipe WHERE r_id='"+r_id+"'";
		System.out.println(select);
		String resultQuery ="";
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();

			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					resultQuery = result.getString("r_time");
				}
				return resultQuery;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String();
		}
	}
	
	public static String[] getRecipeIngred(String r_id) {
		String select = "SELECT T_Ingredient.i_Name FROM T_Ingredient, T_Recipe_Ingredient WHERE T_Ingredient.i_id=T_Recipe_Ingredient.i_id AND T_Recipe_Ingredient.r_id='"+r_id+"' ORDER BY T_Ingredient.i_Name";
		System.out.println(select);
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			List <String>rowValues = new ArrayList<String>();
			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					rowValues.add(result.getString("i_Name"));
				}
				String[] recipes = (String[]) rowValues.toArray(new String[rowValues.size()]);
				return recipes;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	public static String[] getRecipeIngredAmnt(String r_id) {
		String select = "SELECT T_Recipe_Ingredient.ri_amount FROM T_Ingredient, T_Recipe_Ingredient WHERE T_Ingredient.i_id=T_Recipe_Ingredient.i_id AND T_Recipe_Ingredient.r_id='"+r_id+"' ORDER BY T_Ingredient.i_Name";
		System.out.println(select);
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			List <String>rowValues = new ArrayList<String>();
			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					rowValues.add(result.getString("ri_amount"));
				}
				String[] recipes = (String[]) rowValues.toArray(new String[rowValues.size()]);
				return recipes;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	public static String[] getRecipeIngredAmntType(String r_id) {
		String select = "SELECT T_Ingredient.i_amountType FROM T_Ingredient, T_Recipe_Ingredient WHERE T_Ingredient.i_id=T_Recipe_Ingredient.i_id AND T_Recipe_Ingredient.r_id='"+r_id+"' ORDER BY T_Ingredient.i_Name";
		System.out.println(select);
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();
			List <String>rowValues = new ArrayList<String>();
			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					rowValues.add(result.getString("i_amountType"));
				}
				String[] recipes = (String[]) rowValues.toArray(new String[rowValues.size()]);
				return recipes;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	public static float getRecipeRating(String r_id) {
		String select = "SELECT avg(r_rate) FROM T_Rating WHERE r_id='"+r_id+"' GROUP BY r_id";
		System.out.println(select);
		float resultQuery=0;
		try (DatabaseConnection connection = new DatabaseConnection(select)) {
			PreparedStatement stmt = connection.getStatement();

			try (ResultSet result = stmt.executeQuery()) {
				while(result.next()) {
					resultQuery = result.getFloat("avg(r_rate)");
				}
				return resultQuery;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return resultQuery;
		}
	}

	

}
