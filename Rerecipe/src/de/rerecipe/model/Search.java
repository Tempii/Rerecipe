package de.rerecipe.model;

import java.util.List;

public class Search {

	public static class EnteredIngredient {
		private String name;
		private double amount;

		public EnteredIngredient(String name, double amount) {
			this.name = name;
			this.amount = amount;
		}

		public String getName() {
			return name;
		}

		public double getAmount() {
			return amount;
		}

	}

	private List<EnteredIngredient> ingredients;
	private List<String> filterOptions;
	private String orderBy;
	private int start;
	private double amount;

	public Search(List<EnteredIngredient> ingredients, List<String> filterOptions,
			String orderBy, int start, double amount) {
		this.ingredients = ingredients;
		this.filterOptions = filterOptions;
		this.orderBy = orderBy;
		this.start = start;
		this.amount = amount;
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

	public int getStart() {
		return start;
	}

	public double getAmount() {
		return amount;
	}

}
