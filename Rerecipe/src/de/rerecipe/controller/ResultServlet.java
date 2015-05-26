package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.rerecipe.model.Ingredient;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;
import de.rerecipe.persistence.RecipesDatabase;

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
		String[] filter = request.getParameterValues("filter[]");
		String search1 = "ing:";
		String search2 = "filter:";
		String queryString = request.getQueryString();
		if (queryString != null) {
			if (queryString.startsWith("filter"))
				queryString = "";
			else if (queryString.contains("&filter"))
				queryString = queryString.substring(0,
						queryString.indexOf("&filter"));
		} else {
			queryString = "";
		}
		search1 += queryString + "_";

		if (filter != null)
			for (String option : filter)
				search2 += option + "&";
		if (search2.lastIndexOf("&") > -1)
			search2 = search2.substring(0, search2.lastIndexOf("&"));
		if (search1.equals("ing:_") && search2.equals("filter:"))
			response.sendRedirect("index.html?001");
		else
			response.sendRedirect("result.html?" + search1 + search2);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String FilterHtml = "";

		String ingredients = request.getParameter("ingredients").replace(
				"ing:", "");
		ingredients = ingredients.replace("_", "");
		String screenWidthStr = request.getParameter("screenWidth");
		String ingFilterWidthStr = request.getParameter("ingFilterWidth");
		double screenWidth = Double.parseDouble(screenWidthStr);
		double ingFilterWidth = Double.parseDouble(ingFilterWidthStr);
		int timeToShow = (int) (screenWidth - ingFilterWidth) / 250;
		String filterString = request.getParameter("filter");
		String order = request.getParameter("order");
		List<EnteredIngredient> enteredIngredients = new ArrayList<>();
		List<String> ingNames = new ArrayList<>();
		List<String> ingAmount = new ArrayList<>();
		List<String> filter = new ArrayList<>();

		PrintWriter writer = response.getWriter();

		int i = 0;
		while (filterString.contains("&")) {
			String option = filterString
					.substring(0, filterString.indexOf("&"));
			filterString = filterString.replace(option + "&", "");
			filter.add(option);
			FilterHtml += option.replace("ß", "&szlig;") + "<br>";
		}
		if (filterString.length() > 0) {
			FilterHtml += filterString.replace("ß", "&szlig;")  + "<br>";
			filter.add(filterString);
		}

		while (ingredients.contains("&")) {
			String option = ingredients.substring(0, ingredients.indexOf("&"));
			ingredients = ingredients.replace(option + "&", "");
			String ingr = option.substring(0, option.indexOf("="));
			String amount = option.substring(option.indexOf("=") + 1,
					option.length());
			ingNames.add(ingr);
			ingAmount.add(amount);
			enteredIngredients.add(new EnteredIngredient(ingr, 0));
		}
		if (ingredients.length() > 0) {
			String ingr = ingredients.substring(0, ingredients.indexOf("="));
			String amount = ingredients.substring(ingredients.indexOf("=") + 1,
					ingredients.length());
			ingNames.add(ingr);
			ingAmount.add(amount);
			enteredIngredients.add(new EnteredIngredient(ingr, 0));
		}


		List<RecipeResult> recipeResult = RecipesDatabase
				.getResults(new Search(enteredIngredients, filter, order));

		writer.println("<tr><th>Zutat</th><th>Menge</th><th>Einheit</th>");
		if (ingNames.size() > 0) {
			for (int j = 0; j < ingNames.size(); j++)
				writer.println("<tr><td>" + ingNames.get(j) + "</td><td>"
						+ ingAmount.get(j) + "</td><td>g/ml/stk</td></tr>");
		}
		writer.println("%0");

		// Den Filter aufbauen
		if (FilterHtml != "")
			writer.println("<td>" + FilterHtml + "</td>");
		writer.println("%1");
		// Alle Rezepte anzeigen
		if (recipeResult != null) {
			for (RecipeResult item : recipeResult) {
				writer.println("<td><div id=template>");
				writer.println("<div id=name>" + item.getName().replace("ß", "&szlig;") + "</div>");
				writer.println("<a href=\"recipe.html?r_id="
						+ item.getId()
						+ "\" id=\""
						+ item.getId()
						+ "\" onclick=\"document.location=recipe.html?r_id=this.id+'';return false;\" ><img alt="
						+ item.getId() + " src=" + item.getPicture()
						+ " id=\"recipeImg\"></a>");
				writer.println("<div id=ratingBox align=left><div style=\"background-color:#f7931e; height:20px;  width:"
						+ (item.getRating() / 5)
						* 100
						+ "px;\"><img src=\"img/ratingboxsmall.png\"></div></div>");
				/*
				 * if (item.getIngredients().size() > 0) {
				 * writer.println("<div id=IngText> Es fehlt ihnen: "); for
				 * (Ingredient ing : item.getIngredients()) // Hier eventuell
				 * einen Counter um maximale Einträge zu // erzielen
				 * 
				 * if (item.getIngredients().indexOf(ing) == item
				 * .getIngredients().size() - 1) writer.println(ing.getName() +
				 * "."); else writer.println(ing.getName() + ", ");
				 */
				if (item.getIngredients() > 0) {
					writer.println("Es fehlen ihnen " + item.getIngredients()
							+ "Zutaten!");
				} else
					writer.println("Sie haben alle Zutaten.");

				writer.println("<button id=teilenButton>teilen</button>");

				if (i % timeToShow == (timeToShow - 1))
					writer.println("<tr>");

				writer.println("</div>");
				writer.print("</td>");
				i++;
			}
		}
		writer.println("%2");
	}

}
