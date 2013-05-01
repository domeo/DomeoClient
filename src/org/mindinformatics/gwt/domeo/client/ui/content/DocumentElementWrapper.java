package org.mindinformatics.gwt.domeo.client.ui.content;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class DocumentElementWrapper extends Widget implements HasMouseUpHandlers, HasClickHandlers {

    public DocumentElementWrapper(Element theElement) {
        setElement(theElement);
    }
    
    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler)  {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		 return addDomHandler(handler, ClickEvent.getType());
	}
	
    @Override
    public void onAttach() { super.onAttach(); }
    
    @Override
    public void onLoad() { super.onLoad(); }
}
