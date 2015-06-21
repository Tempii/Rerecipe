window.onload = function beginn() {
	showHide("hidden");
	doPost();
}

window.onresize = function() {
	doPost();
}

function setUp(res) {
	// res.ings = Zutaten
	// res.filter = Filter
	// res.results = Ergebnisse
	$("#selectedIngr")
			.replaceWith(
					"<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
	ingr = [];

	var ingreds = "";
	var filters = "";
	var results = "";

	for (var i = 0; i < res.ings.length; i++) {
		$("#selectedIngr").append(
				"<tr><td><img class=\"removeBttn\" onclick=\"removeSingle('"
						+ res.ings[i].name
						+ "');\" src=\"img/remove.png\"></img></td><td>"
						+ res.ings[i].name
						+ "</td><td><input type=\"text\" value=\""
						+ res.ings[i].amount + "\" name=\"" + res.ings[i].name
						+ "\"></input></td><td>g/ml/stk</td></tr>");
		ingr.push(res.ings[i].name);
	}

	for (var i = 0; i < res.filter.length; i++) {
		document.getElementById(res.filter[i]).checked = true;
	}

	var screenWidth = $(window).width();
	var timeToShow = Math.floor(screenWidth / 250);
	var WrapperHeight = $(window).height() - 100;
	document.getElementById("ResultWrapper").style.height = WrapperHeight
			+ "px";
	for (var i = 0; i < res.results.length; i++) {
		// id
		// name
		// pic
		// time
		// rating
		// ingredients
		results += "<td><div id=template><div id=name>"
				+ res.results[i].name
				+ " (&#126;"
				+ res.results[i].time
				+ "min) "
				+ "</div><a href=\"recipe.html?r_id="
				+ res.results[i].id
				+ "\" id=\""
				+ res.results[i].id
				+ "\" onclick=\"document.location=recipe.html?r_id=this.id+'';return false;\" ><div id=\"ImgBoxResult\"><img alt="
				+ res.results[i].id
				+ " src="
				+ res.results[i].pic
				+ " id=\"recipeImg\"></div></a><div id=ratingBox align=left><div style=\"background-color:#f7931e; height:20px;  width:"
				+ (res.results[i].rating / 5) * 100
				+ "px;\"><img src=\"img/ratingboxsmall.png\"></div></div>";
		results += res.results[i].ingredients;

		if (i % timeToShow == (timeToShow - 1))
			results += "</tr><tr>"
	}
	document.getElementById("tableResult").innerHTML = results;
}

function getOrder() {
	var oder = "";
	if (document.getElementById("radioAlp").checked) {
		order = " r_name";
	} else if (document.getElementById("radioZei").checked) {
		order = " r_time";
	} else {
		order = " rating desc";
	}

	return order;

}

function doPost() {
	
	var search = location.search;

	$.post("Main", {
		query : search,
		order : getOrder()
	}, function(data) {
		setUp(data);
	}, "json");

}

function showHide(shoHid) {
	if (shoHid == "") {
		if (document.getElementById("ShowHide").value == "<<") {
			shoHid = "hidden";
		} else if (document.getElementById("ShowHide").value == ">>") {
			shoHid = "visible";
		}
	}
	if (shoHid == "visible") {
		document.getElementById("ShowHide").value = "<<";
		document.getElementById("ResultWrapper").style.width = "600px";
	} else if (shoHid == "hidden") {
		document.getElementById("ShowHide").value = ">>";
		document.getElementById("ResultWrapper").style.width = "30px";
	}
	document.getElementById("easyToHide").style.visibility = shoHid;
}
