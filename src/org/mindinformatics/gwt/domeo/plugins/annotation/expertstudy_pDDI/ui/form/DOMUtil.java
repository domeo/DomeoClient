package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.form;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Some common DOM utility methods.
 * 
 * @author <a href="mailto:jasone@greenrivercomputing.com">Jason Essington</a>
 * @version $Revision: 0.0 $
 */
public class DOMUtil {

	/**
	 * Support class used to represent a NodeList in Java.
	 * 
	 * @author <a href="mailto:jasone@greenrivercomputing.com">Jason
	 *         Essington</a>
	 * @version $Revision: 0.0 $
	 */
	private static class NodeList {

		// holds the nodelist for access via JSNI
		private JavaScriptObject nodeList;

		/**
		 * Creates a new NodeList.
		 * 
		 * @param nodeListObject
		 *            NodeList returned from a native method
		 */
		public NodeList(JavaScriptObject nodeListObject) {
			this.nodeList = nodeListObject;
		}
	}
		public static final String HTML_ANCHOR = "A";

		public static final String HTML_DIV = "DIV";

		public static final String HTML_LIST_ITEM = "LI";

		public static final String HTML_FORM = "FORM";

		public static final String HTML_SELECT = "SELECT";

		public static final String HTML_UNORDERED_LIST = "UL";

		public static final int NODE_TYPE_ELEMENT = 1;

		public static final int NODE_TYPE_TEXT = 3;

		private static final String PROPERTY_NODE_NAME = "nodeName";

		/**
		 * GWT doesn't include any way to create a text node, so here's how it
		 * is done.
		 * 
		 * @param text
		 *            Raw text to make into an element
		 * @return Element object that may be inserted into DOM
		 */
		public static native Element createTextNode(String text) /*-{
			return $doc.createTextNode(text);
		}-*/;

		/**
		 * Utility method to determine if a given element contains the supplied
		 * CSS class name.
		 * 
		 * @param e
		 *            Element to check
		 * @param name
		 *            CSS class name
		 * @return true it the element contains the name
		 */
		public static boolean elementContainsClassName(Element e, String name) {
			boolean containsName = false;
			String classes = DOM.getElementProperty(e, "className");
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

		/**
		 * Checks the element to see if it is of the supplied type. For instance
		 * to see if an element is a select list call, elementIS(e, "SELECT").
		 * The actual comparison is case insensitive, but the nodeName property
		 * returns uppercase.
		 * 
		 * @param e
		 *            element to check
		 * @param type
		 *            node name to check against
		 * @return true if the element is a of the particular type.
		 */
		public static boolean elementIs(Element e, String type) {
			boolean match = false;
			if (type != null && e != null) {
				String nodeName = DOM.getElementProperty(e, PROPERTY_NODE_NAME);
				match = type.equalsIgnoreCase(nodeName);
			}
			return match;
		}

		/**
		 * Returns all elements from the document that contain the supplied css
		 * class name.
		 * 
		 * @param name
		 *            CSS class name
		 * @return list of Elements
		 */
		public static List getElementsByClassName(String name) {
			return getElementsByClassNameFrom(RootPanel.getBodyElement(), name);
		}

		/**
		 * Returns all child elements of the supplied parent that contain the
		 * supplied css class name.
		 * 
		 * @param parent
		 *            Parent element
		 * @param name
		 *            CSS class name
		 * @return list of Elements
		 */
		public static List<Element> getElementsByClassNameFrom(Element parent,
				String name) {
			List<Element> elements = new ArrayList<Element>();
			if (parent != null) {
				int children = DOM.getChildCount(parent);
				for (int i = 0; i < children; i++) {
					Element child = DOM.getChild(parent, i);
					if (elementContainsClassName(child, name)) {
						elements.add(child);
					}
					elements.addAll(getElementsByClassNameFrom(child, name));
				}
			}
			return elements;
		}

		/**
		 * Fetches the first child of the supplied parent if it is of the same
		 * node type.
		 * 
		 * @param parent
		 *            Element who's first child we want to fetch
		 * @param ntype
		 *            The node type that we are looking for
		 * @return element found, or null if it wasn't the proper type or there
		 *         were no children
		 */
		public static native Element getFirstChildIfType(Element parent,
				int ntype)/*-{
			var child = parent.firstChild;
			return (child != null && child.nodeType == ntype) ? child : null;
		}-*/;

		/**
		 * Native call to get elements by tag name from the document.
		 * 
		 * @param parent
		 *            Element where the search for nodes should begin
		 * @param name
		 *            tag name
		 * @return NodeList as a JavaScriptObject
		 */
		private static native JavaScriptObject nativeGetElementsByTagNameFrom(
				Element parent, String name)/*-{
			var nodeList = parent.getElementsByTagName(name);
			return nodeList == null ? null : nodeList;
		}-*/;

		/**
		 * Hide default constructor.
		 */
		protected DOMUtil() {
		}
	}

