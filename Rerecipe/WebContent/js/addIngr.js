var ingr = [];
var counter = 2;

window.onload  = function beginn() {
	var search = location.search;
	if (search == "?001")
    	window.close();
}

function newIngr() {
	var ingrStruct = "<img src=\"img/remove.png\" width=\"23px\" style=\"vertical-align:middle;\" onClick=\"newIngr();\">"
			+ "<input name= \"ingr"
			+ counter
			+ "\" type=\"text\" width=\"50px\" value=\"Name der Zutat\" class=\"ingredName\">"
			+ "<select name=\"measureUnit"
			+ counter
			+ "\">"
			+ "	<option> Milliliter </option>"
			+ "	<option> Gramm </option>"
			+ "	<option> Anzahl </option>"
			+ "</select>"
			+ "<input name=\"filter"
			+ counter
			+ "\" type=\"checkbox\" value=\"Vegan\" /> Vegan"
			+ "<input name=\"filter"
			+ counter
			+ "\" type=\"checkbox\" value=\"Vegetarian\" /> Vegetarisch"
			+ "<input name=\"filter"
			+ counter
			+ "\" type=\"checkbox\" value=\"NutFree\" /> Nussfrei"
			+ "<input name=\"filter"
			+ counter
			+ "\" type=\"checkbox\" value=\"GlutenFree\" /> Glutenfrei<br>";
	$("#ingrInput").append(ingrStruct);
	counter += 1;

}
function removeSingle(ingrObj) {
	$("#selectedIngr")
			.replaceWith(
					"<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
	for (var i = 0; i < ingr.length; i++) {
		if (ingr[i] === ingrObj) {
			ingr.splice(i, 1);
		}
	}
	for (var i = 0; i < ingr.length; i++) {
		$("#selectedIngr")
				.append(
						"<tr><td><img class=\"removeBttn\" src=\"img/remove.png\" onclick=\"removeSingle('"
								+ ingr[i]
								+ "');\"></td><td>"
								+ ingr[i]
								+ "</td><td><input type=\"text\" name=\""
								+ ingr[i]
								+ "\" value=\"100\"></td><td>g/ml/stk</td></tr>");
	}
}
