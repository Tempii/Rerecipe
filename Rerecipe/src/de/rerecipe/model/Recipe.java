package de.rerecipe.model;

import java.util.List;

public class Recipe extends RecipeResult {
	private String author;
	private String description;

	public Recipe(int id, String name, String picture, int preparationTime,
			double rating, int ingredients, String author,
			String description) {
		super(id, name, picture, preparationTime, rating, ingredients);
		this.author = author;
		this.description = description;
	}

	public Recipe(RecipeResult recipeResult, String author, String description) {
		this(recipeResult.getId(), recipeResult.getName(), recipeResult
				.getPicture(), recipeResult.getPreparationTime(), recipeResult
				.getRating(), recipeResult.getIngredients(), author,
				description);

	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

}
