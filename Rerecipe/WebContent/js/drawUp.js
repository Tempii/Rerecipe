window.onload = function beginn() {
	var search = location.search;
	if (search == "?001")
		document.getElementById("fehler").innerHTML = "F&uuml;llen Sie alle Felder aus und geben mindestens eine Zutat an!<p>";
//	doPost();
}
//
//window.onresize = function() {
//	doPost();
//}
//
//function doPost() {
//	
//	var search = location.search;
//
//	$.post("Main", {
//		query : search
//	}, function(data) {
//		setUp(data);
//	}, "json");
//
//}