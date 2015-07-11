package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.rerecipe.persistence.RecipesDatabase;

public class AutocompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		String[] recipes = RecipesDatabase.getIngredientNames(request
				.getParameter("term"));
		response.setContentType("application/json");

		JSONArray ingredients = new JSONArray();
		for (int i = 0; i<recipes.length;i++) {
			JSONObject ingredient = new JSONObject();
			ingredient.put("name", recipes[i]);
			ingredients.add(ingredient);
		}
		JSONObject data = new JSONObject();
		data.put("data", ingredients);
		
		PrintWriter out = response.getWriter();
		out.print(data);

	}

	public AutocompleteServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] recipes = RecipesDatabase.getIngredientNames(request
				.getParameter("term"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(recipes));

	}

}