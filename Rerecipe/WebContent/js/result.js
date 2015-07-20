window.onload = function beginn() {
	showHide("hidden");
	doResultPost();
	$(window).scroll(function() {
		   if($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
		       
		   }
		});
}

window.onresize = function() {
	doResultPost();
}



function setUp(res) {
	// res.ings = Zutaten
	// res.filter = Filter
	// res.results = Ergebnisse
	$("#selectedIngr")
			.replaceWith(
					"<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
	ingr = [];
	ingrCtr = 0;
	var ingreds = "";
	var filters = "";
	var results = "";

	for (var i = 0; i < res.ings.length; i++) {
		appendIng(res.ings[i].name, res.ings[i].amount, res.ings[i].measure);
		ingr[ingrCtr] = new Array();
		ingr[ingrCtr]["name"] = res.ings[i].name;
		ingr[ingrCtr]["count"] = res.ings[i].amount;
		ingr[ingrCtr]["amount"] = res.ings[i].measure;
		ingrCtr += 1;
	}

	for (var i = 0; i < res.filter.length; i++) {
		document.getElementById(res.filter[i]).checked = true;
	}

	var screenWidth = $(window).width();
	var timeToShow = Math.floor(screenWidth / 250);
	var WrapperHeight = $(window).height() - 100;
	document.getElementById("easyToHide").style.height = WrapperHeight
			+ "px";
	for (var i = 0; i < res.results.length; i++) {
		// id
		// name
		// pic
		// time
		// rating
		// ingredients
		results += "<td><div id=template><div class=\"label\">"
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
				+ " src=img/"
				+ res.results[i].name.replace(" ", "_").replace(" ", "_")
				+ "_"
				+ res.results[i].id
				+ ".png"
				+ " id=\"recipeImg\"></div></a><div id=\"ratingBox\"><div style=\"background-color:#f7931e; height:20px;  width:"
				+ ((res.results[i].rating / 5) * 100)
				+ "px;\"><img src=\"img/ratingboxsmall.png\" style=\"width:100px;\"></div></div>";
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

function doResultPost() {
	
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
		if ($("#easyToHide").width() == "500px")
			document.getElementById("ResultWrapper").style.width = "550px";
		else
			document.getElementById("ResultWrapper").style.width = "650px";
	} else if (shoHid == "hidden") {
		document.getElementById("ShowHide").value = ">>";
		document.getElementById("ResultWrapper").style.width = "45px";
		document.getElementById("ingredList").style.visibility = shoHid;
	}
	document.getElementById("easyToHide").style.visibility = shoHid;
}