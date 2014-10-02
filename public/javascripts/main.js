function validateNumberKey(evt) {

  var theEvent = evt || window.event;
  var key = theEvent.keyCode || theEvent.which;

    if(key==9 || key == 37 || key == 38 || key == 39 || key == 40 || key == 8 || key == 46) { // Left / Up / Right / Down Arrow, Backspace, Delete keys
         return;
     }
    key = String.fromCharCode( key );

  var regex = /[0-9]\b/;
  if( !regex.test(key) ) {
    theEvent.returnValue = false;
    if(theEvent.preventDefault) theEvent.preventDefault();
  }
}
function goBack() {
    window.history.back()
}

Array.prototype.remove = function() {
    var what, a = arguments, L = a.length, ax;
    while (L && this.length) {
        what = a[--L];
        while ((ax = this.indexOf(what)) !== -1) {
            this.splice(ax, 1);
        }
    }
    return this;
};

if(!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(needle) {
        for(var i = 0; i < this.length; i++) {
            if(this[i] === needle) {
                return i;
            }
        }
        return -1;
    };
}

Element.prototype.remove = function() {
    this.parentElement.removeChild(this);
}
NodeList.prototype.remove = HTMLCollection.prototype.remove = function() {
    for(var i = 0, len = this.length; i < len; i++) {
        if(this[i] && this[i].parentElement) {
            this[i].parentElement.removeChild(this[i]);
        }
    }
}
$(document).ready( function () {
	var searchBox = $('.searchS :input');
	var tableContent = $('.table.table-striped.overlayTable');
	var table;
	var array = [];
	for(var i=0; i<tableContent.length; i++){
		table = $(tableContent.get(i)).DataTable({
			info : false,
			"iDisplayLength": 15,   // records per page
			"sPaginationType": "bootstrap",
			"sDom": "t <'pagingContainer'p> ",
		});
		array[i]= table;
	}
	for(var i=0; i<searchBox.length; i++){
		if(searchBox.length < tableContent.length){
			$(searchBox.get(i)).on( 'keyup change', function () {
				var list = $(this).parent().parent().parent().parent().parent().parent().get(0);
				var x = $(list).find(".table.table-striped.overlayTable");
				for(var tmp=0; tmp < x.length; tmp++){
					var id = x.get(tmp).id.slice(-1);
					array[id].search( this.value ).draw();
				}
			});
		}else{
			$(searchBox.get(i)).on( 'keyup change', function () {
				var id = $(this).parent().parent().parent().parent().parent().parent().attr('id');
				if(id != undefined && !isNaN(id)){
					array[id-1].search( this.value ).draw();
				}else{
					table.search(this.value).draw();
				}
			});
		}
	}
} );