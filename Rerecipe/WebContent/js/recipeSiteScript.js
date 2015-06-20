var name;
var description;
document.addEventListener('DOMContentLoaded', function() {
	var r_id = location.search.split('r_id=')[1];
	$.post("RecipeServlet", {
		r_id : r_id,
	}, function(data) {
		recipeMainInfoLoader(r_id, data.r_name, data.r_description,
				data.r_author, data.r_time, data.r_ingredient, data.ri_amount,
				data.i_amountType, data.r_rating, data.vegetarian, data.vegan,
				data.nutfree, data.glutenfree);
	}, "json");
}, false);

function recipeMainInfoLoader(id, name, description, author, prepTime,
		ingrName, ingrAmount, ingrAmntType, rating, vegetarian, vegan, nutfree,
		glutenfree) {
//	var vegan = true;
//	var vegetarian = false;
//	var glutenfree = false;
	var ingredientTable = "";
	// Share Links generiert auf http://www.sharelinkgenerator.com/
	// Facebook
	$("#recipeMainInfo")
			.append(
					"<button id=teilenButton onclick=\"window.open('https://www.facebook.com/sharer/sharer.php?u="
							+ document.URL
							+ "','name','width=600,height=400')\">Facebook</button>")
	// Twitter
	$("#recipeMainInfo")
			.append(
					"<button id=teilenButton onclick=\"window.open('https://twitter.com/home?status="
							+ document.URL
							+ "','name','width=600,height=400')\">Twitter</button>")
	// Google Plus
	$("#recipeMainInfo")
			.append(
					"<a>Share On</a><button id=teilenButton onclick=\"window.open('https://plus.google.com/share?url="
							+ document.URL
							+ "','name','width=600,height=400')\">Google+</button>")

	$("#recipeMainInfo")
			.append(
					"<img src=\"img/"
							+ name
							+ ".png\" id=\"recipeImg\"><h2 id=\"recipeTitle\"> "
							+ name
							+ " </h2><p id=\"recipeDescription\"><strong>Zubereitung:</strong> "
							+ description + "</p>");

	var additionalInfo = "<p><strong>Author:</strong>" + author
			+ " <strong>Zubereitungszeit:</strong> " + prepTime + " min"
			+ "</p><p><strong>Zutaten:</strong></p>"
			+ "<table id=\"inredientTable\"><tr><th>"
			+ "<strong>Zutat</strong></th><th>"
			+ "<strong>Menge</strong></th><th>"
			+ "<strong>Einheit</strong></th></tr>";
	for (var i = 0; i < ingrName.length; i++) {
		ingredientTable += "<tr><td>" + ingrName[i] + "</td><td>"
				+ ingrAmount[i] + "</td><td>" + ingrAmntType[i] + "</td></tr>";
	}

	$("#additionalInfo").append(
			additionalInfo + "" + ingredientTable + "</table>");
	$("#ratingBox").append(
			"<div style=\"background-color:#f7931e; height:50px; width:"
					+ (rating / 5) * 250 + "px;\">"
					+ "<img src=\"img/ratingBox.png\"></div>");
}
