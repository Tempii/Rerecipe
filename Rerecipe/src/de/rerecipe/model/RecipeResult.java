package de.rerecipe.model;

import java.util.List;
import java.util.Map;

import de.rerecipe.model.Search.EnteredIngredient;

public class RecipeResult {
	private int id;
	private String name;
	private String picture;
	private int preparationTime;
	private double rating;
	private boolean isVegetarian;
	private boolean isVegan;
	private boolean isNutFree;
	private boolean isGlutenFree;
	private int missingCount;
	private Map<Ingredient, Integer> ingredients;

	public RecipeResult(int id, String name, int preparationTime,
			double rating, int missingCount,
			Map<Ingredient, Integer> ingredients) {
		this.id = id;
		this.name = name;
		this.picture = "img/" + name + ".png";
		this.preparationTime = preparationTime;
		this.rating = rating;
		this.ingredients = ingredients;
		this.missingCount = missingCount;
		setVegetarian();
		setVegan();
		setNutFree();
		setGlutenFree();
	}

	private void setVegetarian() {
		for (Ingredient ingredient : ingredients.keySet()) {
			if (!ingredient.isVegetarian()) {
				return;
			}
		}
		isVegetarian = true;
	}

	private void setVegan() {
		for (Ingredient ingredient : ingredients.keySet()) {
			if (!ingredient.isVegan()) {
				return;
			}
		}
		isVegan = true;
	}

	private void setNutFree() {
		for (Ingredient ingredient : ingredients.keySet()) {
			if (!ingredient.isNutFree()) {
				return;
			}
		}
		isNutFree = true;
	}

	private void setGlutenFree() {
		for (Ingredient ingredient : ingredients.keySet()) {
			if (!ingredient.isGlutenFree()) {
				return;
			}
		}
		isGlutenFree = true;
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

	public boolean isVegetarian() {
		return isVegetarian;
	}

	public boolean isVegan() {
		return isVegan;
	}

	public boolean isNutFree() {
		return isNutFree;
	}

	public boolean isGlutenFree() {
		return isGlutenFree;
	}

	public Map<Ingredient, Integer> getIngredients() {
		return ingredients;
	}

	public int getMissingCount() {
		return missingCount;
	}

	public String getMissingIngredients(
			List<EnteredIngredient> enteredIngredients) {
		if (missingCount == 0)
			return "Sie haben alle Zutaten!";

		StringBuilder builder = new StringBuilder();

		builder.append("Es fehlt ihnen ");

		for (Map.Entry<Ingredient, Integer> ingredientEntry : ingredients
				.entrySet()) {
			Ingredient ingredient = ingredientEntry.getKey();
			int requiredAmount = ingredientEntry.getValue();
			EnteredIngredient enteredIngredient = getEnteredIngredient(
					enteredIngredients, ingredient);
			if (enteredIngredient != null)
				if (enteredIngredient.getAmount() < requiredAmount)
					requiredAmount -= enteredIngredient.getAmount();
				else
					continue;

			builder.append(requiredAmount);
			builder.append(" " + ingredient.getAmountType());
			builder.append(" " + ingredient.getName());
			builder.append(", ");
		}
		builder.deleteCharAt(builder.length() - 2);
		builder.append("(" + missingCount
				+ (missingCount == 1 ? " Zutat)" : " Zutaten)"));

		return builder.toString();
	}

	private EnteredIngredient getEnteredIngredient(
			List<EnteredIngredient> enteredIngredients, Ingredient ingredient) {
		for (EnteredIngredient enteredIngredient : enteredIngredients) {
			if (enteredIngredient.getName().equals(ingredient.getName())) {
				return enteredIngredient;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(String
				.format("%d: %25s,\t dauer: %3d,\t rating: %.2f,\t fehlende Zutaten: %2d,\t benötigte Zutaten: ",
						id, name, preparationTime, rating, missingCount));
		for (Map.Entry<Ingredient, Integer> ingredientEntry : ingredients
				.entrySet()) {
			out.append(ingredientEntry.getValue());
			out.append(ingredientEntry.getKey());
			out.append(",    ");
		}
		out.deleteCharAt(out.length() - 5);

		return out.toString();
	}
}
