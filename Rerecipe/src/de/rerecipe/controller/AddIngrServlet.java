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
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;
import de.rerecipe.persistence.RecipesDatabase;
import de.rerecipe.persistence.Replacer;

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
		String queryString = Replacer.replaceAll(request.getQueryString());
		queryString = queryString.replace("?", "");
		queryString = queryString.replace("ingr1=", "");
		String name = queryString.substring(0, queryString.indexOf("&"));
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
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		String name = Replacer.replaceAll(request.getParameter("name"));
		String measure = Replacer.replaceAll(request.getParameter("measure"));
		boolean isVegetarian = Boolean.parseBoolean(request.getParameter("vegetarian"));
		boolean isVegan = Boolean.parseBoolean(request.getParameter("vegan"));
		boolean isNutFree = Boolean.parseBoolean(request.getParameter("nutFree"));
		boolean isGlutenFree = Boolean.parseBoolean(request.getParameter("glutenFree"));
		RecipesDatabase.addIngredient(new Ingredient(0, name, measure, isVegetarian, isVegan, isNutFree, isGlutenFree));
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("measure", measure);
		writer.print(data);
		
	}
}
