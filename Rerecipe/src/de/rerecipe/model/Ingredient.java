package de.rerecipe.model;

public class Ingredient {
	private int id;
	private String name;
	private String amountType;
	private boolean vegetarian;
	private boolean vegan;

	public Ingredient(int id, String name, String amountType,
			boolean vegetarian, boolean vegan) {
		this.id = id;
		this.name = name;
		this.amountType = amountType;
		this.vegetarian = vegetarian;
		this.vegan = vegan;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAmountType() {
		return amountType;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public boolean isVegan() {
		return vegan;
	}
	
	

}
