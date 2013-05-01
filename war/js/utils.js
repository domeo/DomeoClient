var FRAME_ID = "domeoframe";


/*
 * It returns -1 if the first node is before the second node
 * It returns 1 if the first node is after the second node
 * It returns 0 if the two nodes are identical 
 */
function compareNodesOrder(node1, node2) {
  // Fall out quickly for equality.
  if (node1 == node2) {
    return 0;
  }

  // Use compareDocumentPosition where available
  if (node1.compareDocumentPosition) {
    // 4 is the bitmask for FOLLOWS.
    return node1.compareDocumentPosition(node2) & 2 ? 1 : -1;
  }

  // Process in IE using sourceIndex - we check to see if the first node has
  // a source index or if it's parent has one.
  if ('sourceIndex' in node1 ||
      (node1.parentNode && 'sourceIndex' in node1.parentNode)) {
    var isElement1 = node1.nodeType == goog.dom.NodeType.ELEMENT;
    var isElement2 = node2.nodeType == goog.dom.NodeType.ELEMENT;

    var index1 = isElement1 ? node1.sourceIndex : node1.parentNode.sourceIndex;
    var index2 = isElement2 ? node2.sourceIndex : node2.parentNode.sourceIndex;

    if (index1 != index2) {
      return index1 - index2;
    } else {
      if (isElement1) {
        // Since they are not equal, we can deduce that node2 is a child of
        // node1 and therefore node1 comes first.
        return -1;
      }

      if (isElement2) {
        // Similarly, we know node1 is a child of node2 in this case.
        return 1;
      }

      // If we get here, we know node1 and node2 are both child nodes of the
      // same parent element.
      var s = node2;
      while ((s = s.previousSibling)) {
        if (s == node1) {
          // We just found node1 before node2.
          return -1;
        }
      }

      // Since we didn't find it, node1 must be after node2.
      return 1;
    }
  }

  // For Safari, we cheat and compare ranges.
  var doc = goog.dom.getOwnerDocument(node1);

  var range1, range2, compare;
  range1 = doc.createRange();
  range1.selectNode(node1);
  range1.collapse(true);

  range2 = doc.createRange();
  range2.selectNode(node2);
  range2.collapse(true);

  return range1.compareBoundaryPoints(Range.START_TO_END, range2);
};

function findXPosition(obj) {
	var curleft = 0;
    if(obj.offsetParent)
        while(1) 
        {
          curleft += obj.offsetLeft;
          if(!obj.offsetParent)
            break;
          obj = obj.offsetParent;
        }
    else if(obj.x)
        curleft += obj.x;
    return curleft;
};

function findYPosition(obj) {
	var curtop = 0;
	if(obj.offsetParent)
	    while(1)
	    {
	      curtop += obj.offsetTop;
	      if(!obj.offsetParent)
	        break;
	      obj = obj.offsetParent;
	    }
	else if(obj.y)
	    curtop += obj.y;
	return curtop;
}

function getElementXPath(elt) {
	var path = "";
	for (; elt && elt.nodeType == 1; elt = elt.parentNode) {
		idx = getElementIdx(elt);
		xname = elt.tagName;
		if (idx > 1)
			xname += "[" + idx + "]";
		path = "/" + xname + path;
	}

	return path;
}

function getElementIdx(elt)
{
    var count = 1;
    for (var sib = elt.previousSibling; sib ; sib = sib.previousSibling)
    {
        if(sib.nodeType == 1 && sib.tagName == elt.tagName)	count++
    }
    
    return count;
}

function getHostUrl(url) {
	var baseURL = url.substring(0, url.indexOf('/', 14));
	
    if (baseURL.indexOf('http://localhost') != -1) {
        // Base Url for localhost
        var url = location.href;  // window.location.href;
        var pathname = location.pathname;  // window.location.pathname;
        var index1 = url.indexOf(pathname);
       // var index2 = url.indexOf("/", index1 + 1);
        var baseLocalUrl = url.substr(0, index1);

        return baseLocalUrl + "/";
    }
    else {
        // Root Url for domain name
        return baseURL + "/";
    }
}

function getBaseUrl(url) {
	var baseURL = url.substring(0, url.indexOf('/', 14));
	
    if (baseURL.indexOf('http://localhost') != -1) {
        // Base Url for localhost
        var url = location.href;  // window.location.href;
        var pathname = location.pathname;  // window.location.pathname;
        var index1 = url.indexOf(pathname);
        var index2 = url.indexOf("/", index1 + 1);
        var baseLocalUrl = url.substr(0, index2);

        return baseLocalUrl + "/";
    }
    else {
        // Root Url for domain name
        return baseURL + "/";
    }
}

function qualifyURL(url){
    var img = document.getElementById(FRAME_ID).contentWindow.document.createElement('img');
    alert(document.getElementById(FRAME_ID).contentWindow.document.location);
    img.src = url; // set string url
    url = img.src; // get qualified url
    img.src = null; // no server request
    return url;
}
