package de.rerecipe.model;

import java.util.List;

public class Search {

	public static class EnteredIngredient {
		private String name;
		private int amount;

		public EnteredIngredient(String name, int amount) {
			this.name = name;
			this.amount = amount;
		}

		public String getName() {
			return name;
		}

		public int getAmount() {
			return amount;
		}
	}

	private List<EnteredIngredient> ingredients;
	private List<String> filterOptions;
	private String orderBy;

	public Search(List<EnteredIngredient> ingredients, List<String> filterOptions,
			String orderBy) {
		this.ingredients = ingredients;
		this.filterOptions = filterOptions;
		this.orderBy = orderBy;
	}

	public List<EnteredIngredient> getIngredients() {
		return ingredients;
	}

	public List<String> getFilterOptions() {
		return filterOptions;
	}

	public String getOrderBy() {
		return orderBy;
	}

}
