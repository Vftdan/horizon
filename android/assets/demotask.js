/*
 * API.putButton(String text, function onclick({float x, float y})): void;
 * API.putButtonGroup({text1: function onclick1({float x, float y}), ...}): void;
 * API.putLabel(String text): void;
 * API.putTextField(String value, function onkeytyped({int keyCode, String value})): void;
 * API.onSuccess(): void;
 * API.onMistake(): void;
 * API.version = "0.0";
 */
(function(){
	var a = Math.round(Math.random() * 10),
		  b = Math.round(Math.random() * 10);
	API.putLabel(a + " + " + b + " =");
	var value = "";
	var submit = function() {
		var c = parseInt(value);
		if(isNaN(c)) {
			API.putLabel("Not integer");
			return;
		}
		if(a + b == c) API.onSuccess();
		else API.onMistake();
	}
	API.putTextField("", function(e) {
		value = e.value;
		if(e.keyCode == 10 || e.keyCode == 13) submit();
	});
	API.putButton("Check", submit);
})();