function highlightTextFromNode(exact, prefix, suffix,
	titleAttributeOrSpanCreation, node, cssClass) {
	// TODO Optimize - the node would allow to be faster without
	// having to parse the whole DOM for each annotation.
	// However the right node has to come from outside.
	//textNodes = getTextNodes(node.parentNode);
	textNodes = getTextNodes();

	//alert(prefix.replace(/\\n/g,'\n').replace(/\\"/g,'"'));
	
	var foundText = findText(exact.replace(/\\n/g,'\n').replace(/\\"/g,'"').replace(/\&amp;/g,'&'), prefix.replace(/\\n/g,'\n').replace(/\\"/g,'"').replace(/\&amp;/g,'&'), suffix.replace(/\\n/g,'\n').replace(/\\"/g,'"').replace(/\&amp;/g,'&'), textNodes);
	if (foundText == null || foundText.length == 0) {
		throw('The text:"'+exact+'" was not found ' + prefix + '/' + suffix);
		/*
		console.debug
				&& console.debug("Text not found:"
						+ [ prefix, exact, suffix ].join(""));
		*/
		return;
	}
	if (textNodes) {
		_highlightText(textNodes, foundText[0], foundText[1],
				titleAttributeOrSpanCreation, node.parentNode, cssClass);
	} else {
		alert("Problem in highlighting");
	}
}

/*
 * return array with all the textnodes starting from the top node in the
 * document (body by default)
 */
/* [0] contains array with all the text strings in the document, split by node */
/* [1] contains array with all the text nodes */
function getTextNodes(topNode) {
	if (!topNode) { // Gets all the text nodes in the IFrame
		topNode = document.getElementById(FRAME_ID).contentWindow.document
				.getElementsByTagName("body").item(0);
	}
	!topNode && console
			&& console.error('Could not find the body of the document')
	var textNodes = [];
	traverseDomTree_recurse(topNode, 0, function(node) {
		if (node.nodeName == "#text") {
			textNodes.push(node);
		}
	});
	return textNodes;
}

	function getTexts(){
  		var body_element = document.getElementById(FRAME_ID).contentWindow.document.getElementsByTagName("body").item(0);
  		  var textPieces = [];
  		  
  		  traverseDomTree_recurse(body_element, 0,function(node){if(node.nodeName =="#text"){textPieces.push(node.data);}});
  		  return textPieces.join("");
  		}

function traverseDomTree_recurse(curr_element, level, block) {
	var i;
	if (curr_element.childNodes.length <= 0) {
		// This is a leaf node.
		block(curr_element);
	} else {
		// Expand each of the children of this node.
		for (i = 0; curr_element.childNodes.item(i); i++) {
			traverseDomTree_recurse(curr_element.childNodes.item(i), level + 1,
					block);
		}
	}
}

RegExp.escape = function(str) {
	try {
		var specials = new RegExp("[.*+?|()\\[\\]{}\\\\$]", "g"); // .*+?|()[]{}$\ 
		return str.replace(specials, "\\$&");
	} catch(e) {
		alert(e);
	}
};

/* Looks for matching text within the body of the current document */
/* all params are optional except exact */
/* Return array with textoffset and matching text */
/* Return null of not found */
function findText(exact, prefix, suffix, textNodes) {

	if (!textNodes) {
		textNodes = getTextNodes();
	}
	// var textPieces = textObjects[0];
	var textPieces = [];
	for ( var i = 0; i < textNodes.length; i++) {
		textPieces.push(textNodes[i].data);
	}
	var allTheText = textPieces.join("");
	var whitespaceMatchExpr = "\\s+";
	// var whitespaceMatchExpr = "e";
	// alert(searchTerm.search(new RegExp(whitespaceMatchExpr,'g')));
	// var regexedSearchTerm = searchTerm.replace(new
	// RegExp(whitespaceMatchExpr),whitespaceMatchExpr);
	exact = RegExp.escape(exact);
	prefix && (prefix = RegExp.escape(prefix));
	suffix && (suffix = RegExp.escape(suffix));
	var terms = [ prefix, exact, suffix ];
	var regexSearchTerm = "";
	var buildRegexSearchString = function(rawString) {
		return rawString.replace(/\s+/g, "\\s*");
	};

	try {
		for ( var i = 0; i < terms.length; i++) {
			terms[i]
					&& (regexSearchTerm += ('(' + buildRegexSearchString(terms[i]) + ')'));
		}
	} catch (e) {
		alert("Invalid build regex:" + e);
		// alert('The regular expression was invalid:'+regexSearchTerm);
	}
	// console.debug("Initial regexSearchTerm:"+regexSearchTerm);
	var preliminaryMatch = null;
	try {
		preliminaryMatch = allTheText.match(new RegExp(regexSearchTerm));
	} catch (e) {
		alert("Invalid regex:" + regexSearchTerm);
		// alert('The regular expression was invalid:'+regexSearchTerm);
	}
	if (!preliminaryMatch) {
		return null;
	}
	// var exactMatchIdx = prefix ? 2 : 1;
	var pos = allTheText.indexOf(preliminaryMatch[0])
			+ (prefix ? preliminaryMatch[1].length : 0);
	var match = preliminaryMatch[prefix ? 2 : 1];
	// console.debug("the pos="+pos);

	// console.debug("the match=*"+match+"*");
	return [ pos, match ];
}

function findSpanChildren(matchTextNodes){
	   var matchInfos = [];
	   var textNodesToLookFor = matchTextNodes.slice();
	   try {
		   do{
			   //console.debug("Looking for "+debugString(textNodesToLookFor));
			   var currentMatchInfo = findCommonParent(textNodesToLookFor);
			  
			   currentMatchInfo && matchInfos.push(currentMatchInfo);
			   //console.debug('sliceParams='+(currentMatchInfo.lastTextNodeMatchedIndex+1)+","+textNodesToLookFor.length);
			   textNodesToLookFor = textNodesToLookFor.slice(currentMatchInfo.lastTextNodeMatchedIndex + 1,textNodesToLookFor.length);
			   
		   } while(currentMatchInfo && textNodesToLookFor.length);
	   } catch (e) {
		   /*
		   alert(textNodesToLookFor);
		   alert(textNodesToLookFor.length);
		   */
		   var text = "";
		   for(var i=0; i<textNodesToLookFor.length; i++ ) {
			   text += textNodesToLookFor[i].textContent + " ";
		   }
		   alert(textNodesToLookFor + " =====" + text + " =====" + e);
	   }
	   
	   return matchInfos;
	   
}

function _highlightText(textNodes, pos, match, titleAttributeOrSpanCreation, commonParent, cssClass) {
	
	try {
		var matchNodes = findTextNodesWithNodeSplitIfNeeded(textNodes,pos,match);
	
		/*
		var matchingNodeString = "";
	    if(console.debug){
		    for(var k=0; k< matchNodes.length; k++){
			    matchingNodeString += "["+matchNodes[k].data+"]";
		    }
	    }
	    */
		
		//alert(match + " - " + matchNodes.length);
	    
	    var matchInfos = findSpanChildren(matchNodes);
	    for(var i = 0; i < matchInfos.length; i++){
	
			  var matchInfo = matchInfos[i];
			  var commonParent = null;
			  matchInfo && (commonParent = matchInfo.commonParent);
			  if(!(matchInfo && (commonParent = matchInfo.commonParent))){
				  console.debug("Could not find nodes");
			  }
		    var children = commonParent.childNodes;
		    var childrenCopy = [];
		    for(var j = 0; j < children.length;j++){
				childrenCopy.push(children[j])
			}
		    
		    var spanNode = createHighlightSpan(titleAttributeOrSpanCreation, i, cssClass);
		    
		    var lastTextNode = null;
			var firstNodeFound = false;
			var lastNodeFound = false;
			for(var j = 0; j < childrenCopy.length && !lastNodeFound;j++){
				var currentNode = childrenCopy[j];
				if(!firstNodeFound){
					if(currentNode != matchInfo.firstNode){
						 continue;
					 }else{
						 firstNodeFound = true;
					 }  
				}
				
				lastNodeFound = (currentNode == matchInfo.lastNode);
		
				if(firstNodeFound && !spanNode.parentNode){
					commonParent.insertBefore(spanNode,currentNode);
				}
				//console.debug("Adding to span:"+debugString(currentNode));
				spanNode.appendChild(currentNode);	  
			}
	    }
	} catch (e) {
		alert(e);
	}
		//if(1==1)return;
		//currentPos += textNodes[i].length;
}

function createHighlightSpan(id, part, cssClass){
	var spanNode = document.createElement("span");
	spanNode.setAttribute('class', cssClass);
	id && spanNode.setAttribute('id','annotation'+id+'-part'+part);
	spanNode.setAttribute('annotationId',''+id);
	spanNode.setAttribute('annotationDefCss',''+cssClass);
	spanNode.setAttribute('annotationPart',''+part);
	return spanNode;
	//spanNode.parentNode = childNode.parentNode;
	//spanNode.parentNode.removeChild(childNode);
	//spanNode.appendChild(childNode);
}

function findTextNodesWithNodeSplitIfNeeded(textNodes,pos,match){
	var currentPos = 0;
	var i;
	var textNodeEndPos = 0;
	for(i = 0; i < textNodes.length;  i++){
		//Move in order through all the documents text to find the start of the search text
		textNodeEndPos = textNodes[i].length + currentPos;
		  
		//Not there yet. Save current position and go on to the next node
		if(textNodeEndPos < pos){
			currentPos = textNodeEndPos;
			continue;
		}
		if(textNodeEndPos > pos){
			break;
		}else{ 
			currentPos = textNodeEndPos;
		}
	}
	  
	var matchNodes = [];	
	
	//position of match in relative to the first node
	//if needed, split the current text node so that the beginning of our text
	//starts in its own text node
	var matchPosInFirstNode = pos -currentPos;
	if(pos > 0){
		var theText = textNodes[i].data;
		var oldNodeContents = theText.substring(matchPosInFirstNode);
		var newNode = null;
		if(oldNodeContents === theText){
			// newNode = textNodes[i];
		}else{
			newNode =  document.createTextNode(theText.substring(0,matchPosInFirstNode));
			textNodes[i].data = oldNodeContents;
			textNodes[i].parentNode.insertBefore(newNode,textNodes[i]);
			textNodes.splice(i,0,newNode);
			++i;
		} 
		currentPos = currentPos + matchPosInFirstNode;
	}
			  
	var matchPosInLastNode = -1;
	//find the last text node. If needed, split the last text node to contain only our text
	for(var j = i; j < textNodes.length; j++){
		currentPos += textNodes[j].length;
		matchNodes.push(textNodes[j]);
		if(currentPos >=pos + match.length){
			matchPosInLastNode = (pos + match.length) -(currentPos - textNodes[j].data.length);
			//this is a test
			var theText = textNodes[j].data;
			var newTextNodeContents = theText.substring(0,matchPosInLastNode);
			var newNode = null
			if(theText === newTextNodeContents){
				newNode = textNodes[j]
			}else{
				newNode = document.createTextNode(newTextNodeContents);
				textNodes[j].data = theText.substring(matchPosInLastNode,theText.length);
				textNodes[j].parentNode.insertBefore(newNode,textNodes[j]);
				textNodes.splice(j,0,newNode);
			}
			matchNodes.pop();
			matchNodes.push(newNode);
			break;
		}
	}
	return matchNodes;
}

//Try to find the nodes that can be included in one span to highlight the most number of specified text nodes
//Note that we are assuming that these text nodes are contiguous in the flow of the document tree
function findCommonParent(matchTextNodes){
		  if(matchTextNodes && matchTextNodes.length){
			  //console.debug("finding commonParent for textnodes"+debugString(matchTextNodes));
			  
			  //console.debug("common parent to check first:"+debugString(currentParent));
			  var previousParentResults = null;
			  var currentParentResults = null;
			  //We will start looking at the direct parentNode of the first text node
			  var currentParent = matchTextNodes[0].parentNode;
			  for(var i = 0; i < 10 && currentParent; i++){
				  //console.debug("checking common parent:"+debugString(currentParent));
				  var children = currentParent.childNodes;
				  var parentFound = false;
				  var firstTextNodeFound = false;
				  var lastTextNodeFound = false;
				  var invalidChildReached = false;
				  var k = -1;
				  var firstNode = null;
				  var lastNode = null;
				  var currentChild = null;
				  
				  for(var j = 0; j < children.length && !lastTextNodeFound; j++){
					  currentChild = children[j];
					  //console.debug("Checking child node:"+debugString(currentChild));
					  var thisNodeTextNodes = getTextNodes(currentChild);
					  
					  //Have we found the first node?
					  if(!firstTextNodeFound){
						  firstTextNodeFound = thisNodeTextNodes && (thisNodeTextNodes[0] == matchTextNodes[0]);
						  if(firstTextNodeFound){
							  k =0;
							  firstNode = currentChild;
							  //console.debug("The first node was found="+debugString(currentChild));
						  }else{
							  continue;
						  }
					  }
					  //Not any element can be added within a span. Check if this is one of the bad ones
					  if(firstTextNodeFound && !isValidSpanChild(currentChild) || (thisNodeTextNodes.length + k > matchTextNodes.length)){
						  //console.debug("Reached a node that cannot be added to a span: "+debugString(currentChild));
						  invalidChildReached = true;
						  
						  //Just in case we did find some nodes before. This is as far as we can go
						  lastNode = currentChild.previousSibling;
						  break;
					  }
					 
					  //How many of the text nodes have we found
					  for(var l = 0; l < thisNodeTextNodes.length; l++){
						  if(matchTextNodes[k] == thisNodeTextNodes[l]){
							  //return here
							  ++k;
							  if(k == matchTextNodes.length){
								  lastTextNodeFound = true;
								  lastNode = currentChild;
								  break;
							  }
							  //All the text nodes for this node are good so this could be the lastNode we includ
							  if(l == thisNodeTextNodes.length -1){
								  lastNode = currentChild;
							  }
							  
						  }else{
							  //console.debug("The currentChild:"+debugString(currentChild)+" should have a matching node but it does not");
							  return null;
						  }
						                                         
					  }
				  }
				  currentParentResults = {commonParent:currentParent,firstNode:firstNode,lastNode: lastNode,lastTextNodeMatchedIndex: k -1};
				  //console.debug("Finished level:"+i+"currentParent="+debugString(currentParent)+"firstNode="+debugString(firstNode)+"lastNode="+debugString(lastNode)+",lastTextNodeMatchedIndex="+(k -1)+",invalidChildReached="+invalidChildReached);
				  var gotAllNodes = currentParentResults.lastTextNodeMatchedIndex == (matchTextNodes.length -1);
				  if(!gotAllNodes){
					  //console.debug("I did not get all the nodes");
					  
					  if(!previousParentResults || (previousParentResults.lastTextNodeMatchedIndex < currentParentResults.lastTextNodeMatchedIndex)){
						  //console.debug(previousParentResults ? ("the matchedTextIndex previous="+previousParentResults.lastTextNodeMatchedIndex+",current="+currentParentResults.lastTextNodeMatchedIndex) : "no previousresults,currentResults="+currentParentResults.lastTextNodeMatchedIndex);
						  previousParentResults = currentParentResults;
						  //console.debug("theCurrentParent="+debugString(currentParent));
						  currentParent = currentParent.parentNode;
						  //console.debug("Updating currentParent to the parent:"+debugString(currentParent));
					  }else if(previousParentResults && ((previousParentResults.lastTextNodeMatchedIndex > currentParentResults.lastTextNodeMatchedIndex) || invalidChildReached)){
						  //console.debug("return previousParentResults");
						  return previousParentResults;
					  }
				  }else if(gotAllNodes){
					 //console.debug("i got all the nodes");
					 //console.debug("the value of the expression="+((currentParentResults.lastTextNodeMatchedIndex < (matchTextNodes.length -1)) && !invalidChildReached));
					 //console.debug("currentParentResults.lastTextNodeMatchedIndex < (matchTextNodes.length -1)="+(currentParentResults.lastTextNodeMatchedIndex < (matchTextNodes.length -1))+",!invalidChildReached="+(!invalidChildReached));
					 //console.debug("The parent node is"+ debugString(currentParent));
					 //console.debug("FirstNode:"+debugString(firstNode)+",LastNode:"+debugString(lastNode));
					 return currentParentResults;
					//Hooray!  
				  }
				  
			  }
			  // Added on Nov 2011 (not sure yet)
			  return currentParentResults;
		  }else{
			  return null;
		  }
}

function isValidSpanChild(element){
	   if (element.nodeName == '#text'){
		   return true;
	   }
	   var validTagNames = ['span','a', 'b','abbr', 'em', 'small', 'strong', 'sub', 'sup','i', 'img','acronym',  'bdo', 'big', 'br', 'button', 'cite', 'code', 'del', 'dfn',  'input', 'ins', 'kdb', 'label', 'map', 'object', 'q', 'samp', 'script', 'select',  'textarea', 'tt', 'var' ];
	   for(var i = 0; i < validTagNames.length; i++){
		   if(element.tagName && element.tagName.toLowerCase() == validTagNames[i]){
			   return true;
		   }
	   }
	   return false;
	   
}

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