var ingr = new Array();
var ingrCtr = 0;
function resetIngr() {
	$("#selectedIngr")
			.replaceWith(
					"<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
	ingr = [];
	ingrCtr = 0;
}
function removeSingle(ingrObj) {
	for (var i = 0; i<ingr.length; i++) {
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
	}, function(data) {
		fillList(data);
	}, "json");

}

var ingr = [];

function fillList(data) {
	document.getElementById("ingredName").innerHTML = "";
	var res = data.data;
	for (var i = 0; i < res.length; i++) {
		$("#ingredName").append(
				"<option value=\"" + res[i].name + "\">" + res[i].name
						+ "</option>");
	}
}

function putIn() {
	var input = document.getElementById("ingred").value;
	var push = true;
	var isInOptions = false;
	document.getElementById("ingred").value = "";
	for (var i = 0; i < ingr.length; i++) {
		if (ingr[i]["name"] == input) {
			push = false;
		}
	}
	for (var i = 0; i<document.getElementsByTagName("option").length; i++) {
		if (document.getElementsByTagName("option")[i].value == input) {
			isInOptions = true;
		}
	}
	
	if (push && isInOptions) {
		ingr[ingrCtr] = new Array();
		ingr[ingrCtr]["name"] = input;
		ingr[ingrCtr]["count"] = 100;
		ingr[ingrCtr]["amount"] = "g";
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
								+ ingr[ingrCtr]["amount"] + "</td></tr>");
		ingrCtr += 1;
	}
}
