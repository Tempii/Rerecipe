package de.rerecipe.controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import de.rerecipe.model.Ingredient;
import de.rerecipe.model.Recipe;
import de.rerecipe.persistence.RecipesDatabase;

/**
 * Servlet implementation class Main
 */
@WebServlet("/DrawUp")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10)
// 10MB
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
						queryString = queryString.replace("file=", "");
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

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + "img";
		System.out.println(savePath);
		String image = "default.png";
		String name = null;
		String author = null;
		int time = 0;
		String description = null;
		Map<Ingredient, Integer> ingredients = new LinkedHashMap<Ingredient, Integer>();

		InputStream is;

		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		boolean ingred = false;
		for (Part part : request.getParts()) {
			String dataName = part.getName();

			if (!ingred) {

				switch (dataName) {
				case "file":
					// TODO Exception
					image = extractFileName(part);
					part.write(savePath + File.separator + image);
					break;
				case "name":
					is = part.getInputStream();
					name = convertStreamToString(is);
					System.out.println(dataName + ": " + name);
					break;
				case "author":
					is = part.getInputStream();
					author = convertStreamToString(is);
					System.out.println(dataName + ": " + author);
					break;
				case "time":
					is = part.getInputStream();
					time = Integer.parseInt(convertStreamToString(is));
					System.out.println(dataName + ": " + time);
					break;
				case "description":
					is = part.getInputStream();
					description = convertStreamToString(is);
					System.out.println(dataName + ": " + description);
					break;
				default:
					is = part.getInputStream();
					int amount = Integer.parseInt(convertStreamToString(is));
					System.out.println(dataName + ": " + amount);
					Ingredient ingredient = RecipesDatabase.getIngredient(dataName);
					ingredients.put(ingredient, amount);
					ingred=true;
					break;

				}
			}else{
				//TODO andere Einheiten erlauben?
				ingred = false;
			}
		}
			Recipe recipe = new Recipe(name, time, ingredients,
					author, description);

			int id = RecipesDatabase.addRecipe(recipe);

		    try {
				   
			    String sourceFile =savePath + File.separator + image;
			    File file = new File(sourceFile);
			    BufferedImage bi = new BufferedImage(200, 180, BufferedImage.TYPE_INT_ARGB);
			    BufferedImage im = ImageIO.read(file);
			    
			    Graphics g = bi.getGraphics();
			    g.drawImage(im, 0, 0, bi.getWidth(), bi.getHeight(), 0, 0, im.getWidth(), im.getHeight(), null);
			    
			    File outputfile = new File(savePath + File.separator + name.replace(' ', '_') + '_' + id + ".png");
			    ImageIO.write(bi, "png", outputfile);
			    
			    file.delete();
			   
			} catch(Exception e) {
			    e.printStackTrace();
			}
		
		
			response.sendRedirect("recipe.html?r_id=" + id);
		

	}

	private String extractFileName(Part part) throws ServletException,
			IOException {
		// TODO bereits vorhandener fileName
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "default.png";
	}

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
