package de.rerecipe.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.rerecipe.model.Ingredient;
import de.rerecipe.model.Recipe;
import de.rerecipe.persistence.RecipesDatabase;

/**
 * Servlet implementation class Main
 */
@WebServlet("/DrawUp")
public class DrawUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DrawUpServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String queryString = request.getQueryString();

		String subString;
		String name;
		String author;
		int time;
		String description;
		Map<Ingredient, Integer> ingredients = new LinkedHashMap<Ingredient, Integer>();
		System.out.println(queryString);
		// Rezeptnamen setzen
		while (queryString.contains("+"))
			queryString = queryString.replace("+", " ");
		
		while (queryString.contains("%E4"))
			queryString = queryString.replace("%E4", "ä");
		while (queryString.contains("%C4"))
			queryString = queryString.replace("%C4", "Ä");

		while (queryString.contains("%F6"))
			queryString = queryString.replace("%F6", "ö");
		while (queryString.contains("%D6"))
			queryString = queryString.replace("%D6", "Ö");

		while (queryString.contains("%FC"))
			queryString = queryString.replace("%FC", "ü");
		while (queryString.contains("%DC"))
			queryString = queryString.replace("%DC", "Ü");

		while (queryString.contains("%DF"))
			queryString = queryString.replace("%DF", "ß");
		
		if (queryString.indexOf("=") + 1 == queryString.indexOf("&")
				|| !queryString.startsWith("name")) {
			response.sendRedirect("drawUp.html?001");
		} else {
			subString = queryString.substring(queryString.indexOf("=") + 1,
					queryString.indexOf("&"));
			name = subString;
			queryString = queryString.substring(queryString.indexOf("&") + 1);
			System.out.println(queryString);
			// Autor setzen
			if (queryString.indexOf("=") + 1 == queryString.indexOf("&")
					|| !queryString.startsWith("author")) {
				response.sendRedirect("drawUp.html?010");
			} else {
				subString = queryString.substring(queryString.indexOf("=") + 1,
						queryString.indexOf("&"));
				author = subString;
				queryString = queryString
						.substring(queryString.indexOf("&") + 1);
				System.out.println(queryString);
				// Benötigte Zeit setzen

				if (queryString.indexOf("=") + 1 == queryString.indexOf("&")
						|| !queryString.startsWith("time")) {
					response.sendRedirect("drawUp.html?011");
				} else {
					subString = queryString.substring(
							queryString.indexOf("=") + 1,
							queryString.indexOf("&"));
					try {
						time = Integer.parseInt(subString);
					} catch (NumberFormatException e) {
						time = -1;
					}
					queryString = queryString.substring(queryString
							.indexOf("&") + 1);
					System.out.println(queryString);
					// Beschreibung setzen
					if (time < 0) {
						response.sendRedirect("drawUp.html?110");
					} else if (queryString.indexOf("=") + 1 == queryString
							.indexOf("&")
							|| !queryString.startsWith("description")) {
						response.sendRedirect("drawUp.html?100");
					} else {
						subString = queryString.substring(
								queryString.indexOf("=") + 1,
								queryString.indexOf("&"));
						description = subString;

						queryString = queryString.substring(queryString
								.indexOf("&") + 1);
						System.out.println(queryString);
						// TODO Bild hochladen
						queryString = queryString.replace("Datei=", "");
						if (queryString.startsWith("&"))
							queryString = queryString.substring(1);
						// queryString = queryString.substring(queryString
						// .indexOf("&") + 1);
						// System.out.println(queryString);
						// Zutaten hinzufuegen
						while (queryString.contains("&")) {
							if (queryString.indexOf("=") + 1 == queryString
									.indexOf("&"))
								response.sendRedirect("drawUp.html?101");

							subString = queryString.substring(0,
									queryString.indexOf("&"));
							queryString = queryString.replace(subString + "&",
									"");

							int value = Integer.parseInt(subString
									.substring(subString.indexOf("=") + 1));
							String type = subString.substring(0,
									subString.indexOf("="));
							Ingredient ingredient = RecipesDatabase
									.getIngredient(type);

							ingredients.put(ingredient, value);
						}
						System.out.println("Fehler" + queryString);
						int value = Integer.parseInt(queryString
								.substring(queryString.indexOf("=") + 1));
						String type = queryString.substring(0,
								queryString.indexOf("="));
						Ingredient ingredient = RecipesDatabase
								.getIngredient(type);

						ingredients.put(ingredient, value);
						System.out.println(queryString);

						if (time >= 1) {
							Recipe recipe = new Recipe(name, time, ingredients,
									author, description);

							int id = RecipesDatabase.addRecipe(recipe);

							response.sendRedirect("recipe.html?r_id=" + id);
						}

					}
				}
			}
		}
	}
}
