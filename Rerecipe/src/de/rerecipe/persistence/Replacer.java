package de.rerecipe.persistence;

public class Replacer {
	public static String replaceAll(String text) {

		while (text.contains("+"))
			text = text.replace("+", " ");

		while (text.contains("%E4"))
			text = text.replace("%E4", "�");
		
		while (text.contains("%C4"))
			text = text.replace("%C4", "�");

		while (text.contains("%F6"))
			text = text.replace("%F6", "�");
		
		while (text.contains("%D6"))
			text = text.replace("%D6", "�");

		while (text.contains("%FC"))
			text = text.replace("%FC", "�");
		
		while (text.contains("%DC"))
			text = text.replace("%DC", "�");

		while (text.contains("%DF"))
			text = text.replace("%DF", "�");

		return text;
	}
}
