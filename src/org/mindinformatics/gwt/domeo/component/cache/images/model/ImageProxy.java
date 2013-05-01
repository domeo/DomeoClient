package org.mindinformatics.gwt.domeo.component.cache.images.model;

import com.google.gwt.dom.client.Element;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ImageProxy {

	private String src;
	private String width;
	private String height;
	private String xpath;
	private String title;
	private Element image;
	
	public ImageProxy(String src, String width, String height, String title, Element image) {
		this.src = src;
		this.width = width;
		this.height = height;
		this.image = image;
		this.title = title;
	}

	public String getSrc() {
		return src;
	}

	public String getWidth() {
		return width;
	}

	public String getHeight() {
		return height;
	}

	public String getXpath() {
		return xpath;
	}

	public Element getImage() {
		return image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
