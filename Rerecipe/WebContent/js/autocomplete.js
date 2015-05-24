var ingr = [];
 function resetIngr() {
                $("#selectedIngr").replaceWith("<div id=\"selectedIngr\" style=\"font-size:23px; display:inline-block;\"></div>");
                ingr = [];
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



function contains(a, obj) {
                        for (var i = 0; i < a.length; i++) {
                                if (a[i] === obj) {
                                return true;
                        }
                }
                return false;
        }
  $(function() {

        var availableTags = [
      "Mehl",
      "Milchpulver",
      "Eier",
      "Milch",
      "Schokolade",
      "Kartoffel",
      "Kartoffelbreipulver",
      "Fischfilet",
      "Fischoel",
      "Fisch",
      "Banane",
      "Bananenschale",
      "Bananenbrei"
    ];

        $( "#ingred" ).autocomplete({
            source: "AutocompleteServlet",
            minLength: 2,
            select: function( event, ui ) {
      		if(!contains(ingr, ui.item ? ui.item.label : this.value)) {
      		ingr.push(ui.item ? ui.item.label : this.value);
      		$("#selectedIngr").append("<tr><td><img class=\"removeBttn\" src=\"img/remove.png\" onclick=\"removeSingle('" + ingr[ingr.length - 1] +"');\"></td><td>"+ingr[ingr.length - 1]+"</td><td><input type=\"text\" name=\""+ingr[ingr.length - 1]+"\" value=\"100\"></td><td>g/ml</td></tr>");
      		}
            }
          });
        });