var width;

window.onload = function beginn() {
	doPost();
}

window.onresize = function() {
	doPost();
}

function doPost() {
	var oder = getOrder();
	var search = location.search;
	var ingredient = search.substring(5, search.indexOf("_"));
	var filter = search.substring(search.indexOf("filter:") + 7, search.length);
	var ingFilterWidth = "";
	if (document.getElementById("filterHead").style.visibility == "hidden") {
		ingFilterWidth = "35";
	} else {
		ingFilterWidth = "300";
	}

	var data = "";

	$.post("Main", {
		ingredients : ingredient,
		filter : filter,
		screenWidth : $(document).width(),
		order : order,
		ingFilterWidth : ingFilterWidth
	}, function(data) {
		setUp(data);
	});
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

function setUp(res) {
	var filters = "";
	var ingredients = "";
	ingredients = res.substring(0, res.indexOf("%0"));
	res = res.replace(ingredients + "%0", "");
	filters = res.substring(0, res.indexOf("%1"));
	res = res.replace(filters + "%1", "");
	data = res.substring(0, res.indexOf("%2"));
	document.getElementById("tHave").innerHTML = ingredients;
	document.getElementById("filter").innerHTML = filters;
	document.getElementById("tableResult").innerHTML = data;
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
	reload();
}