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

import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;
import de.rerecipe.persistence.RecipesDatabase;
import de.rerecipe.persistence.Replacer;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class ResultServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResultServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		String queryString = request.getQueryString();
		if (queryString == null || queryString == ""
				|| queryString.startsWith("filter"))
			response.sendRedirect("index.html?001");
		else
			response.sendRedirect("result.html?" + queryString);
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
		// Die Eingaben holen
		String queryString = Replacer.replaceAll(request.getParameter("query")
				.replace("?", ""));
		String order = request.getParameter("order");

		// Ergebnisse
		JSONArray results = new JSONArray();
		// Alles in einem
		JSONObject data = new JSONObject();

		// Eingegebene Zutaten
		JSONArray enteredIngArray = new JSONArray();
		List<EnteredIngredient> enteredIngredients = new ArrayList<>();
		// Filter
		JSONArray enteredFilterArray = new JSONArray();
		List<String> filter = new ArrayList<>();

		while (queryString.contains("&")) {
			String queryPart = queryString.substring(0,
					queryString.indexOf("&"));
			queryString = queryString.replace(queryPart + "&", "");
			String queryValue = queryPart.substring(queryPart.indexOf("=") + 1);
			String queryType = queryPart.substring(0, queryPart.indexOf("="));
			if (queryType.startsWith("filter")) {
				filter.add(queryValue);
				enteredFilterArray.add(queryValue.replace("�", "&szlig;"));
			} else {
				String queryMeasure = "";
				if (queryString.contains("&"))
					queryMeasure = queryString.substring(0,
							queryString.indexOf("&"));
				else
					queryMeasure = queryString;
				queryString = queryString.replace(queryMeasure, "");
				if (queryString.startsWith("&"))
					queryString = queryString.substring(1);
				String measure = queryMeasure.substring(queryMeasure
						.indexOf("=") + 1);
				System.out.println(queryType);
				try {
					enteredIngredients.add(new EnteredIngredient(queryType,
							Double.parseDouble(queryValue.replace(",", "."))));
					JSONObject entered = new JSONObject();
					entered.put("name", queryType);
					entered.put("amount", Double.parseDouble(queryValue.replace(",", ".")));
					entered.put("measure", measure);
					enteredIngArray.add(entered);
				} catch (NumberFormatException e) {
					JSONObject entered = new JSONObject();
					entered.put("name", queryType);
					entered.put("amount", 100);
					entered.put("measure", measure);
					enteredIngArray.add(entered);
				}
			}
		}
		if (queryString.contains("=")) {
			String queryValue = queryString
					.substring(queryString.indexOf("=") + 1);
			String queryType = queryString.substring(0,
					queryString.indexOf("="));
			if (queryType.startsWith("filter")) {
				filter.add(queryValue);
				enteredFilterArray.add(queryValue.replace("�", "&szlig;"));
			}
		}
		int start = new Integer(request.getParameter("start"));
		double amount = new Double(request.getParameter("amount").replace(",", "."));

		List<RecipeResult> recipeResult = RecipesDatabase
				.getResults(new Search(enteredIngredients, filter, order,
						start, amount));

		for (RecipeResult result : recipeResult) {
			JSONObject JSONResult = new JSONObject();
			JSONResult.put("id", result.getId());
			JSONResult.put("name", result.getName());
			JSONResult.put("pic", result.getPicture());
			JSONResult.put("time", result.getPreparationTime());
			JSONResult.put("rating", result.getRating());
			JSONResult.put("ingredients",
					result.getMissingIngredients(enteredIngredients));
			results.add(JSONResult);
		}
		data.put("ings", enteredIngArray);
		data.put("filter", enteredFilterArray);
		data.put("results", results);
		writer.print(data);
	}
}
