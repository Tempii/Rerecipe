

window.onload = function beginn() {
			doPost("rating");
	    }
window.onresize = function resize() {
	var oder = "";
	if (document.getElementById("radioAlp").checked) {
		order = document.getElementById("radioAlp").value;
	} else if (document.getElementById("radioZei").checked) {
		order = document.getElementById("radioZei").value;
	} else if (document.getElementById("radioBew").checked) {
		order = document.getElementById("radioBew").value;
	}
	doPost(order);
};

function doPost(order) {
	var search = location.search;
	var ingredient = search.substring(5,search.indexOf("_"));
	var filter = search.substring(search.indexOf("filter:")+7,search.length);
	var data = "";

	$.post("Main",
		  {ingredients: ingredient, 
		   filter: filter,
		   screenWidth: $(document).width(),
		   order: "missing_ingredients, " + order + " desc"
	},
		  function(data) {
		    setUp(data);
		  }
		);
}

function setUp(res) {
		var filters = "";
		var ingredients = "";
    	ingredients = res.substring(0,res.indexOf("%0"));;
    	res = res.replace(ingredients + "%0","");
    	filters = res.substring(0,res.indexOf("%1"));;
    	res = res.replace(filters + "%1","");
    	data = res.substring(0,res.indexOf("%2"));
    	document.getElementById("tHave").innerHTML=ingredients;
    	document.getElementById("filter").innerHTML=filters;
    	document.getElementById("tableResult").innerHTML=data;
}
	