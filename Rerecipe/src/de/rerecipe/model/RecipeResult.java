package de.rerecipe.model;

import java.util.List;

public class RecipeResult {
	private int id;
	private String name;
	private String picture;
	private int preparationTime;
	private double rating;
	private List<Ingredient> ingredients;
	
	public RecipeResult(int id, String name, String picture,
			int preparationTime, double rating, List<Ingredient> ingredients) {
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.preparationTime = preparationTime;
		this.rating = rating;
		this.ingredients = ingredients;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPicture() {
		return picture;
	}

	public int getPreparationTime() {
		return preparationTime;
	}
	
	public double getRating() {
		return rating;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	
	
	
	
}
