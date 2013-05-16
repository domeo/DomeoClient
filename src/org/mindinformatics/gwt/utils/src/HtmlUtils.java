package org.mindinformatics.gwt.utils.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class HtmlUtils {

	public static final String ELEMENT_SPAN = "span";
	
	public static final String STANDALONE_FLAG = "standalone";
	
	public static final String IMAGE_ID = "imgId";
	public static final String ANNOTATION_ID = "annotationId";
	public static final String ANNOTATION_PART = "annotationPart";
	public static final String ANNOTATION_DEFAULT_CSS = "annotationDefCss";
	
	// -------------------------------------------------------------------------
	//  1. Retrieve not standard attributes
	// -------------------------------------------------------------------------
	/*
	 * Given a node it returns the attribute 'annotationid'
	 */
	public static String getAnnotationIdAttribute(Element node) {
		if(node!=null) return getNodeProperty(node, ANNOTATION_ID);
		else return "-1";
	};
	public static String getImageIdAttribute(Element node) {
		if(node!=null) return getNodeProperty(node, IMAGE_ID);
		else return "-1";
	};
	public static String getImageOriginalSourceAttribute(Element node) {
		if(node!=null) return getNodeProperty(node, "domeoOriginalSrc");
		else return "-1";
	};
	public static String getImageSourceAttribute(Element node) {
		if(node!=null) return getNodeProperty(node, "src");
		else return "-1";
	};
	
	/*
	 * Given a node it returns the attribute 'annotationPart'
	 */
	public static String getAnnotationPartAttribute(Element node) {
		return getNodeProperty(node, ANNOTATION_PART);
	};
	
	/*
	 * Given a node it returns the attribute 'annotationDefCssAttribute'
	 */
	public static String getAnnotationDefaultCssAttribute(Element node) {
		String att = getNodeProperty(node, ANNOTATION_DEFAULT_CSS);
		if(att == null) return "";
		return att;
	};
	
	//  Native methods
	// -------------------------------------------------------------------------
	/**
	 * Given a node it is returning the requested attribute.
	 */
	private static native String getNodeProperty(Element node, String attribute) /*-{
	    return node.getAttribute(attribute);
	}-*/;
	
	/**
	 * Given a node it is returning the requested attribute.
	 */
	private static native String getNodeSrc(Element node) /*-{
	    return node.src;
	}-*/;
	
	/**
	 * Returns all the elements with a given class name.
	 * 
	 * @param element	The root element
	 * @param className	The desired class name
	 */
	public static List<Element> getElementsByClassName(Document document, String className, boolean stopAtFirstOne) {
		return getElementsByClassNameFrom(document.getBody(), className, stopAtFirstOne);
	}

	/**
	 * Returns all child elements of the supplied parent that contain the
	 * supplied css class name.
	 * 
	 * @param parent 	Parent element
	 * @param name  	CSS class name
	 * @return list of Elements
	 */
	public static List<Element> getElementsByClassNameFrom(Element parent, String className, boolean stopAtFirstOne) {
		List<Element> elements = new ArrayList<Element>();
		if (parent != null) {
			int children = DOM.getChildCount((com.google.gwt.user.client.Element) parent);
			for (int i = 0; i < children; i++) {
				Element child = DOM.getChild((com.google.gwt.user.client.Element)parent, i);
				if (elementContainsClassName(child, className)) {
					elements.add(child);
					if(stopAtFirstOne) return elements;
				}
				elements.addAll(getElementsByClassNameFrom(child, className, stopAtFirstOne));
			}
		}
		return elements;
	}
	
	/**
	 * Utility method to determine if a given element contains the supplied CSS
	 * class name.
	 * 
	 * @param e		Element to check
	 * @param name  CSS class name
	 * @return true it the element contains the name
	 */
	public static boolean elementContainsClassName(Element e, String name) {
		boolean containsName = false;
		String classes = DOM.getElementProperty((com.google.gwt.user.client.Element)e, "className");
		if (classes != null && !"".equals(classes)) {
			String[] names = classes.split(" ");
			for (int i = 0; i < names.length; i++) {
				if (names[i].equals(name)) {
					containsName = true;
					break;
				}
			}
		}
		return containsName;
	}
	
	public static void flipElement(Element e, String annotationId, String selectorId, String elementId, String elementClass) {
		e.setId("annotation"+annotationId+":"+selectorId+"-part"+elementId);
		DOM.setElementAttribute((com.google.gwt.user.client.Element) e, "annotationId", annotationId+":"+selectorId);
		DOM.setElementAttribute((com.google.gwt.user.client.Element) e, "annotationdefcss", elementClass);
		DOM.setElementAttribute((com.google.gwt.user.client.Element) e, "class", elementClass);
		e.setClassName(elementClass);
	}
	
	public native static String normalizeSpaces(String string) /*-{
		return string.replace(/^\s*|\s(?=\s)|\s*$/g, "");
	}-*/;
	
	public native boolean isStorageEnabled() 
	/*-{
		if ($wnd.Modernizr.localstorage) {
		  return true;
		} else {
		  // no native support for HTML5 storage :(
		  // maybe try dojox.storage or a third-party solution
		  return false;
		}
	}-*/;
	
	public native static String getDocumentTitle(Document doc) /*-{
		return doc.title;
	}-*/;
	
	public static HashMap<String, String> getDocumentMetadata(Document doc) {
		HashMap<String, String> metadata = new HashMap<String, String>();
		NodeList<Element> meta = doc.getElementsByTagName("meta");
		for(int i = 0; i<meta.getLength(); i++) {
			if(meta.getItem(i).getAttribute("name")!=null && meta.getItem(i).getAttribute("name").trim().length()>0) 
				metadata.put(meta.getItem(i).getAttribute("name").toLowerCase(), normalizeSpaces(meta.getItem(i).getAttribute("content")));
		}
		return metadata;
	}
	
	public native static Element getElementById(String id) /*-{
		return $doc.getElementById(id);
	}-*/;
	
	public native static Element getElementById(Document doc, String id) /*-{
		return doc.getElementById(id);
	}-*/;
	
	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;
	
	public static native void addCssStylesheet(Document document, String css) /*-{
		try {
			var style = document.createElement('style');
			style.innerHTML = css;
			document.getElementsByTagName("head")[0].appendChild(style);
		} catch(e) {
			alert(e);
		}
	}-*/;
	
	public static native int findXPosition(Element obj) /*-{
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
	}-*/;
	
	public static native int findTopRightXPosition(Element obj) /*-{
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
	    return curleft + obj.offsetWidth;
	}-*/;
	
	public static native int findYPosition(Element obj) /*-{
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
	}-*/;
	
	public static native int findTopRightYPosition(Element obj) /*-{
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
	    return curtop + obj.offsetTop;
	}-*/;
	
	public static native String getElementXPath(Element obj) /*-{
		return $wnd.getElementXPath(obj);
	}-*/;
	
	// -------------------------------------------------------------------------
	//  4. Perform annotation highlight
	// -------------------------------------------------------------------------
	/**
	 * The method calls a javascript algorithm that performs the actual highlight
	 */
	public static native void performHighlight(String uid, String exact, String prefix, String suffix, Node node, String cssClass) /*-{	
		$wnd.highlightTextFromNode(
			exact, 
			prefix, 
			suffix, 
			uid, 
			node,
			cssClass);
	}-*/;	
	
	public static native void performAnnotation(String uid, String exact, String prefix, String suffix, Node node, String cssClass) /*-{	 
		$wnd.highlightTextFromNode(
			exact, 
			prefix, 
			suffix, 
			uid, 
			node,
			cssClass);
	}-*/;	
	
	// -------------------------------------------------------------------------
	//  5. Retrieve span by annotationId
	// -------------------------------------------------------------------------
//	public static Set<Element> getElementsByAnnotationId(Element frameElement, Long annotationId) {
//		IFrameElement iframe = IFrameElement.as(frameElement);
//		final Document frameDocument = iframe.getContentDocument();
//		
//		Set<Element> spans = new HashSet<Element>();
//		NodeList<Element> elements = frameDocument.getElementsByTagName(ELEMENT_SPAN);
//		for(int i=0; i<elements.getLength(); i++) {
//			String id = HtmlUtils.getAnnotationIdAttribute(elements.getItem(i));
//			if(id != null && id.trim().length()>0 && Long.parseLong(id)==annotationId) {
//				spans.add(elements.getItem(i));
//			}
//		}	
//		return spans;
//	}
//	
//	public static int getVerticalPositionOfElementWithId(Element frameElement, Long id) {
//		Set<Element> spans = getElementsByAnnotationId(frameElement, id); 
//		if(spans.size()>0) return spans.iterator().next().getAbsoluteTop();
//		else return -1;
//	}
//	
//	public static int getVerticalPositionOfElementWithId(Element frameElement, Element node, Long id) {
//		Set<Element> spans = getElementsByAnnotationId(frameElement, id); 
//		if(spans.size()>0) return spans.iterator().next().getAbsoluteTop();
//		else return -1;
//	}
	
	public static Set<Element> getElementsByAnnotationId(Element frameElement, String annotationId) {
		IFrameElement iframe = IFrameElement.as(frameElement);
		final Document frameDocument = iframe.getContentDocument();
		
		Set<Element> spans = new HashSet<Element>();
		NodeList<Element> elements = frameDocument.getElementsByTagName(ELEMENT_SPAN);
		for(int i=0; i<elements.getLength(); i++) {
			String id = HtmlUtils.getAnnotationIdAttribute(elements.getItem(i));
			if(id != null && id.trim().length()>0 && id.equals(annotationId)) {
				spans.add(elements.getItem(i));
			}
		}	
		return spans;
	}
	
	public static int getVerticalPositionOfElement(Element element) {
		return element.getAbsoluteTop();
	}
	
	public static int getVerticalPositionOfElementWithId(Element frameElement, String id) {
		Set<Element> spans = getElementsByAnnotationId(frameElement, id); 
		if(spans.size()>0) return spans.iterator().next().getAbsoluteTop();
		else return -1;
	}
	
	public static int getVerticalPositionOfElementWithId(Element frameElement, Element node, String id) {
		Set<Element> spans = getElementsByAnnotationId(frameElement, id); 
		if(spans.size()>0) return spans.iterator().next().getAbsoluteTop();
		else return -1;
	}
	
	public static native void scrollToElement(Element frameElement, Element element) /*-{
	    $wnd.scrollToDomElement(frameElement, element);
	}-*/;
	
	// Get all the annotations realated to a click
	// -------------------------------------------
	public static void getOnClickAnnotationElements(com.google.gwt.user.client.Element element, ArrayList<com.google.gwt.user.client.Element> spans, int max) {
		getTheMostOuterAnnotationSpan(element, spans, 0, max);
		//Window.alert("outer: " + spans.get(spans.size()-1).getInnerHTML() + "--" + spans.size());
		if(spans.size()>0) getInnerElements(spans.get(0), spans, true);
		//Window.alert("inner: " + spans.size());
	}
	
	public static Element getTheMostOuterAnnotationSpan(com.google.gwt.user.client.Element currentElement, ArrayList<com.google.gwt.user.client.Element> elements, int counter, int max) {
	    if(currentElement.getParentElement()==null) return null;
		Element e = null;
		if(counter++<max) {
			if (HtmlUtils.getAnnotationIdAttribute(currentElement) != null) {
				e = currentElement;
				elements.add(currentElement);
				Element ee = getTheMostOuterAnnotationSpan((com.google.gwt.user.client.Element) currentElement.getParentElement(), elements, counter, max);
				if (ee != null)
					e = ee;
			} else {
				e = currentElement;
				Element ee = getTheMostOuterAnnotationSpan((com.google.gwt.user.client.Element) currentElement.getParentElement(), elements, counter, max);
				if (ee != null)
					e = ee;
			}
		}
		return e;
	}
	
	public static void getInnerElements(com.google.gwt.user.client.Element element, ArrayList<com.google.gwt.user.client.Element> elements, boolean first) {
		if(element.getNodeName().equalsIgnoreCase("span") || element.getNodeName().equalsIgnoreCase("a")) {
			if(!first) elements.add(element);
			NodeList<Node> l = element.getChildNodes();
			for (int i = 0; i < l.getLength(); i++) {
				getInnerElements((com.google.gwt.user.client.Element) l.getItem(i), elements, false);
			}
		}
	}
	
	// --------------------------------------------
	
	public static void getOnClickAnnotationElements(Element element, ArrayList<Node> nodes) {
		Element outerSpan = getOuterSpan(element);
		getInnerElements(outerSpan, nodes);
	}
	
	public static Element getOuterSpan(Element element) {
		Element e = null;
		if (HtmlUtils.getAnnotationIdAttribute(element) != null) {
			e = element;
			Element ee = getOuterSpan(element.getParentElement());
			if (ee != null)
				e = ee;
		}
		return e;
	}
	
	public static void getInnerElements(Element element, ArrayList<Node> nodes) {
		if(element.getNodeName().equalsIgnoreCase("span") || element.getNodeName().equalsIgnoreCase("a")) {
			nodes.add(element);
			NodeList<Node> l = element.getChildNodes();
			for (int i = 0; i < l.getLength(); i++) {
				getInnerElements((Element) l.getItem(i), nodes);
			}
		}
	}
	
	public static void getInnerSpans(Element element, ArrayList<String> elements,
			ArrayList<Element> spans) {
		if (element.getNodeName().equalsIgnoreCase("span")
				&& HtmlUtils
						.getAnnotationIdAttribute((Element) element) != null) {
			elements.add(HtmlUtils
					.getAnnotationIdAttribute((Element) element));
			spans.add(element);
		}
		NodeList<Node> l = element.getChildNodes();
		for (int i = 0; i < l.getLength(); i++) {
			if (l.getItem(i).getNodeName().equalsIgnoreCase("span")) {
				if (HtmlUtils.getAnnotationIdAttribute((Element) l
						.getItem(i)) != null) {
					getInnerSpans((Element) l.getItem(i), elements, spans);
				}
			}
		}
	}
	
	public static int detectInnerSpans(Element element) {
		int counter = 0;
		NodeList<Node> l = element.getChildNodes();
		for (int i = 0; i < l.getLength(); i++) {
			if (l.getItem(i).getNodeName().equalsIgnoreCase("span")) {
				if (HtmlUtils.getAnnotationIdAttribute((Element) l
						.getItem(i)) != null) {
					counter++;
					counter += detectInnerSpans((Element) l.getItem(i));
				}
			}
		}
		return counter;
	}
	
	public static void removeSpansWithAnnotationId(Element frameElement, Long id) {
		String annotationId = id.toString();
		removeSpansWithAnnotationId(frameElement, annotationId);
	}
		
	public static void removeSpansWithAnnotationId(Element frameElement, String annotationId) {
		Set<Element> spans = HtmlUtils.getSpansWithAnnotationId(
				frameElement, annotationId);
		for (Element span : spans) {
			Element parent = (Element) span.getParentElement();

			NodeList<Node> nl = span.getChildNodes();
			Node[] ns = new Node[nl.getLength()];
			for (int i = 0; i < nl.getLength(); i++) {
				ns[i] = nl.getItem(i);
			}
			for (int j = 0; j < ns.length; j++) {
				parent.insertBefore(ns[j], span);
			}
			parent.removeChild(span);
		}
	}
	
	public static Set<Element> getSpansWithAnnotationId(Element frameElement, String annotationId) {
		IFrameElement iframe = IFrameElement.as(frameElement);
		final Document frameDocument = iframe.getContentDocument();
		
		Set<Element> spans = new HashSet<Element>();
		NodeList<Element> elements = frameDocument.getElementsByTagName(ELEMENT_SPAN);
		for(int i=0; i<elements.getLength(); i++) {
			if(HtmlUtils.getAnnotationIdAttribute(elements.getItem(i))!=null && 
					HtmlUtils.getAnnotationIdAttribute(elements.getItem(i)).equals(annotationId)) {
				spans.add(elements.getItem(i));
			}
		}
		return spans;
	}
	
	// -------------------------
	
	public static int getPrefixExactPostfixForNode(int lastIndex, String[] prefixExactPostfix, List<Node> textNodes, Element node, boolean stopAtEndOfParagraph) {
		prefixExactPostfix[1] = getNodeTextValue(node);
		return getPrefixPostfix3(lastIndex, textNodes, prefixExactPostfix, node, 100);
	}
	
	public static String[] getPrefixExactPostfixForNode(List<Node> textNodes, Element node) {
		String[] prefixExactPostfix = new String[3];
		prefixExactPostfix[1] = getNodeTextValue(node);
		getPrefixPostfix(textNodes, prefixExactPostfix, node, 100);
		return prefixExactPostfix;
	}
	
	public static String[] getPrefixExactPostfixForNode(Element node) {
		String[] prefixExactPostfix = new String[3];
		prefixExactPostfix[1] = getNodeTextValue(node);
		getPrefixPostfix(prefixExactPostfix, node, 100);
		return prefixExactPostfix;
	}
	
	public static native Node getFirstTextNode(Element node) /*-{
		var textNodes = $wnd.getTextNodes(node);
		if(textNodes.length>0)
			return textNodes[0];
		else return node;
	}-*/;
	
	public static native Node getLastTextNode(Element node) /*-{
		var textNodes = $wnd.getTextNodes(node);
		if(textNodes.length>0)
			return textNodes[textNodes.length-1];
		else return node;
	}-*/;
	
	public static native String getNodeTextValue(Element node) /*-{
		var textNodes = $wnd.getTextNodes(node);
		var buffer='';
		for(var i=0; i<textNodes.length; i++) {
			buffer+=(textNodes[i].nodeValue!=undefined?textNodes[i].nodeValue:'');
		}
		return buffer;
	}-*/;
	
	public static native Node getBodyNode() /*-{
		//alert($wnd.document.getElementById("domeoframe").contentWindow.document.getElementsByTagName("body").item(0));
		return $wnd.document.getElementById("domeoframe").contentWindow.document
		.getElementsByTagName("body").item(0)
	}-*/;
	
	public static String getPrefixPostfix(List<Node> textNodes, String[] prefixExactPostfix, Element node, int length) {
		Node firstNode = getFirstTextNode(node);
		Node lastNode = getLastTextNode(node);
		int counter = 0;
		int firstIndex = 0;
		for(Node textNode: textNodes) {
			if(textNode == firstNode) {
				firstIndex = counter;
			}
			if(textNode == lastNode) {
				break;
			}
			counter++;
		}

		// Detect prefix up to length
		String prefixBuffer = "";
		for(int prefixCounter = firstIndex-1; prefixCounter>0; prefixCounter--) {
			prefixBuffer = textNodes.get(prefixCounter).getNodeValue() + " " + prefixBuffer;
			if(prefixBuffer.length()>length) {
				prefixExactPostfix[0] = prefixBuffer.substring(prefixBuffer.length()-100, prefixBuffer.length()-1);
				break;
			}
		}
		
		String postFixBuffer = "";
		for(int postfixCounter = counter+1; postfixCounter<textNodes.size(); postfixCounter++) {
			postFixBuffer = postFixBuffer + " " + textNodes.get(postfixCounter).getNodeValue();
			if(postFixBuffer.length()>length) {
				prefixExactPostfix[2] = postFixBuffer;
				break;
			}
		}
		
		return ""+counter;
	}
	
	// This works with http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2700002/
	public static int getPrefixPostfix4(int lastIndex, List<Node> textNodes, String[] prefixExactPostfix, Element node, int length) {
		Node firstNode = getFirstTextNode(node);
		Node lastNode = getLastTextNode(node);

		int startIndex = 0;
		int firstIndex = 0;

		boolean startedFlag=false;
		boolean matchedFlag=false;
		
		String prefixBuffer = "";
		String postFixBuffer = "";
		GWT.log("----------------");
		GWT.log(">> "+node + " startIndex: " + lastIndex);
		for(int i= lastIndex; i<textNodes.size(); i++) {
			Node textNode = textNodes.get(i);
			if(!matchedFlag) {
				if(textNode == firstNode) {
					startIndex = i;
					startedFlag = true;
				}
				if(textNode == node.getFirstChild()) {
					GWT.log("Match "+textNode.getNodeValue() + " index: " + i);
					firstIndex = i;
					matchedFlag = true;
					continue;
				}
			}
			if(startedFlag && matchedFlag) {
				GWT.log("And");
				if(node.getParentNode()==textNode.getParentNode()) {
					postFixBuffer = postFixBuffer + " " + textNode.getNodeValue();
					GWT.log("Equal "+postFixBuffer);
				} else {
					break;
				}
			}
		}
		
		int LENGTH=20;
		for(int prefixCounter = firstIndex-1; prefixCounter>0; prefixCounter--) {
			GWT.log("test " + prefixCounter);
			prefixBuffer = textNodes.get(prefixCounter).getNodeValue() + " " + prefixBuffer;
			if(prefixBuffer.length()>LENGTH) {
				prefixExactPostfix[0] = prefixBuffer.substring(prefixBuffer.length()-LENGTH, prefixBuffer.length()-1);
				break;
			}
		}
		
		//for(int postfixCounter = counter+1; postfixCounter<textNodes.size(); postfixCounter++) {
		//	postFixBuffer = postFixBuffer + " " + textNodes.get(postfixCounter).getNodeValue();
			if(postFixBuffer.length()>length) {
				prefixExactPostfix[2] = postFixBuffer.substring(0, length);
				//break;
			} else {
				prefixExactPostfix[2] = postFixBuffer;
			}
			
			GWT.log("Pre: "+prefixExactPostfix[0] );
			GWT.log("Exact: "+prefixExactPostfix[1]);
			GWT.log("Post: "+prefixExactPostfix[2]);
			
		//}
		
		return (firstIndex+1);
	}
	
	
	// This works with http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2799499/
	public static int getPrefixPostfix3(int lastIndex, List<Node> textNodes, String[] prefixExactPostfix, Element node, int length) {
		Node firstNode = getFirstTextNode(node);
		Node lastNode = getLastTextNode(node);

		int startIndex = 0;
		int firstIndex = 0;

		boolean startedFlag=false;
		boolean matchedFlag=false;
		
		String prefixBuffer = "";
		String postFixBuffer = "";
		GWT.log("----------------");
		GWT.log(">> "+node + " startIndex: " + lastIndex);
		for(int i= lastIndex; i<textNodes.size(); i++) {
			Node textNode = textNodes.get(i);
			if(!matchedFlag) {
				if(textNode == firstNode) {
					startIndex = i;
					startedFlag = true;
				}
				if(textNode == node.getFirstChild()) {
					GWT.log("Match "+textNode.getNodeValue() + " index: " + i);
					firstIndex = i;
					matchedFlag = true;
					continue;
				}
			}
			if(startedFlag && matchedFlag) {
				GWT.log("And");
				if(node.getParentNode()==textNode.getParentNode()) {
					postFixBuffer = postFixBuffer + " " + textNode.getNodeValue();
					GWT.log("Equal "+postFixBuffer);
				} else {
					break;
				}
			}
		}
		
		
		// Try the same nod parent trick. Does not work for lists of citations.
		/*
		int LENGTH=10;
		for(int prefixCounter = firstIndex-1; prefixCounter>0; prefixCounter--) {
			GWT.log("test " + prefixCounter);
			prefixBuffer = textNodes.get(prefixCounter).getNodeValue() + " " + prefixBuffer;
			if(prefixBuffer.length()>LENGTH) {
				prefixExactPostfix[0] = prefixBuffer.substring(prefixBuffer.length()-LENGTH, prefixBuffer.length()-1);
				break;
			}
		}
		*/
		
		
		//for(int postfixCounter = counter+1; postfixCounter<textNodes.size(); postfixCounter++) {
		//	postFixBuffer = postFixBuffer + " " + textNodes.get(postfixCounter).getNodeValue();
			if(postFixBuffer.length()>length) {
				prefixExactPostfix[2] = postFixBuffer.substring(0, length);
				//break;
			} else {
				prefixExactPostfix[2] = postFixBuffer;
			}
			
			GWT.log("Pre: "+prefixExactPostfix[0] );
			GWT.log("Exact: "+prefixExactPostfix[1]);
			GWT.log("Post: "+prefixExactPostfix[2]);
			
		//}
		
		return (firstIndex+1);
	}
	
	public static String getPrefixPostfix2(List<Node> textNodes, String[] prefixExactPostfix, Element node, int length) {
		Node firstNode = getFirstTextNode(node);
		Node lastNode = getLastTextNode(node);
		int counter = 0;
		int firstIndex = 0;
		boolean started = false;
		boolean match = false;
		
		String postFixBuffer = "";
		
		for(Node textNode: textNodes) {
			if(textNode == firstNode) {
				started = true;
			}
//			if(started && textNode.getParentElement()==node.getParentElement()) {
//				firstIndex = counter;
//			}
			if(textNode.getNodeValue().equals(node.getInnerText())) {
				GWT.log("Match "+textNode.getNodeValue());
				firstIndex = counter-1;
				match = true;
				continue;
			}
			if(match) {
				postFixBuffer = postFixBuffer + " " + textNode.getNodeValue();
				GWT.log("Postfix " + postFixBuffer);
				GWT.log("Value "+ textNode.getNodeValue()+ "-"+textNode.getNodeValue().indexOf(".")+"-"+counter);
				if (textNode.getNodeValue().indexOf(".")>0 || textNode == lastNode) {
					GWT.log("Break");
					break;
				}
			}
			counter++;
		}

		// Detect prefix up to length
		String prefixBuffer = "";
		for(int prefixCounter = firstIndex-1; prefixCounter>0; prefixCounter--) {
			prefixBuffer = textNodes.get(prefixCounter).getNodeValue() + " " + prefixBuffer;
			if(prefixBuffer.length()>length) {
				prefixExactPostfix[0] = prefixBuffer.substring(prefixBuffer.length()-100, prefixBuffer.length()-1);
				break;
			}
		}
		
		//for(int postfixCounter = counter+1; postfixCounter<textNodes.size(); postfixCounter++) {
		//	postFixBuffer = postFixBuffer + " " + textNodes.get(postfixCounter).getNodeValue();
			if(postFixBuffer.length()>length) {
				prefixExactPostfix[2] = postFixBuffer.substring(0, length);
				//break;
			} else {
				prefixExactPostfix[2] = postFixBuffer;
			}
			
			GWT.log("Pre: "+prefixExactPostfix[0] );
			GWT.log("Match: "+prefixExactPostfix[1]);
			GWT.log("Post: "+prefixExactPostfix[2]);
			
		//}
		
		return ""+counter;
	}
	
	public static String getPrefixPostfix(String[] prefixExactPostfix, Element node, int length) {
		Node firstNode = getFirstTextNode(node);
		Node lastNode = getLastTextNode(node);
		List<Node> textNodes = HtmlTraversalUtils.getTextNodes(getBodyNode());
		int counter = 0;
		int firstIndex = 0;
		for(Node textNode: textNodes) {
			if(textNode == firstNode) {
				firstIndex = counter;
			}
			if(textNode == lastNode) {
				break;
			}
			counter++;
		}

		// Detect prefix up to length
		String prefixBuffer = "";
		for(int prefixCounter = firstIndex-1; prefixCounter>0; prefixCounter--) {
			prefixBuffer = textNodes.get(prefixCounter).getNodeValue() + " " + prefixBuffer;
			if(prefixBuffer.length()>length) {
				prefixExactPostfix[0] = prefixBuffer.substring(prefixBuffer.length()-100, prefixBuffer.length()-1);
				break;
			}
		}
		
		String postFixBuffer = "";
		for(int postfixCounter = counter+1; postfixCounter<textNodes.size(); postfixCounter++) {
			postFixBuffer = postFixBuffer + " " + textNodes.get(postfixCounter).getNodeValue();
			if(postFixBuffer.length()>length) {
				prefixExactPostfix[2] = postFixBuffer;
				break;
			}
		}
		
		return ""+counter;
	}
	
	public static Element createSpan(Element frameElement, Long id) {
		IFrameElement iframe = IFrameElement.as(frameElement);
		final Document frameDocument = iframe.getContentDocument();
		
		Element e = frameDocument.createElement(ELEMENT_SPAN);
		e.setClassName("assigned");
		e.setId("annotated_" + id);
		return e;
	}

	public static Element getImage(Element frameElement,String url, String xPath) {
		IFrameElement iframe = IFrameElement.as(frameElement);
		final Document frameDocument = iframe.getContentDocument();
		
		Element toReturn = null;
		NodeList<Element> images = frameDocument.getElementsByTagName("img");
		for(int i=0; i<images.getLength(); i++) {
			if(getImageOriginalSourceAttribute((Element)images.getItem(i)).equals(url)) {
				toReturn =  (Element)images.getItem(i);
				break;
			} else {
				if(getNodeSrc((Element)images.getItem(i)).equals(url)) {
					toReturn =  (Element)images.getItem(i);
					break;
				}
			}
		}
		return toReturn;
	}
}
