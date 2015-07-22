package de.rerecipe.persistence;

public class Replacer {
	public static String replaceAll(String text) {
		
		while (text.contains("%C3%A4"))
			text = text.replace("%C3%A4", "ä");
		
		while (text.contains("%C3%84"))
			text = text.replace("%C3%84", "Ä");

		while (text.contains("%C3%B6"))
			text = text.replace("%C3%B6", "ö");
		
		while (text.contains("%C3%96"))
			text = text.replace("%C3%96", "Ö");

		while (text.contains("%C3%BC"))
			text = text.replace("%C3%BC", "ü");
		
		while (text.contains("%C3%9C"))
			text = text.replace("%C3%9C", "Ü");

		while (text.contains("%C3%9F"))
			text = text.replace("%C3%9F", "ß");
		
		while (text.contains("%E2%80%9A"))
			text = text.replace("%E2%80%9A", "‚");

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
		
		while (text.contains("%2C"))
			text = text.replace("%2C", ",");
		
		while (text.contains("%2F"))
			text = text.replace("%2F", "/");
		
		while (text.contains("%3A"))
			text = text.replace("%3A", ":");
		
		while (text.contains("%3B"))
			text = text.replace("%3B", ";");
		
		while (text.contains("%40"))
			text = text.replace("%40", "@");

		while (text.contains("%3F"))
			text = text.replace("%3F", "?");
		
		while (text.contains("%2B"))
			text = text.replace("%2B", "+");

		while (text.contains("Ã¤"))
			text = text.replace("Ã¤", "ä");
		
		while (text.contains("Ã„"))
			text = text.replace("Ã„", "Ä");

		while (text.contains("Ã¶"))
			text = text.replace("Ã¶", "ö");
		
		while (text.contains("Ã–"))
			text = text.replace("Ã–", "Ö");

		while (text.contains("Ã¼"))
			text = text.replace("Ã¼", "ü");
		
		while (text.contains("Ãœ"))
			text = text.replace("Ãœ", "Ü");

		while (text.contains("ÃŸ"))
			text = text.replace("ÃŸ", "ß");
		
		while (text.contains("Ã?"))
			text = text.replace("Ã?", "ß");
		
		while (text.contains("â€š"))
			text = text.replace("â€š", "‚");
		
		return text;
	}
	

}
