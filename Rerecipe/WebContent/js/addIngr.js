var ingr = [];
 function newIngr() {
				var ingrStruct = "<img src=\"img/remove.png\" width=\"23px\" style=\"vertical-align:middle;\" onClick=\"newIngr();\">"
                                       + "<input type=\"text\" width=\"50px\" value=\"Name der Zutat\" class=\"ingredName\">"
										+"<select name=\"measureUnit\">"
										+"	<option> Milliliter </option>"
										+"	<option> Gramm </option>"
										+"	<option> Anzahl </option>"	
										+"</select>"
										+"<input name=\"filter[]\" type=\"checkbox\" value=\"vegan\" /> Vegan"
										+"<input name=\"filter[]\" type=\"checkbox\" value=\"vegetarian\" /> Vegetarisch"
										+"<input name=\"filter[]\" type=\"checkbox\" value=\"nutfree\" /> Nussfrei"
										+"<input name=\"filter[]\" type=\"checkbox\" value=\"glutenfree\" /> Glutenfrei<br>";
                $("#newIngr").append(ingrStruct);
                
        }
function removeSingle(ingrObj) {
                $("#selectedIngr").replaceWith("<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
                for (var i = 0; i < ingr.length; i++) {
                                if (ingr[i] === ingrObj) {
                                ingr.splice(i, 1);
                        }
                }
                for (var i = 0; i < ingr.length; i++) {
                        $("#selectedIngr").append("<tr><td><img class=\"removeBttn\" src=\"img/remove.png\" onclick=\"removeSingle('"+ingr[i]+"');\"></td><td>"+ingr[i]+"</td><td><input type=\"text\" name=\""+ingr[i]+"\" value=\"100\"></td><td>g/ml/stk</td></tr>");
                        }
                }


