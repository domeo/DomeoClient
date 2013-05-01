package org.mindinformatics.gwt.domeo.model.buffers;

import com.google.gwt.dom.client.Node;

public class HighlightedTextBuffer {
	String text, exact, prefix, suffix;
	Node node;
	
	public HighlightedTextBuffer(String exact, String prefix, String suffix, Node node) {
		this.exact = exact;
		this.prefix = prefix;
		this.suffix = suffix;
		this.node = node;
	}
	
	public String getExact() {
		return exact;
	}
	public String getPrefix() {
		return prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public Node getNode() {
		return node;
	}
	public void setExact(String exact) {
		this.exact = exact;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
