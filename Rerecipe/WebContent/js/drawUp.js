window.onload = function beginn() {
	var search = location.search;
	if (search == "?001")
		document.getElementById("fehler").innerHTML = "Geben Sie einen Rezeptnamen an!<p>";
	else if (search == "?010")
		document.getElementById("fehler").innerHTML = "Geben Sie einen Autoren an!<p>";
	else if (search == "?011")
		document.getElementById("fehler").innerHTML = "Geben Sie die Bearbeitungszeit an!<p>";
	else if (search == "?100")
		document.getElementById("fehler").innerHTML = "Geben Sie eine Beschreibung an!<p>";
	else if (search == "?101")
		document.getElementById("fehler").innerHTML = "Geben Sie mindestens eine Zutat an!<p>";
	else if (search == "?110")
		document.getElementById("fehler").innerHTML = "Geben Sie die Zeit in Minuten (als Ziffern) an!<p>";
	

}