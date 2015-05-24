package de.rerecipe.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import de.rerecipe.persistence.RecipesDatabase;

public class AutocompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
		String[] recipes = RecipesDatabase.getIngredientNames(request.getParameter("term"));
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(recipes));
     
       
    }
	
	public AutocompleteServlet() {
        super();
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String[] recipes = RecipesDatabase.getIngredientNames(request.getParameter("term"));
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(recipes));
       
    }

   

}