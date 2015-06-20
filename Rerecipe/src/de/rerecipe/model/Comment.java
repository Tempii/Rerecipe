package de.rerecipe.model;

public class Comment {
	
	private String author;
	private int rating;
	private String comment;
	
	public Comment(String author, int rating, String comment) {
		this.author = author;
		this.rating = rating;
		this.comment = comment;
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
