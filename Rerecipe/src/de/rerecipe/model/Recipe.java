package de.rerecipe.model;

import java.util.Map;

public class Recipe extends RecipeResult {
	private String author;
	private String description;

	public Recipe(String name, int preparationTime,
			Map<Ingredient, Double> ingredients, String author,
			String description) {
		this(0, name, preparationTime, 0, 0, ingredients, author, description);
	}

	public Recipe(int id, String name, int preparationTime, double rating,
			int missingIngredients, Map<Ingredient, Double> ingredients,
			String author, String description) {
		super(id, name, preparationTime, rating, missingIngredients,
				ingredients);
		this.author = author;
		this.description = description;
	}
	public Recipe(int id, String name, int preparationTime, double rating,
			int missingIngredients, Map<Ingredient, Double> ingredients,
			String author, String description, String picture) {
		super(id, name, preparationTime, rating, missingIngredients,
				ingredients, picture);
		this.author = author;
		this.description = description;
	}

	public Recipe(RecipeResult recipeResult, String author, String description) {
		this(recipeResult.getId(), recipeResult.getName(), recipeResult
				.getPreparationTime(), recipeResult.getRating(), recipeResult
				.getMissingCount(), recipeResult.getIngredients(), author,
				description);

	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public void setPic(String pic) {
		super.setPic(pic);
	}
	
	@Override
	public String toString() {
		return super.toString() + "autor: " + author + "\t\nBeschreibung "
				+ description;
	}

}
