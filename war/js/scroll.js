/*
 * Paolo Ciccarese
 */

function scrollToDomElement(frame, theElement) {
	var selectedPosX = 0;
	var selectedPosY = 0;
              
	while(theElement != null){
		//selectedPosX += theElement.offsetLeft;
		selectedPosY += theElement.offsetTop;
		theElement = theElement.offsetParent;
	}		
	
	var y = selectedPosY - frame.contentWindow.pageYOffset
	
	frame.contentWindow.scrollBy(0,y-20); // horizontal and vertical scroll increments
	//scrolldelay = setTimeout('pageScroll()',100); // scrolls every 100 milliseconds
	
	//frame.contentWindow.scrollTo(selectedPosX,selectedPosY-20);
}
