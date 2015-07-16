package de.rerecipe.persistence;

public class Replacer {
	public static String replaceAll(String text) {

		while (text.contains("+"))
			text = text.replace("+", " ");

		while (text.contains("%E4"))
			text = text.replace("%E4", "ä");
		
		while (text.contains("%C4"))
			text = text.replace("%C4", "Ä");

		while (text.contains("%F6"))
			text = text.replace("%F6", "ö");
		
		while (text.contains("%D6"))
			text = text.replace("%D6", "Ö");

		while (text.contains("%FC"))
			text = text.replace("%FC", "ü");
		
		while (text.contains("%DC"))
			text = text.replace("%DC", "Ü");

		while (text.contains("%DF"))
			text = text.replace("%DF", "ß");

		return text;
	}
}
