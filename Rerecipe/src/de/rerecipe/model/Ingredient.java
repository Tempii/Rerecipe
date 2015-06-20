package de.rerecipe.model;

public class Ingredient {
	private int id;
	private String name;
	private String amountType;
	private boolean isVegetarian;
	private boolean isVegan;
	private boolean isNutFree;
	private boolean isGlutenFree;

	public Ingredient(int id, String name, String amountType,
			boolean isVegetarian, boolean isVegan, boolean isNutFree,
			boolean isGlutenFree) {
		this.id = id;
		this.name = name;
		this.amountType = amountType;
		this.isVegetarian = isVegetarian;
		this.isVegan = isVegan;
		this.isNutFree = isNutFree;
		this.isGlutenFree = isGlutenFree;
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

	@Override
	public String toString() {
		return String.format("%s %s (%d, Vt:%b, Vn:%b, Nf:%b, Gf:%b)", amountType, name,
				id, isVegetarian, isVegan, isNutFree, isGlutenFree);
	}

}
