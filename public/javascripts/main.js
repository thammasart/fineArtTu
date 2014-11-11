var array = [];
var pagingNumber = 15;
var tableConfiguration = {
		"info" : false,
		"iDisplayLength": pagingNumber,   // records per page
		"sPaginationType": "bootstrap",
		"sDom": "t <'pagingContainer'p> ",
		"fnDrawCallback": function(oSettings) {
	        if (oSettings.aiDisplay.length < pagingNumber) {
	            $(oSettings.nTableWrapper).find('.dataTables_paginate').hide();
	            $('.pagingContainer').append("<div class='tableFoot' style='width:100%; margin-bottom:50px'> </div>");
	        }else{
	        	$(oSettings.nTableWrapper).find('.dataTables_paginate').show();
	        	$('.pagingContainer .tableFoot').hide();
	        }
	    },
	};

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

function checkAll(tableNum,id){
	var num = tableNum || 0;
	var checkAll = id || "checkAll";
	var check = $("#" + checkAll).prop("checked");
	var checkLists = $(getTable(num)).find('.checkLists');
	console.log(checkLists);
	$.each(checkLists,function(i,field){
		var isChange = field.checked != check;
		field.checked = check;
		if(isChange){
			field.onchange();
		}
	});
}

function isCheckAll(tableNum,id){
	var num = tableNum || 0;
	var checkAll = id||"checkAll";
	var check = $("#" + checkAll).prop("checked");
	var checkLists = $(getTable(num)).find('.checkLists');
	var flag = true;
	$.each(checkLists,function(i,field){
		if(!field.checked){
			$("#" + checkAll).prop("checked",false);
			flag = false;
		}
	});
	if(flag){
		$("#" + checkAll).prop("checked",true);
	}
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

if(!('contains' in String.prototype)) {
    String.prototype.contains = function(str, startIndex) {
             return -1 !== String.prototype.indexOf.call(this, str, startIndex);
    };
}

function destroyTable(num){
	if(num == undefined){
		for(var i=0; i<array.length; i++){
			array[i].destroy();
		}
	}else{
		array[num].destroy();
	}
}
function updateTable(num){
	var tableContent = $('.table.table-striped.overlayTable');
	var table;
	if(num == undefined){
		for(var i=0; i<tableContent.length; i++){
			table = $(tableContent.get(i)).DataTable(tableConfiguration);
			array[i]= table;
		}
	}else{
		table = $(tableContent.get(num)).DataTable(tableConfiguration);
		array[num]= table;
	}
}

function clearTable(num){
	if(num != undefined) array[num].clear().draw();
}

function getTable(num){
	return $('.table.table-striped.overlayTable').get(num);
}

function getDataTable(num){
	return array[num];
}

function setDataTableColumn(id,innerThead){
	destroyTable();
	var table = document.getElementById(id);
	$(table).find('thead').get(0).innerHTML = innerThead;
	updateTable();
	var tableContent = $('.table.table-striped.overlayTable');
	for(var i=0; i<tableContent.length; i++){
		if($(tableContent.get(i)).attr('id')){
			return array[i];
		}
	}
}

function setSearchBox(searchBox,numTable){
	$('#'+searchBox).on( 'keyup change', function () {
		array[numTable].search( $('#'+searchBox).val()).draw();
	});
}

$(document).ready( function () {
	var searchBox = $('.searchS :input');
	var tableContent = $('.table.table-striped.overlayTable');
	var table;
	for(var i=0; i<tableContent.length; i++){
		table = $(tableContent.get(i)).DataTable(tableConfiguration);
		
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