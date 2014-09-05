function validateNumberKey(evt) {

  var theEvent = evt || window.event;
  var key = theEvent.keyCode || theEvent.which;

    if(key == 37 || key == 38 || key == 39 || key == 40 || key == 8 || key == 46) { // Left / Up / Right / Down Arrow, Backspace, Delete keys
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
