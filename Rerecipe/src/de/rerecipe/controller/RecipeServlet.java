package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;

import de.rerecipe.model.RecipesDatabase;

/**
 * Servlet implementation class RecipeServlet
 */
@WebServlet("/RecipeServlet")
public class RecipeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecipeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String r_id = request.getParameter("r_id");
		String r_name = RecipesDatabase.getRecipeName(r_id);
		String r_description = RecipesDatabase.getRecipeDescription(r_id);
		request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();         
        JSONObject obj = new JSONObject();
        obj.put("r_name", r_name);
        obj.put("r_description", r_description);
        out.print(obj);
		/*String r_id = request.getParameter("r_id");
		String r_name = RecipesDatabase.getRecipeName(r_id);
		String r_description = RecipesDatabase.getRecipeDescription(r_id); */
		/*
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(r_name);
		response.getWriter().write(r_description);
		*/
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String r_id = request.getParameter("r_id");
		String r_name = RecipesDatabase.getRecipeName(r_id);
		String r_description = RecipesDatabase.getRecipeDescription(r_id);
		String r_author = RecipesDatabase.getRecipeAuthor(r_id);
		String r_time = RecipesDatabase.getRecipePrepTime(r_id);
		String[] r_ingredient = RecipesDatabase.getRecipeIngred(r_id);
		String[] ri_amount = RecipesDatabase.getRecipeIngredAmnt(r_id);
		String[] i_amountType = RecipesDatabase.getRecipeIngredAmntType(r_id);
		float r_rating = RecipesDatabase.getRecipeRating(r_id);
		request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json"); 
        PrintWriter out = response.getWriter();         
        JSONObject obj = new JSONObject();
        obj.put("r_name", r_name);
        obj.put("r_description", r_description);
        obj.put("r_author", r_author);
        obj.put("r_time", r_time);
        obj.put("r_rating", r_rating);
        JSONArray IngredJson = new JSONArray();
        for(String ingr : r_ingredient) {
        	IngredJson.add(ingr);
        }
        JSONArray IngredAmntJson = new JSONArray();
        for(String ingr : ri_amount) {
        	IngredAmntJson.add(ingr);
        }
        JSONArray IngredAmntTypeJson = new JSONArray();
        for(String ingr : i_amountType) {
        	IngredAmntTypeJson.add(ingr);
        }
        obj.put("r_ingredient",IngredJson);
        obj.put("ri_amount",IngredAmntJson);
        obj.put("i_amountType",IngredAmntTypeJson);
        out.print(obj);
	}

}
