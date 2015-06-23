window.onload = function beginn() {
	hide();
	var search = location.search;
	if (search == "?001")
		document.getElementById("fehler").innerHTML = "Geben Sie einen Rezeptnamen an!";
	else if (search == "?010")
		document.getElementById("fehler").innerHTML = "Geben Sie einen Autoren an!";
	else if (search == "?011")
		document.getElementById("fehler").innerHTML = "Geben Sie die Bearbeitungszeit an!";
	else if (search == "?100")
		document.getElementById("fehler").innerHTML = "Geben Sie eine Beschreibung an!";
	else if (search == "?101")
		document.getElementById("fehler").innerHTML = "Geben Sie mindestens eine Zutat an!";
	else if (search == "?110")
		document.getElementById("fehler").innerHTML = "Geben Sie die Zeit in Minuten (als Ziffern) an!";
	

}

function hide() {
	document.getElementById("addWrapper").style.visibility = "hidden";
}
function show() {
	document.getElementById("addWrapper").style.visibility = "visible";
}

function doPost() {
	var name = document.getElementById("name").value;
	var measure = document.getElementById("measure").value;
	var vegan = false;
	var vegetarian = false;
	var nutFree = false;
	var glutenFree = false;
	if (document.getElementById("Vegan").checked)
		vegan = true;
	if (document.getElementById("Vegetarian").checked)
		vegetarian = true;
	if (document.getElementById("NutFree").checked)
		nutFree = true;
	if (document.getElementById("GlutenFree").checked)
		glutenFree = true;
	$.post("addIngr", {
		name: name,
		measure: measure,
		vegan: vegan,
		vegetarian: vegetarian,
		nutFree: nutFree,
		glutenFree: glutenFree
	}, function(data) {
		$("#selectedIngr").append(
				"<tr><td><img class=\"removeBttn\" onclick=\"removeSingle('"
						+ data.name
						+ "');\" src=\"img/remove.png\"></img></td><td>"
						+ data.name
						+ "</td><td><input type=\"text\" value=\"100\" name=\"" + data.name
						+ "\"></input></td><td>"+data.measure+"</td></tr>");
		ingr.push(data.name);
	}, "json");
}