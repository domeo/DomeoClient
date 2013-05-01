package org.mindinformatics.gwt.framework.widget;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ButtonWithIcon extends Button {
	private String text;
	private String heightIcon;

	public ButtonWithIcon() {
	}
	
	public ButtonWithIcon(String style) {
		this.setStyleName(style);
	}
	
	public void setResource(ImageResource imageResource) {
		Image img = new Image(imageResource);
		String definedStyles = img.getElement().getAttribute("style");
		img.getElement().setAttribute("style",
				definedStyles + "; vertical-align:top;");
		img.setHeight((this.heightIcon!=null)?(this.heightIcon+"px"):"14px");
		DOM.insertBefore(getElement(), img.getElement(), DOM
				.getFirstChild(getElement()));
	}

	@Override
	public void setText(String text) {
		this.text = text;
		Element span = DOM.createElement("span");
		span.setInnerText(text);
		span.setAttribute("style", "padding-left:3px; vertical-align:top;");

		DOM.insertChild(getElement(), span, 0);
	}

	@Override
	public String getText() {
		return this.text;
	}
	
	public void setHeightIcon(String heightIcon) {
		this.heightIcon = heightIcon;
	}
}

