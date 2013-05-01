
var type = "IE"; // Variable used to hold the browser name

detectBrowser();

function detectBrowser() {
  if (window.opera && document.readyState) {
    type="OP"; // The surfer is using Opera of some version
  } else if (document.all) {
    type="IE"; // The surfer is using IE 4+
  } else if (document.layers) {
    type="NN"; // The surfer is using NS 4
  } else if (!document.all && document.getElementById) {
    type="MO"; // The surfer is using NS6+ or Firefox
  } else {
    type="IE"; // I assume it will not get here
  }
}

/*****************************************************************************/

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}

/*****************************************************************************/

var leftmost = -190;
var rightmost = 10;
var interval = null;
var sidebarVisible = false;
var DOMCapable;

document.getElementById ? DOMCapable=true : DOMCapable=false;

function scrollRight(menuId) {
  var leftPosition;
  if (DOMCapable) {
    leftPosition = parseInt(document.getElementById(menuId).style.left);
    if (leftPosition >= rightmost) {
      clearInterval(interval);
      return;
    } else {
      leftPosition += 10;
      document.getElementById(menuId).style.left = leftPosition+"px";
    }
  }
}

function scrollLeft(menuId) {
  if (DOMCapable) {
    leftPosition = parseInt(document.getElementById(menuId).style.left);
    if (leftPosition <= leftmost) {
      clearInterval(interval);
      return;
    } else {
      leftPosition -= 10;
      document.getElementById(menuId).style.left = leftPosition+"px";
    }
  }
}

function slideIn(menuId) {
  if (document.getElementById(menuId)) {
    clearInterval(interval);
    interval = setInterval('scrollRight("'+menuId+'")', 5);
  }
  sidebarVisible = true;
}

function slideOut(menuId) {
  if (document.getElementById(menuId)) {
    clearInterval(interval);
    interval = setInterval('scrollLeft("'+menuId+'")', 5);
  }
  sidebarVisible = false;
}

function toggleSidebar(menuId) {
  if (!sidebarVisible) {
    slideIn(menuId);
  } else {
    slideOut(menuId);
  }
}

/*****************************************************************************/

function popImage(img, title) {
  picfile = new Image();
  picfile.src = (img);
  fileCheck(img, title);
}

function fileCheck(img, title) { 	
  if( (picfile.width!=0) && (picfile.height!=0) ) {
    makeWindow(img, title);
  } else {
    funzione="fileCheck('"+img+"')";
    intervallo=setTimeout(funzione, 50);
  }
}

function makeWindow(img, title) { 	
  wd = picfile.width;
  ht = picfile.height;

  var isIE = (navigator.appName.indexOf("Microsoft") != -1) ? 1 : 0;
  var args = "resizable=yes";
  if (window.screen) {
    var avwd = screen.availWidth;
    var avht = screen.availHeight;

    if (avwd < wd || avht < ht) { args += ",scrollbars=yes"; }

    var xcen = (avwd > wd) ? (avwd - wd) / 2 : 0;
    var ycen = (avht > ht) ? (avht - ht) / 2 : 0;
    args += ",left=" + xcen + ",screenX=" + xcen;
    args += ",top=" + ycen + ",screenY=" + ycen;	

    if (avwd < wd) { 
      wd = avwd;
      if (isIE) wd -= 12;
    }
    if (avht < ht) {
      ht = avht;
      if (isIE) ht -= 32;
    }
  }
  args += ",width=" + wd + ",innerWidth=" + wd;
  args += ",height=" + ht + ",innerHeight=" + ht;

  popwin = window.open(img, title, args);
  popwin.document.open();
  popwin.document.write('<html><head><title>'+title+'</title></head><body style="margin:0px"><img src="'+img+'" border="0"></body></html>');
  popwin.document.close();
}

/*****************************************************************************/

var exifVisible = false;

function setLayer(obj,lyr) {
  var newX = findPosX(obj)+5;
  var newY = findPosY(obj)+5;
  var x = new getObj(lyr);
  x.style.top = newY + 'px';
  x.style.left = newX + 'px';
}

function findPosX(obj) {
  var curleft = 0;

  if (!obj) { return curleft; }
  if (obj.offsetParent) {
    while (obj.offsetParent) {
      curleft += obj.offsetLeft
      obj = obj.offsetParent;
    }
  } else if (obj.x) {
    curleft += obj.x;
  }
  return curleft;
}

function findPosY(obj) {
  var curtop = 0;
  var printstring = '';

  if (!obj) { return curtop; }
  if (obj.offsetParent) {
    while (obj.offsetParent) {
      printstring += ' element ' + obj.tagName + ' has ' + obj.offsetTop;
      curtop += obj.offsetTop
      obj = obj.offsetParent;
    }
  } else if (obj.y) {
    curtop += obj.y;
  }
  window.status = printstring;
  return curtop;
}

function getObj(id) {
  if (type=="IE") {
    this.obj = document.all[id];
    this.style = document.all[id].style;
  } else if (type=="MO" || type=="OP") {
    this.obj = document.getElementById(id);
    this.style = document.getElementById(id).style;
  } else if (type=="NN") {
    if (document.layers[id]) {
      this.obj = document.layers[id];
      this.style = document.layers[id];
    }
  }
}

function toggleExif(parentId, id) {
  var parent = document.getElementById(parentId);

  MM_showHideLayers(id,'',(exifVisible) ? 'hide' : 'show');
  setLayer(parent,id);

  exifVisible = !exifVisible;
}
