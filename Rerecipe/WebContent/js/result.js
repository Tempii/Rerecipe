var canLoad = true;
var showingCount = 1;
var maxShow;
var lastOrder;
var timeToShow;
var doIt;

window.onload = function beginn() {
	showHide("hidden");
	maxShow = getTimeToShow() *3;
	doResultPost();
	$(window).scroll(function() {
		   if($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
			   if (canLoad) {
					canLoad = false;
					maxShow = +maxShow +getTimeToShow() *3;
					doResultPost2(showingCount, getTimeToShow()*3);
				}
		   }
		});
}

function doResize() {
}
window.onresize = function() {
	if (document.getElementById("easyToHide").style.visibility =="hidden") {
		document.getElementById("tableResult").innerHTML = "";
		doResultPost();
	} else {
		var WrapperHeight = $(window).height() - 100;
		document.getElementById("easyToHide").style.height = WrapperHeight
				+ "px";
	}
}


function getTimeToShow() {
	var screenWidth = $(window).width();
	return Math.floor(screenWidth / 250);
}

function setUp(res, mustReset) {
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
		appendIng(unescape(res.ings[i].name), res.ings[i].amount, res.ings[i].measure);
		ingr[ingrCtr] = new Array();
		ingr[ingrCtr]["name"] = unescape(res.ings[i].name);
		ingr[ingrCtr]["count"] = res.ings[i].amount;
		ingr[ingrCtr]["amount"] = res.ings[i].measure;
		ingrCtr += 1;
	}

	for (var i = 0; i < res.filter.length; i++) {
		document.getElementById(res.filter[i]).checked = true;
	}

	var timeToShow = getTimeToShow();
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
				+ (" src=\"../img/"
				+ res.results[i].pic).replace("/img/img", "/img")
				+ "\" id=\"recipeImg\"></div></a><div id=\"ratingBox\"><div style=\"background-color:#f7931e; height:20px;  width:"
				+ ((res.results[i].rating / 5) * 100)
				+ "px;\"><img src=\"img/ratingboxsmall.png\" style=\"width:100px;\"></div></div>";
		results += res.results[i].ingredients;
		results = unescape(results);
		if (i % timeToShow == (timeToShow - 1))
			results += "</tr><tr>"
	}

	if (mustReset)
		document.getElementById("tableResult").innerHTML = results;
	else
		document.getElementById("tableResult").innerHTML += results;
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
	document.getElementById("tableResult").innerHTML = "";
	doResultPost2(1, maxShow);
}

function doResultPost2(start, amount) {
	var search = location.search;
	var newOrder = getOrder();
	if (newOrder != order) {
		order = newOrder;
		canLoad = true;
	}

	$.post("Main", {
		query : search,
		order : newOrder,
		start : start,
		amount : amount
	}, function(data) {
		setUp(data, start == 1);
		var resultCount = data.results.length;

		if (resultCount == amount) {
			canLoad = true;
			showingCount = showingCount + amount;
		}
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
			$("#ResultWrapper").css("width","650px");
	} else if (shoHid == "hidden") {
		document.getElementById("ShowHide").value = ">>";
		document.getElementById("ResultWrapper").style.width = "45px";
		document.getElementById("ingredList").style.visibility = shoHid;
	}
	document.getElementById("easyToHide").style.visibility = shoHid;
}
