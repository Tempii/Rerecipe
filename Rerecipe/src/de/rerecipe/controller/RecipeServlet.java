package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.rerecipe.model.Ingredient;
import de.rerecipe.model.Recipe;
import de.rerecipe.persistence.RecipesDatabase;

@WebServlet("/RecipeServlet")
public class RecipeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		
		String id = request.getParameter("r_id");
		Recipe recipe = RecipesDatabase.getRecipe(new Integer(id));
		String r_name = recipe.getName();
		String r_description = recipe.getDescription();
		
		JSONObject json = new JSONObject();
		json.put("r_name", r_name);
		json.put("r_description", r_description);

		PrintWriter out = response.getWriter();
		out.print(json);

	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");

		JSONObject json = new JSONObject();
		int r_id = new Integer(request.getParameter("r_id"));
		Recipe recipe = RecipesDatabase.getRecipe(r_id);
		Map<Ingredient, Integer> ingredients = recipe.getIngredients();
		json.put("r_name", recipe.getName());
		json.put("r_description", recipe.getDescription());
		json.put("r_author", recipe.getAuthor());
		json.put("r_time", recipe.getPreparationTime());
		json.put("r_rating", recipe.getRating());
		
		JSONArray ingredJson = new JSONArray();
		JSONArray ingredAmountTypeJson = new JSONArray();
		JSONArray ingredAmountJson = new JSONArray();
		
		for (Map.Entry<Ingredient, Integer> ingredientEntry : ingredients
				.entrySet()) {
			Ingredient ingredient = ingredientEntry.getKey();
			int requiredAmount = ingredientEntry.getValue();
			ingredJson.add(ingredient.getName());
			ingredAmountTypeJson.add(ingredient.getAmountType());
			ingredAmountJson.add(requiredAmount);
		}
		json.put("r_ingredient", ingredJson);
		json.put("ri_amount", ingredAmountJson);
		json.put("i_amountType", ingredAmountTypeJson);
		PrintWriter writer = response.getWriter();
		writer.print(json);
	}

}
