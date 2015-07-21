var name;
var description;
var firstTime = true;
var count = 0;

document.addEventListener('DOMContentLoaded', function() {
	doColor(5);
	var r_id = location.search.substring(location.search.indexOf("=")+1)
	$.post("RecipeServlet", {
		r_id : r_id,
	}, function(data) {
		recipeMainInfoLoader(r_id, data.r_name, data.r_description,
				data.r_author, data.r_time, data.r_ingredient, data.ri_amount,
				data.i_amountType, data.r_rating, data.vegetarian, data.vegan,
				data.nutfree, data.glutenfree, data.pic);
	}, "json");
}, false);

function recipeMainInfoLoader(id, name, description, author, prepTime,
		ingrName, ingrAmount, ingrAmntType, rating, vegetarian, vegan, nutfree,
		glutenfree, pic) {

	// Share Links generiert auf http://www.sharelinkgenerator.com/
	// Facebook
	$("#shareBox")
			.append(
					"<img src=\"img/fbShare.png\" id=teilenButton onclick=\"window.open('https://www.facebook.com/sharer/sharer.php?u="
							+ document.URL
							+ "','name','width=600,height=400')\"></img>")
	// Twitter
	$("#shareBox")
			.append(
					"<img src=\"img/twitterShare.png\" id=teilenButton onclick=\"window.open('https://twitter.com/home?status="
							+ document.URL
							+ "','name','width=600,height=400')\"></img>")
	// Google Plus
	$("#shareBox")
			.append(
					"<img src=\"img/gPlusShare.png\" id=teilenButton onclick=\"window.open('https://plus.google.com/share?url="
							+ document.URL
							+ "','name','width=600,height=400')\"></img>")

	$("#recipeMainInfo")
			.append(
					("<img src=\"../img/" + pic
							+ "\" id=\"recipeImg\"><h2 id=\"recipeTitle\"> "
							+ name
							+ " </h2><p id=\"recipeDescription\"><strong>Zubereitung:</strong> "
							+ description + "</p>").replace("/img/img", "/img"));

	var additionalInfo = "<p><strong>Author: </strong>" + author
			+ " <strong>Zubereitungszeit:</strong> " + prepTime + " min"
			+ "</p><p><strong>Zutaten:</strong></p>"
			+ "<table id=\"inredientTable\"><tr><th>"
			+ "<strong>Zutat</strong></th><th>"
			+ "<strong>Menge</strong></th><th>"
			+ "<strong>Einheit</strong></th></tr>";
	var ingredientTable = "";
	for (var i = 0; i < ingrName.length; i++) {
		ingredientTable += "<tr><td>" + ingrName[i] + "</td><td>"
				+ ingrAmount[i] + "</td><td>" + ingrAmntType[i] + "</td></tr>";
	}

	$("#additionalInfo").append(
			additionalInfo + "" + ingredientTable + "</table>");
	$("#ratingBox").append(
			"<div style=\"background-color:#f7931e; height:25px; width:"
					+ (rating / 5) * 250 + "px;\">"
					+ "<img src=\"img/ratingBox.png\" ></div>");
	
	var filterImage = "";

	if(vegetarian)
		filterImage += "<img src=\"img/VegetaIcon.png\" class=\"filterImg\">";
	
	if(vegan)
		filterImage += "<img src=\"img/vegan.png\" class=\"filterImg\">";
	
	if(nutfree)
		filterImage += "<img src=\"img/NutfreeIcon.png\" class=\"filterImg\">";
	
	if(glutenfree)
		filterImage += "<img src=\"img/GlutenfreeIcon.png\" class=\"filterImg\">";
	
	document.getElementById("filterInfo").innerHTML = filterImage;
	doCommentPost(false);
}

function doCommentPost(commentPressed) {
	var rate = 5;
	var comment = "";
	var id;
	var author = "";
	var noError = true;
	if (commentPressed) {
		comment = document.getElementById("commentText").value;
		author = document.getElementById("authorInput").value;
		firstTime = true;
		count = 0;
		if (author == "") {
			document.getElementById("error1").innerHTML = "Autor ist ein Pflichtfeld!"
			noError = false;
		}
		if (author == "") {
			document.getElementById("error2").innerHTML = "Kommentar ist ein Pflichtfeld!"
			noError = false;
		}
	}
	if (noError) {
		document.getElementById("loadMore").style.visibility = "visible";
		for (var i = 1; i <= 5; i++) {
			var boxName = "radio" + i;
			if (document.getElementById("colorCommentRate"+i).style.background == "white none repeat scroll 0% 0%") {
				rate = i-1;
				break;
			} else if (document.getElementById("colorCommentRate"+i).style.background == "")
				rate = 5;
		}
		id = location.search.substring(location.search.indexOf("=") + 1,
				location.search.length);
		count = +count +10;
		$.post("CommentServlet", {
			rate : rate,
			comment : comment,
			id : id,
			author : author,
			count : count
		}, function(data) {
			setUpComments(data);
		}, "json");
	}
}

function hexcolor(colorval) {
    var parts = colorval.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    delete(parts[0]);
    for (var i = 1; i <= 3; ++i) {
        parts[i] = parseInt(parts[i]).toString(16);
        if (parts[i].length == 1) parts[i] = '0' + parts[i];
    }
    color = '#' + parts.join('');

    return color;
}

function setUpComments(data) {
	if (data.data.length < 10)
		document.getElementById("loadMore").style.visibility = "hidden";
	if (firstTime) {
		$("#UserComments")
				.replaceWith(
						"<table id=\"UserComments\"><tr><td width=\"25%\"><h3>Author</h3></td><td width=\"25%\"><h3>Bewertung</h3></td><td width=\"50%\"><h3>Kommentar</h3></td></tr></table>");
		firstTime = false;
	}
	for (var i = 0; i < data.data.length; i++) {
		$("#UserComments")
				.append(
						"<tr><td>"
								+ data.data[i].author
								+ "</td><td><div id=ratingBoxComment align=left><div style=\"background-color:#f7931e; height:20px;  width:"
								+ (data.data[i].rate / 5)
								* 100
								+ "px;\"><img src=\"img/ratingBoxSmallG.png\"style=\"width:100px;\"></div></td><td>"
								+ data.data[i].comment + "</td></tr>");
	}
	$("#ratingBox")
			.replaceWith(
					"<div id=\"ratingBox\"><div style=\"background-color:#f7931e; height:50px; width:"
							+ ((data.avgRate / 5) * 250)
							+ "px;\">"
							+ "<img src=\"img/ratingBox.png\"></div></div>");

	document.getElementById("commentText").value = "";

}

function doColor(i) {
	for (var j=1; j<=5; j++) {
		if (j<=i)
			document.getElementById("colorCommentRate"+j).style.background = "#f7931e";
		else
			document.getElementById("colorCommentRate"+j).style.background = "white";
	}
}
