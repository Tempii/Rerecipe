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
		if (search1.equals("ing:_")  && search2.equals("filter:"))
			response.sendRedirect("index.html?001");
		else
			response.sendRedirect("result.html?"+search1+search2);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String FilterHtml = "";
		List<RecipeResult> recipeResult = new ArrayList<RecipeResult>();

		String ingredients = request.getParameter("ingredients");
		String filterString = request.getParameter("filter");

		List<String> ingNames = new ArrayList<String>();
		List<String> ingAmount = new ArrayList<String>();
		List<String> filter = new ArrayList<String>();

		PrintWriter writer = response.getWriter();

		int i = 0;
		while (filterString.contains("&")) {
			String option = filterString
					.substring(0, filterString.indexOf("&"));
			filterString = filterString.replace(option + "&", "");
			filter.add(option);
			FilterHtml += option + "<br>";
		}
		if (filterString.length() > 0) {
			FilterHtml += filterString + "<br>";
			filter.add(filterString);
		}

		while (ingredients.contains("&")) {
			String option = ingredients.substring(0, ingredients.indexOf("&"));
			ingredients = ingredients.replace(option + "&", "");
			ingNames.add(option.substring(0, option.indexOf("=")));
			ingAmount.add(option.substring(option.indexOf("=") + 1,
					option.length()));
		}
		if (ingredients.length() > 0) {
			ingNames.add(ingredients.substring(0, ingredients.indexOf("=")));
			ingAmount.add(ingredients.substring(ingredients.indexOf("=") + 1,
					ingredients.length()));
		}

		// Hier sollte man die Liste aus der DB holen
		recipeResult = RecipesDatabase.getRecipeResults(filter, ingNames);

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
				writer.println("<div id=name>" + item.getName() + "</div>");
				writer.println("<a href=\"recipe.html?r_id="
						+ item.getId()
						+ "\" id=\""
						+ item.getId()
						+ "\" onclick=\"document.location=recipe.html?r_id=this.id+'';return false;\" ><img alt="
						+ item.getId() + " src=" + item.getPicture() + "></a>");
				writer.println("<div id=ratingBox align=left><div style=\"background-color:#f7931e; height:20px;  width:"
						+ (item.getRating() / 5)
						* 100
						+ "px;\"><img src=\"img/ratingboxsmall.png\"></div></div>");
				if (item.getIngredients().size() > 0) {
					writer.println("<div id=IngText> Es fehlt ihnen: ");
					for (Ingredient ing : item.getIngredients())
						// Hier eventuell einen Counter um maximale Einträge zu
						// erzielen

						if (item.getIngredients().indexOf(ing) == item
								.getIngredients().size() - 1)
							writer.println(ing.getName() + ".");
						else
							writer.println(ing.getName() + ", ");
				} else
					writer.println("Sie haben alle Zutaten.");

				writer.println("<button id=teilenButton>teilen</button>");

				if (i % 5 == 4)
					writer.println("<tr>");

				writer.println("</div>");
				writer.print("</td>");
				i++;
			}
		}
		writer.println("%2");
		writer.close();
	}

}
