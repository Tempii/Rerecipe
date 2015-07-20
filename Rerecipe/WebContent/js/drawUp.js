window.onload = function beginn() {
	hide();
	var search = location.search;

	search = unescape(search);
	if (search != "") {
		search = search.substring(1);
		if (search.startsWith("r1")) {
			document.getElementById("error1").innerHTML = "Geben Sie einen Rezeptnamen an!";
			search = search.substring(3);
		} else {
			var help = search.substring(0, search.indexOf("/"));
			document.getElementById("name").value = help;
			search = search.substring(search.indexOf("/") + 1);
		}

		if (search.startsWith("a1")) {
			document.getElementById("error2").innerHTML = "Geben Sie einen Autoren an!";
			search = search.substring(3);
		} else {
			var help = search.substring(0, search.indexOf("/"));
			document.getElementById("author").value = help;
			search = search.substring(search.indexOf("/") + 1);
		}

		if (search.startsWith("t1")) {
			document.getElementById("error3").innerHTML = "Geben Sie die Bearbeitungszeit in Minuten (Ziffern) an!";
			search = search.substring(3);
		} else {
			var help = search.substring(0, search.indexOf("/"));
			document.getElementById("time").value = (help);
			search = search.substring(search.indexOf("/") + 1);
		}

		if (search.startsWith("d1")) {
			document.getElementById("error4").innerHTML = "Geben Sie eine Beschreibung an!";
			search = search.substring(3);
		} else {
			var help = search.substring(0, search.indexOf("/"));
			document.getElementById("description").value = help;
			search = search.substring(search.indexOf("/") + 1);
		}

		if (search.startsWith("i1")) {
			document.getElementById("error6").innerHTML = "Geben Sie mindestens eine Zutat an!";
			search = search.substring(3);
		} else {

			while (!(search.startsWith("p0") || search.startsWith("p1"))) {
				if(search.startsWith("i2")){
					document.getElementById("error7").innerHTML = "Geben sie die Zutatenmengen in Ziffern an!";
					search = search.substring(3);
				}
				var ingred = search.substring(0, search.indexOf("/"));
				search = search.substring(search.indexOf("/") + 1);
				var count = search.substring(0, search.indexOf("/"));
				search = search.substring(search.indexOf("/") + 1);
				var amount = search.substring(0, search.indexOf("/"));
				search = search.substring(search.indexOf("/") + 1);
				appendIng(ingred, count, amount);
			}
		}

		if (search.startsWith("p1")) {
			document.getElementById("error5").innerHTML = "Die von ihnen gew&auml;hlte Datei war kein Bild!";
		}
	}
}

function hide() {
	document.getElementById("addWrapper").style.visibility = "hidden";
}
function show() {
	document.getElementById("addWrapper").style.visibility = "visible";
}

function doIngredPost() {
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
		name : name,
		measure : measure,
		vegan : vegan,
		vegetarian : vegetarian,
		nutFree : nutFree,
		glutenFree : glutenFree
	}, function(data) {
		$("#selectedIngr").append(
				"<tr><td><img class=\"removeBttn\" onclick=\"removeSingle('"
						+ data.name
						+ "');\" src=\"img/remove.png\"></img></td><td>"
						+ data.name
						+ "</td><td><input type=\"text\" value=\"100\" name=\""
						+ data.name + "\"></input></td><td>" + data.measure
						+ "</td></tr>");
		ingr.push(data.name);
	}, "json");
}

function submit() {
	document.forms["form"].submit();
}

function appendIng(name, count, amount) {
	$("#selectedIngr")
			.append(
					"<tr><td><img class=\"removeBttn\" src=\"img/remove.png\" onclick=\"removeSingle('"
							+ name
							+ "');\"></td><td><label class=\"label\">"
							+ name
							+ "</label></td><td><input type=\"text\" name=\""
							+ name
							+ "\" value=\""
							+ count
							+ "\"></td><td>"
							+ amount
							+ "<input type=\"hidden\" name=\""
							+ name
							+ "\" value=\"" + amount + "\"></input></td></tr>");
}