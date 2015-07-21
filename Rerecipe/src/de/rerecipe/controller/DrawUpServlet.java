package de.rerecipe.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
@MultipartConfig
public class DrawUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DrawUpServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// String uploadPath = "/img/";
		// String savePath = getServletContext().getRealPath(uploadPath);
		String savePath = "/user/proj/it15/it15g05/jetty/webapps/img/";
		System.out.println(savePath);
		String image = "";
		String name = null;
		String author = null;
		int time = 0;
		String description = null;
		Map<Ingredient, Integer> ingredients = new LinkedHashMap<Ingredient, Integer>();

		BufferedImage bi = null;
		File file = null;
		boolean noIngredients = true;

		InputStream is;
		OutputStream out = null;
		InputStream filecontent = null;
		final Part filePart = request.getPart("file");

		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		boolean ingred = false;
		boolean failure = false;
		boolean noImage = false;
		String recipeError = "";
		String authorError = "";
		String timeError = "";
		String descriptionError = "";
		String ingredientsError = "";
		String pictureError = "";
		for (Part part : request.getParts()) {
			String dataName = part.getName();

			if (!ingred) {

				switch (dataName) {
				case "file":

					try {
						image = extractimage(part);
						System.out.println(image);
						if (image.equals("")) {
							noImage = true;
							break;
						}else{
							image="temp.png";
						}
						
						out = new FileOutputStream(new File(savePath
								+ File.separator + image));
						filecontent = filePart.getInputStream();

						int read = 0;
						final byte[] bytes = new byte[1024];

						while ((read = filecontent.read(bytes)) != -1) {
							out.write(bytes, 0, read);
						}
					} catch (FileNotFoundException fne) {
						noImage = true;
					} finally {
						if (out != null) {
							out.close();
						}
						if (filecontent != null) {
							filecontent.close();
						}
					}

					break;
				case "name":
					is = part.getInputStream();
					name = convertStreamToString(is);
					if (name.equals("")) {
						recipeError = "r1/";
						failure = true;
					} else {
						recipeError = name + "/";
					}
					break;
				case "author":
					is = part.getInputStream();
					author = convertStreamToString(is);
					if (author.equals("")) {
						authorError = "a1/";
						failure = true;
					} else {
						authorError = author + "/";
					}
					break;
				case "time":
					is = part.getInputStream();
					try {
						time = Integer.parseInt(convertStreamToString(is));
						timeError = time + "/";
					} catch (NumberFormatException e) {
						timeError = "t1/";
						failure = true;
					}
					break;
				case "description":
					is = part.getInputStream();
					description = convertStreamToString(is);
					if (description.equals("")) {
						descriptionError = "d1/";
						failure = true;
					} else {
						descriptionError = description + "/";
					}
					break;
				default:
					is = part.getInputStream();
					try {
						int count = Integer.parseInt(convertStreamToString(is));
						Ingredient ingredient = RecipesDatabase
								.getIngredient(dataName);
						ingredients.put(ingredient, count);
						ingredientsError = ingredientsError + dataName + "/"
								+ count + "/";
					} catch (NumberFormatException e) {
						ingredientsError = ingredientsError + "i2/" + dataName
								+ "/" + 100 + "/";
						failure = true;
					}
					ingred = true;
					noIngredients = false;
					break;

				}
			} else {
				ingred = false;
				is = part.getInputStream();
				String amount = convertStreamToString(is);
				ingredientsError = ingredientsError + amount + "/";
			}
		}

		if (noIngredients) {
			ingredientsError = ingredientsError + "i1/";
			failure = true;
		}

		int id = 0;

		if (!noImage) {
			try {
				String sourceFile = savePath + File.separator + image;
				file = new File(sourceFile);
				bi = new BufferedImage(180, 200, BufferedImage.TYPE_INT_ARGB);
				BufferedImage im = ImageIO.read(file);

				Graphics g = bi.getGraphics();
				g.drawImage(im, 0, 0, bi.getWidth(), bi.getHeight(), 0, 0,
						im.getWidth(), im.getHeight(), null);
				pictureError = "p0";
			} catch (Exception e) {
				pictureError = "p1";
				failure = true;
				file.delete();

			}
		}else{
			pictureError = "p0";
		}

		String error = recipeError + authorError + timeError + descriptionError
				+ ingredientsError + pictureError;
		System.out.println(error);
		if (!failure) {
			Recipe recipe = new Recipe(name, time, ingredients, author,
					description);

			id = RecipesDatabase.addRecipe(recipe);

			if(!noImage){
				String pic = name.replace(' ', '_') + '_' + id + ".png";
				File outputfile = new File(savePath + File.separator
						+ pic);
				ImageIO.write(bi, "png", outputfile);
				file.delete();

				RecipesDatabase.addPic(id, pic);
			}else{
				RecipesDatabase.addPic(id, "default.png");
			}

			response.sendRedirect("recipe.html?r_id=" + id);
		} else {
			response.sendRedirect("drawUp.html?" + error);
		}

	}

	private String extractimage(Part part) throws ServletException, IOException {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}
	
	@SuppressWarnings("resource")
	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}