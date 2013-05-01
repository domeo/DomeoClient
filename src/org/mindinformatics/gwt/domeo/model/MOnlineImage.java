package org.mindinformatics.gwt.domeo.model;

import com.google.gwt.user.client.Element;


@SuppressWarnings("serial")
public class MOnlineImage extends MOnlineResource {

	private String xPath;
	
	transient private String localId;
	transient private Element image;
	
	public String getXPath() {
		return xPath;
	}

	public void setXPath(String xPath) {
		this.xPath = xPath;
	}

	public Element getImage() {
		return image;
	}

	public void setImage(Element image) {
		this.image = image;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}
}
