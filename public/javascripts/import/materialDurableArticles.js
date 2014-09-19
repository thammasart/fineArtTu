    var availableTags = [
      "11",
      "119",
      "13",
      "25",
      "259"
    ];
$(function() {
    $( "#groupId" ).autocomplete({
      source: availableTags
    });
  });
