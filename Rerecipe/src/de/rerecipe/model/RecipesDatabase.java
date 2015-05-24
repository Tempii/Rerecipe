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

	

}
