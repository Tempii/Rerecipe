package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.rerecipe.model.Ingredient;
import de.rerecipe.model.NewIngredient;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;
import de.rerecipe.persistence.RecipesDatabase;

/**
 * Servlet implementation class Main
 */
public class AddIngrServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddIngrServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String queryString = request.getQueryString();
		queryString = queryString.replace("?", "");
		queryString = queryString.replace("ingr1=", "");
		String name = queryString.substring(0, queryString.indexOf("&"));
		while (name.contains("+"))
			name = name.replace("+", " ");
		String measure = "";
		String cut = "";
		boolean isVegetarian = false;
		boolean isVegan = false;
		boolean isNutFree = false;
		boolean isGlutenFree = false;
		queryString = queryString.replace(name + "&", "");
		if (queryString.contains("&"))
			cut = "&";
		if (queryString.startsWith("measureUnit1")) {
			queryString = queryString.replace("measureUnit1=", "");
			if (cut == "&")
				measure = queryString.substring(0, queryString.indexOf("&"));
			else
				measure = queryString;
			queryString.replace("measure" + cut, "");
		}
		if (queryString.contains("Vegetarian"))
			isVegetarian = true;
		if (queryString.contains("Vegan"))
			isVegan = true;
		if (queryString.contains("NutFree"))
			isNutFree = true;
		if (queryString.contains("GlutenFree"))
			isGlutenFree = true;
		RecipesDatabase.addIngredient(new Ingredient(0, name, measure, isVegetarian, isVegan, isNutFree, isGlutenFree));
		response.sendRedirect("addIngr.html?001");

	}

}
