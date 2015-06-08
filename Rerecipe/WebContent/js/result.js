
window.onload = function beginn() {
	doPost();
}

window.onresize = function() {
	doPost();
}

function setUp(res) {
	// res.ings = Zutaten
	// res.filter = Filter
	// res.results = Ergebnisse
	
	var screenWidth = $(document).width();	
	var ingFilterWidth = 0;
	if (document.getElementById("filterHead").style.visibility == "hidden") {
		ingFilterWidth = "35";
	} else {
		ingFilterWidth = "300";
	}
	var timeToShow = Math.floor((screenWidth - ingFilterWidth) / 250);
	
	var ingreds = "<tr><th>Zutat</th><th>Menge</th><th>Einheit</th>";
	var filters = "";
	var results = "";
	for (var i = 0; i < res.ings.length; i++) {
		ingreds += "<tr><td>" + res.ings[i].name
						+ "</td><td>" + res.ings[i].amount
						+ "</td><td>g/ml/stk</td></tr>";
		/*ingreds += "<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"><tr><td><img class=\"removeBttn\" onclick=\"removeSingle('"
				+ res.ings[i].name
				+ "');\" src=\"img/remove.png\"></img></td><td>"
				+ res.ings[i].name
				+ "</td><td><input type=\"text\" value=\""
				+ res.ings[i].amount
				+ "\" name=\""
				+ res.ings[i].name
				+ "\"></input></td><td>g/ml/stk</td></tr></div>";*/
	}
	document.getElementById("tHave").innerHTML = ingreds;

	for (var i = 0; i < res.filter.length; i++) {
		filters += "<td>" + res.filter[i] + "<br></td>";
	}
	document.getElementById("filter").innerHTML = filters;

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
				+ "\" onclick=\"document.location=recipe.html?r_id=this.id+'';return false;\" ><img alt="
				+ res.results[i].id
				+ " src="
				+ res.results[i].pic
				+ " id=\"recipeImg\"></a><div id=ratingBox align=left><div style=\"background-color:#f7931e; height:20px;  width:"
				+ (res.results[i].rating / 5) * 100
				+ "px;\"><img src=\"img/ratingboxsmall.png\"></div></div>";
		if (res.results[i].ingredients == 1)
			results += "Es fehlt ihnen 1 Zutat!";
		else if (res.results[i].ingredients > 1)
			results += "Es fehlen ihnen " + res.results[i].ingredients + " Zutaten!";
		else
			results += "Sie haben alle Zutaten.";
		if (i % timeToShow == (timeToShow - 1)) 
			results += "<tr>"
	}
	document.getElementById("tableResult").innerHTML = results;
}

function getOrder() {
	var oder = "";
	if (document.getElementById("radioAlp").checked) {
		order = " r_name";
	} else if (document.getElementById("radioZei").checked) {
		order = " r_time";
	} else if (document.getElementById("radioBew").checked) {
		order = " rating desc";
	}

	return order;

}

function doPost() {
	var oder = getOrder();
	var search = location.search;

	$.post("Main", {
		query : search,
		order : order
	}, function(data) {
		setUp(data);
	}, "json");

}

function showHide() {
	var val = document.getElementById("ShowHide").value;
	var shoHid = "";
	if (val == "<<") {
		shoHid = "hidden";
		width = document.getElementById("ingredFilter").style.width;
		document.getElementById("ShowHide").value = ">>";
		document.getElementById("ingredFilter").style.maxWidth = "35px";
	} else {
		shoHid = "visible"
		document.getElementById("ShowHide").value = "<<";
		document.getElementById("ingredFilter").style.maxWidth = "300px";
	}
	document.getElementById("tHave").style.visibility = shoHid;
	document.getElementById("filter").style.visibility = shoHid;
	document.getElementById("filterHead").style.visibility = shoHid;
	document.getElementById("ingHead").style.visibility = shoHid;
	doPost();
}
