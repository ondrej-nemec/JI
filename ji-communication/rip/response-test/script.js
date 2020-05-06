
$(document).ready(function() {
	$.ajax({
	  url: "index.php",
	 //  context: "application/json",
	  success: function(data) {
	  	 $('#key').text(data['key']);
	  }
	});
});