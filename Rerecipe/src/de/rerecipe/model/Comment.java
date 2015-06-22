package de.rerecipe.model;

public class Comment {
	private int r_id;
	private String author;
	private int rating;
	private String comment;

	public Comment(int r_id, String author, int rating, String comment) {
		this.r_id = r_id;
		this.author = author;
		this.rating = rating;
		this.comment = comment;
	}

	public int getRid() {
		return r_id;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return comment;
	}

	public int getRating() {
		return rating;
	}

	@Override
	public String toString() {
		return String.format("%s (%d): %s", author, rating, comment);
	}

}
