//--------------------------------------------------------------------------------------------------------
// Script.js
//--------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------
// openNewWindow
//
// Opens a full size window containing the given page
//--------------------------------------------------------------------------------------------------------

  function openNewWindow(inURL) {

    var theName=new Date();
    theName="X"+theName.getTime();

    var theScreenHeight=480;
    var theScreenWidth=640;
    if (window.screen!=null) {
      theScreenHeight=window.screen.height;
      theScreenWidth=window.screen.width;
    }

    var theHeight=Math.round(Math.min(1000,theScreenHeight*0.80));
    var theWidth=Math.round(Math.min(theHeight*0.80,700+(theScreenWidth-740)*0.50));

    var theFeatures="toolbar,titlebar,directories,location,status,"+
      "menubar,scrollbars,resizable,width="+theWidth+",height="+theHeight;

    var docView=window.open(inURL,theName,theFeatures);
  }

//--------------------------------------------------------------------------------------------------------
// openPopupWindow
//
// Pops up a small window containing the given page, suitable for a glossary entry
//--------------------------------------------------------------------------------------------------------

  function openPopupWindow(inURL,inBig) {

    var theName=new Date();
    theName="X"+theName.getTime();

    var theScreenHeight=480;
    var theScreenWidth=640;
    if (window.screen!=null) {
      theScreenHeight=window.screen.height;
      theScreenWidth=window.screen.width;
    }

    var theHeight=320;
    var theWidth=400;
    if (inBig==true) {
      theHeight=Math.round(Math.min(1000,theScreenHeight*0.80));
      theWidth=Math.round(Math.min(theHeight*0.80,700+(theScreenWidth-740)*0.50));
    } else {
      theHeight=Math.round(320+(Math.min(1000,theScreenHeight)-360)*0.30);
      theWidth=Math.round(400+(Math.min(1000,theScreenWidth)-440)*0.50);
    }

    var theFeatures="titlebar,scrollbars,resizable,width="+theWidth+",height="+theHeight;

    var docView=window.open(inURL,theName,theFeatures);
  }

//--------------------------------------------------------------------------------------------------------
// countdown
//--------------------------------------------------------------------------------------------------------

  function countdown() {
    var theCountElement=document.getElementById('countdown_count');
    var theN=theCountElement.innerHTML;
    if (theN>1) {
      theN=theN-1;
      theCountElement.innerHTML=theN;
      window.setTimeout(countdown,1000);
    } else {
      document.getElementById("countdown").innerHTML="Click on the link to continue.";
      var theDestLink=document.getElementById('dest_link');
      if (theDestLink!=null)
        location.href=theDestLink.href;
    }
  }

//--------------------------------------------------------------------------------------------------------
// doLoadActions
//
// Puts cursor in text box with id="first_focus"
//--------------------------------------------------------------------------------------------------------

  function doLoadActions() {
    var theInput=document.getElementById("first_focus");
    if (theInput!=null)
      theInput.focus();
    var theElement=document.getElementById("countdown");
    if (theElement!=null) {
      theElement.innerHTML="Click on the link if you are not automatically redirected in <span id=\"countdown_count\" class=\"header1\">5</span> seconds.";
      window.setTimeout(countdown,1000);
    }
  }









