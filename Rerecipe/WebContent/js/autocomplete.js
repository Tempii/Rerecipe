var ingr = new Array();
var ingrCtr = 0;
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
		$("#selectedIngr")
				.append(
						"<tr><td><img class=\"removeBttn\" src=\"img/remove.png\" onclick=\"removeSingle('"
								+ ingr[i]["name"]
								+ "');\"></td><td>"
								+ ingr[i]["name"]
								+ "</td><td><input name=\""
								+ ingr[i]["name"]
								+ "\" value=\""
								+ ingr[i]["count"]
								+ "\"></td><td>"
								+ ingr[i]["amount"] + "</td></tr>");
	}
}

function changeCount() {

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

var ingr = [];

var data;

function fillList() {
	document.getElementById("ingredList").style.visibility = "visible";
	input = true;
	document.getElementById("ingredList").innerHTML = "";
	for (var i = 0; i < data.length; i++) {
		var link = document.createElement("div");
		link.className = 'ingredIn';
		link.innerHTML = data[i].name;
		link.setAttribute("onclick", "putIn(\"" + data[i].name + "\")");
		$("#ingredList").append(link);
	}
}

function putIn(input) {

	for (var i = 0; i < ingr.length; i++) {
		if (ingr[i]["name"] == input) {
			return;
		}
	}
	document.getElementById("ingred").value = "";

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
		$("#selectedIngr")
				.append(
						"<tr><td><img class=\"removeBttn\" src=\"img/remove.png\" onclick=\"removeSingle('"
								+ ingr[ingrCtr]["name"]
								+ "');\"></td><td>"
								+ ingr[ingrCtr]["name"]
								+ "</td><td><input type=\"text\" name=\""
								+ ingr[ingrCtr]["name"]
								+ "\" value=\""
								+ ingr[ingrCtr]["count"]
								+ "\"></td><td>"
								+ ingr[ingrCtr]["amount"]
								+ "<input type=\"hidden\" name=\""
								+ ingr[ingrCtr]["name"]
								+ "\" value=\""
								+ ingr[ingrCtr]["amount"]
								+ "\"></input></td></tr>");
		ingrCtr += 1;
		document.getElementById("ingredList").style.visibility = "hidden";
	}
}
