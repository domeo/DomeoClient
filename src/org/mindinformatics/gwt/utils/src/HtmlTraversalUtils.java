package org.mindinformatics.gwt.utils.src;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Window;

public class HtmlTraversalUtils {

	//-------------------------
	//   RETRIEVE TEXT NODES
	//-------------------------
	public List<Node> getTextNodes(Document doc) {
		return getTextNodes(doc.getBody());
	}

	public static List<Node> getTextNodes(Node rootNode) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		recursiveTraversalDomTreeForNodes(rootNode, 0, nodes);
		return nodes;
	}
	
	public static void recursiveTraversalDomTreeForNodes(Node currentNode, int level, ArrayList<Node> nodes) {
		if(currentNode.getChildNodes().getLength() <=0) {
			if(currentNode.getNodeType() == Node.TEXT_NODE) {
				nodes.add(currentNode);
			}
		} else {
			for(int i=0; i<currentNode.getChildNodes().getLength(); i++) {
				recursiveTraversalDomTreeForNodes(currentNode.getChildNodes().getItem(i), level+1, nodes);
			}
		}
	}
	
	//-------------------------
	//   RETRIEVE TEXT NODES
	//-------------------------
	public static boolean verifyPresenceAsSubnode(Node rootNode, Node toVerify) {
		return recursiveVerifyPresenceAsSubnode(rootNode, toVerify);
	}
	
	public static boolean recursiveVerifyPresenceAsSubnode(Node currentNode, Node toVerify) {
		//GWT.log("##1## " + currentNode + " - " + toVerify, null);
		for(int i=0; i<currentNode.getChildNodes().getLength(); i++) {
			//printNode(currentNode.getChildNodes().getItem(i), "A " + i + "" );
			//printNode(toVerify, "B ");
			if(currentNode.getChildNodes().getItem(i) == toVerify) {
				//GWT.log("#### " + currentNode, null);
				return true;
			} else {
				//GWT.log("$$$$ " + getPrintNode(currentNode.getChildNodes().getItem(i)) + " - " + getPrintNode(toVerify), null);
				boolean res = recursiveVerifyPresenceAsSubnode(currentNode.getChildNodes().getItem(i), toVerify);
				if(res) return true;
			}
		}

		return false;
	}
	
	public static String getPrintNode(Node node) {
		if(node.getNodeType()==Node.TEXT_NODE)  return node.getNodeValue();
		else return ((Element)node).getInnerHTML();
	}
	
	public static void printNode(Node node, String prefix) {
		if(node.getNodeType()==Node.TEXT_NODE)  GWT.log("##T##" + prefix + node.getNodeValue(), null);
		else GWT.log("##N##" + prefix + ((Element)node).getInnerHTML(), null);
	}
	
/*	
	public static List<Node> getTextNodes(Node rootNode) {
		GWT.log("???? entry", null);
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.addAll(recursiveTraversalDomTreeForNodes(rootNode, 0));
		GWT.log("???? exitSize " + nodes.size(), null);
		return nodes;
	}
	
	public static List<Node> recursiveTraversalDomTreeForNodes(Node currentNode, int level) {
		GWT.log("???? recursion", null);
		ArrayList<Node> localNodes = new ArrayList<Node>();
		if(currentNode.getChildNodes().getLength() <=0) {
			if(currentNode.getNodeType() == Node.TEXT_NODE) {
				localNodes.add(currentNode);
				GWT.log("???? leaf", null);
			}
		} else {
			for(int i=0; i<currentNode.getChildNodes().getLength(); i++) {
				localNodes.addAll(recursiveTraversalDomTreeForNodes(currentNode.getChildNodes().getItem(i), level+1));
				GWT.log("???? localNodesSizeInternal " + localNodes.size(), null);
			}
		}
		GWT.log("???? localNodesSize " + localNodes.size(), null);
		return localNodes;
	}
	*/
	
	//-----------------------------------
	//   RETRIEVE TEXT CONTENT TO NODE
	//-----------------------------------
	public String getTextContentToNode(Node targetNode) {
		Node topNode = getTopNode(targetNode);
		ArrayList<String> text = new ArrayList<String>();
		traverseToTarget(text, topNode, targetNode);
		String prefixBuffer = "";
		if(text.size()>0) {
			for(int i=text.size()-1; i>0; i--) {
				prefixBuffer = text.get(i).trim() + " " + prefixBuffer;
				if(prefixBuffer.length()>30) break; 
			}
		}
		return prefixBuffer.toString();
	}
	
	public Node getTopNode(Node targetNode) {
		Node parent = targetNode.getParentElement();
		if(parent.getNodeName().toLowerCase().equals("div")) return parent;
		return getTopNode(parent);
	}
	
	public boolean traverseToTarget(ArrayList<String> content, Node currentNode, Node targetNode) {
		if(currentNode.equals(targetNode)) return true;
		if(currentNode.getChildNodes().getLength() <=0) {
			if(currentNode.getNodeType() == Node.TEXT_NODE) {
				content.add(currentNode.getNodeValue().trim());
			}
			return false;
		} else {
			for(int i=0; i<currentNode.getChildNodes().getLength(); i++) {
				boolean ret = traverseToTarget(content, currentNode.getChildNodes().getItem(i), targetNode);
				if(ret) return true;
			}
		}
		return false;
	}

	public String getTextContentFromNode(Node targetNode) {
		Node topNode = getTopNode(targetNode);
		StringBuffer text = new StringBuffer();
		traverseFromTarget(text, topNode, targetNode, new ArrayList<Node>());
		if(text.length()>30) return text.toString().substring(0,30);
		else return text.toString();
	}
	
	public void traverseFromTarget(StringBuffer content, Node currentNode, Node targetNode, ArrayList<Node> splitNode) {
		if(currentNode.getChildNodes().getLength() <=0) {
			if(splitNode.size()>0 && currentNode.getNodeType() == Node.TEXT_NODE) {
				content.append(currentNode.getNodeValue());
				if(content.length()>30) splitNode.clear();
			}
			if(currentNode.equals(targetNode)) {
				splitNode.add(targetNode);
			}
		} else {
			for(int i=0; i<currentNode.getChildNodes().getLength(); i++) {
				traverseFromTarget(content, currentNode.getChildNodes().getItem(i), targetNode, splitNode);
			}
		}
	}
	
	//---------------------------
	//   RETRIEVE TEXT CONTENT
	//---------------------------
	public List<String> getTextContent(Document doc) {
		return getTextContent(doc, doc.getBody());
	}
	
	public List<String> getTextContent(Document doc, Node rootNode) {
		ArrayList<String> content = new ArrayList<String>();
		recursiveTraversalDomTreeForContent(rootNode, 0, content);
		return content;
	}
	
	public void recursiveTraversalDomTreeForContent(Node currentNode, int level, ArrayList<String> content) {
		if(currentNode.getChildNodes().getLength() <=0) {
			if(currentNode.getNodeType() == Node.TEXT_NODE) {
				content.add(currentNode.getNodeValue());
			}
		} else {
			for(int i=0; i<currentNode.getChildNodes().getLength(); i++) {
				recursiveTraversalDomTreeForContent(currentNode.getChildNodes().getItem(i), level+1, content);
			}
		}
	}
}
