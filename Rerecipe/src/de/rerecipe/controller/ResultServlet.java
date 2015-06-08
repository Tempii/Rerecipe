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
		String queryString = request.getQueryString();
		if (queryString == null)
			response.sendRedirect("index.html?001");
		else
			response.sendRedirect("result.html?" + queryString);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter writer = response.getWriter();

		String order = request.getParameter("order");
		String queryString = request.getParameter("query").replace("?", "");
		System.out.println(queryString);
		

		List<EnteredIngredient> enteredIngredients = new ArrayList<>();
		List<String> filter = new ArrayList<>();

		String screenWidthStr = request.getParameter("screenWidth");
		String ingFilterWidthStr = request.getParameter("ingFilterWidth");
		double screenWidth = Double.parseDouble(screenWidthStr);
		double ingFilterWidth = Double.parseDouble(ingFilterWidthStr);
		int timeToShow = (int) (screenWidth - ingFilterWidth) / 250;
		int i = 0;
		System.out.println(queryString);
		if (queryString != null) {
			while (queryString.contains("&")) {
				String queryPart = queryString.substring(0,
						queryString.indexOf("&"));
				String queryValue = queryPart
						.substring(queryPart.indexOf("=") + 1);
				queryString = queryString.replace(queryPart + "&", "");
				if (queryPart.startsWith("filter"))
					filter.add(queryValue);
				else
					enteredIngredients.add(new EnteredIngredient(queryPart
							.substring(0, queryPart.indexOf("=")), Integer
							.parseInt(queryValue)));
			}
			if (!queryString.contains("&")) {
				String queryValue = queryString.substring(queryString
						.indexOf("=") + 1);
				if (queryString.startsWith("filter"))
					filter.add(queryValue);
				else
					enteredIngredients.add(new EnteredIngredient(queryString
							.substring(0, queryString.indexOf("=")), Integer
							.parseInt(queryValue)));
			}
		}

		writer.println("<tr><th>Zutat</th><th>Menge</th><th>Einheit</th>");
		if (enteredIngredients.size() > 0) {
			for (EnteredIngredient enteredIngredient : enteredIngredients)
				writer.println("<tr><td>" + enteredIngredient.getName()
						+ "</td><td>" + enteredIngredient.getAmount()
						+ "</td><td>g/ml/stk</td></tr>");
		}
		writer.println("%0");

		for (String filterValue : filter)
			writer.println("<td>" + filterValue.replace("ß", "&szlig;")
					+ "<br></td>");
		writer.println("%1");

		List<RecipeResult> recipeResult = RecipesDatabase
				.getResults(new Search(enteredIngredients, filter, order));

		// Alle Rezepte anzeigen
		if (recipeResult != null) {
			for (RecipeResult item : recipeResult) {
				writer.println("<td><div id=template>");
				writer.println("<div id=name>"
						+ item.getName().replace("ß", "&szlig;") + " (&#126;"
						+ item.getPreparationTime() + "min) " + "</div>");
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
				
				if (item.getIngredients() == 1) 
					writer.println("Es fehlt ihnen 1 Zutat!");
				else if (item.getIngredients() > 1) 
					writer.println("Es fehlen ihnen " + item.getIngredients()
							+ " Zutaten!");
				else
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
