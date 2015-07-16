package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;

import de.rerecipe.model.Ingredient;
import de.rerecipe.persistence.RecipesDatabase;
import de.rerecipe.persistence.Replacer;

public class AutocompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		List<Ingredient> ingredients = RecipesDatabase
				.getIngredientNames(Replacer.replaceAll(request.getParameter("term")));
		response.setContentType("application/json");

		JSONArray ingredientsJson = new JSONArray();
		for (int i = 0; i < ingredients.size(); i++) {
			JSONObject ingredientJson = new JSONObject();
			Ingredient ingredient = ingredients.get(i);
			ingredientJson.put("name", ingredient.getName());
			ingredientJson.put("amountType", ingredient.getAmountType());
			ingredientsJson.add(ingredientJson);
		}
		JSONObject data = new JSONObject();
		data.put("data", ingredientsJson);

		PrintWriter out = response.getWriter();
		out.print(data);
	}

	public AutocompleteServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Ingredient> recipes = RecipesDatabase.getIngredientNames(request
				.getParameter("term"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(recipes));

	}

}