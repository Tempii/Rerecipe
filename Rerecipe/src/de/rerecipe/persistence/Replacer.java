package de.rerecipe.persistence;

public class Replacer {
	public static String replaceAll(String text) {
		
		while (text.contains("%C3%A4"))
			text = text.replace("%C3%A4", "�");
		
		while (text.contains("%C3%84"))
			text = text.replace("%C3%84", "�");

		while (text.contains("%C3%B6"))
			text = text.replace("%C3%B6", "�");
		
		while (text.contains("%C3%96"))
			text = text.replace("%C3%96", "�");

		while (text.contains("%C3%BC"))
			text = text.replace("%C3%BC", "�");
		
		while (text.contains("%C3%9C"))
			text = text.replace("%C3%9C", "�");

		while (text.contains("%C3%9F"))
			text = text.replace("%C3%9F", "�");
		
		while (text.contains("%E2%80%9A"))
			text = text.replace("%E2%80%9A", "�");

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

		while (text.contains("ä"))
			text = text.replace("ä", "�");
		
		while (text.contains("Ä"))
			text = text.replace("Ä", "�");

		while (text.contains("ö"))
			text = text.replace("ö", "�");
		
		while (text.contains("Ö"))
			text = text.replace("Ö", "�");

		while (text.contains("ü"))
			text = text.replace("ü", "�");
		
		while (text.contains("Ü"))
			text = text.replace("Ü", "�");

		while (text.contains("ß"))
			text = text.replace("ß", "�");
		
		while (text.contains("�?"))
			text = text.replace("�?", "�");
		
		while (text.contains("‚"))
			text = text.replace("‚", "�");
		
		return text;
	}
	

}
