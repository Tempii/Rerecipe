var data;
var ingr = new Array();
var ingrCtr = 0;
var selectedDiv = -1;
var input = false;
function resetIngr() {
	$("#selectedIngr")
			.replaceWith(
					"<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
	ingr = [];
	ingrCtr = 0;
}
function removeSingle(ingrObj) {
	for (var i = 0; i < ingr.length; i++) {
		ingr[i]["count"] = document.getElementsByName(ingr[i]["name"])[0].value;
	}

	$("#selectedIngr")
			.replaceWith(
					"<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
	for (var i = 0; i < ingr.length; i++) {
		if (ingr[i]["name"] === ingrObj) {
			ingr.splice(i, 1);
			ingrCtr -= 1;
		}
	}
	for (var i = 0; i < ingr.length; i++) {
		appendIng(ingr[ingrCtr]["name"], ingr[ingrCtr]["count"], ingr[ingrCtr]["amount"]);
	}
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
					+ "\" value=\""
					+ amount
					+ "\"></input></td></tr>");
}

function doPost() {
	var input = document.getElementById("ingred").value;
	$.post("AutocompleteServlet", {
		term : input
	}, function(newData) {
		data = newData.data
		fillList();
	}, "json");

}

function fillList() {
	document.getElementById("ingredList").style.visibility = "visible";
	input = true;
	document.getElementById("ingredList").innerHTML = "";
	for (var i = 0; i < data.length; i++) {
		var add = true;
		for (var j = 0; j < ingr.length; j++) {
			if (ingr[j]["name"] == data[i].name) {
				add = false;
			}
		}
		if (add) {
			var link = document.createElement("div");
			link.className = 'ingredIn';
			link.innerHTML = data[i].name;
			link.setAttribute("name", "ingredIn");
			link.setAttribute("onclick", "putIn(\"" + data[i].name + "\")");
			link.setAttribute("onmouseover", "setSelectedDiv(\"" + i + "\")");
			link.setAttribute("onkeydown", "keyDown(\"myPrefix\", \"event\")");
			$("#ingredList").append(link);
		}
	}
}

function setSelectedDiv(i) {
	var divItems = document.getElementsByName("ingredIn");
	if (selectedDiv != -1)
		$(divItems[selectedDiv]).css("background-color", "#f2f2f2");
	selectedDiv = i;
	$(divItems[selectedDiv]).css("background-color", "white");
}

function putIn(input) {

	for (var i = 0; i < ingr.length; i++) {
		if (ingr[i]["name"] == input) {
			
			return;
		}
	}
	document.getElementById("ingred").value = "";
	selectedDiv = -1;
	var ingredient;
	for (var i = 0; i < data.length; i++) {
		if (data[i].name == input) {
			ingredient = data[i];
			break;
		}
	}

	if (ingredient != null) {
		ingr[ingrCtr] = new Array();
		ingr[ingrCtr]["name"] = ingredient.name;
		ingr[ingrCtr]["count"] = 100;
		ingr[ingrCtr]["amount"] = ingredient.amountType;
		appendIng(ingr[ingrCtr]["name"], ingr[ingrCtr]["count"], ingr[ingrCtr]["amount"]);
		ingrCtr += 1;
		document.getElementById("ingredList").style.visibility = "hidden";
	}
}

function keyDown(pref, evt) {

	var key = (evt.which) ? evt.which : evt.keyCode;
	var divItems = document.getElementsByName("ingredIn");
	if (key == 40) { // down
		if (selectedDiv > -1)
			$(divItems[selectedDiv]).css("background-color", "#f2f2f2");
		if (selectedDiv < divItems.length)
			selectedDiv = +selectedDiv + 1;
		else
			selectedDiv = 0;
		$(divItems[selectedDiv]).css("background-color", "white");
	} else if (key == 38) { // up
		if (selectedDiv != -1) {
			$(divItems[selectedDiv]).css("background-color", "#f2f2f2");
			selectedDiv = +selectedDiv - 1;
		} else
			selectedDiv = divItems.length - 1;
		$(divItems[selectedDiv]).css("background-color", "white");
	} else if (key == 13) {
		evt.preventDefault();
		if (selectedDiv != -1) {
			putIn(divItems[selectedDiv].innerHTML);
		}
	} else {

	}
}