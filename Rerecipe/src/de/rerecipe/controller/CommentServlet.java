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

import de.rerecipe.model.Comment;
import de.rerecipe.model.RecipeResult;
import de.rerecipe.model.Search;
import de.rerecipe.model.Search.EnteredIngredient;
import de.rerecipe.persistence.RecipesDatabase;
import de.rerecipe.persistence.Replacer;

/**
 * Servlet implementation class Main
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CommentServlet() {
		super();
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
		float avgRate = 0;
		int rate = Integer.parseInt(request.getParameter("rate"));
		int r_id = Integer.parseInt(request.getParameter("id"));
		int count = Integer.parseInt(request.getParameter("count"));
		String comment = Replacer.replaceAll(request.getParameter("comment"));
		String author = Replacer.replaceAll(request.getParameter("author"));
		if (!comment.equals("") && !author.equals("")) {
			RecipesDatabase.addComment(new Comment(r_id, author, rate, comment));
		}
		List<Comment> comments = RecipesDatabase.getComments(r_id, count-9, count);

		JSONArray JSONComments = new JSONArray();
		for (Comment item : comments) {
			JSONObject JSONComment = new JSONObject();
			JSONComment.put("author", item.getAuthor());
			JSONComment.put("comment", item.getContent());
			JSONComment.put("rate", item.getRating());
			avgRate += item.getRating();
			JSONComments.add(JSONComment);
		}
		avgRate = avgRate / comments.size();
		JSONObject data = new JSONObject();
		data.put("data", JSONComments);
		data.put("avgRate", avgRate);
		writer.print(data);
	}

}
