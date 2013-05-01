package org.mindinformatics.gwt.domeo.client.ui.popup;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.link.LLinkCard;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CurationPopup extends PopupPanel {

	public static final CurationPopupResources localResources = 
		GWT.create(CurationPopupResources.class);
	
	private IDomeo _domeo;
	private CurationPopup _this;
	
	private ArrayList<MAnnotation> currentItems;
	private ArrayList<Element> currentSpans;
	private List<String> currentIds;
	
	DateTimeFormat fmt = DateTimeFormat.getFormat("MMMM dd, yyyy");
	private ScrollPanel sp = new ScrollPanel();
	private VerticalPanel vp = new VerticalPanel();
	
	public CurationPopup(IDomeo domeo) {
		super(true, true);
		
		_domeo = domeo;
		_this = this;

		localResources.popupCss().ensureInjected();
		this.setStyleName(localResources.popupCss().popupPanel());

		sp.setStyleName(localResources.popupCss().scrollPanel());
		sp.add(vp);
		this.setAnimationEnabled(true);
		this.setWidget(sp);
	}
	
	public void initialize() {
		vp.clear();
	}
	
	public void showLink(Element link, int x, int y) {
		LLinkCard linkCard = new LLinkCard(_domeo, link);
		vp.add(linkCard);
		
		this.setPopupPosition(x, y);
		this.show();
	}
	
	public void showAnnotation(ArrayList<MAnnotation> annotationItems,
			ArrayList<Element> spans, int x, int y, int maxHeight) {
		//sp.setSize("400px", maxHeight+"px");
		for(int i=0; i<annotationItems.size(); i++) {
			MAnnotation annotationItem = annotationItems.get(i);
			ACardComponent card = _domeo.getAnnotationCardsManager().getAnnotationCard(annotationItem.getClass().getName());
			if(card!=null) {
				if (spans.size()>1) card.initializeCard(i, this, annotationItem);
				else card.initializeCard(this, annotationItem);
				card.setSpan(spans.get(i));
				vp.add(card);
			} else {
				Window.alert("Card not implemented for: " + annotationItem.getClass().getName());
			}
		}
		this.setPopupPosition(x, y);
		this.show();
	}
	
	public void show(Element link, ArrayList<MAnnotation> annotationItems,
			ArrayList<Element> spans, int x, int y, int maxHeight) {
		//sp.setSize("400px", maxHeight+"px");
		Window.alert(""+annotationItems.size());
		for(int i=0; i<annotationItems.size(); i++) {
			MAnnotation annotationItem = annotationItems.get(i);
			ACardComponent card = _domeo.getAnnotationCardsManager().getAnnotationCard(annotationItem.getClass().getName());
			if(card!=null) {
				if (spans.size()>1) card.initializeCard(i, this, annotationItem);
				else card.initializeCard(this, annotationItem);
				vp.add(card);
			} else {
				Window.alert("Card not implemented for: " + annotationItem.getClass().getName());
			}
		}
		
		this.setPopupPosition(x, y);
		this.show();
	}
	
	/*
	public void refresh() {
		initialize();
		List<VerticalPanel> tmpV = cookAnnotationElements(currentItems,
				currentSpans);

		if (tmpV.size() == 0) {
			HorizontalPanel hp1 = new HorizontalPanel();
			hp1.addStyleName("cs-bubbleBar");
			hp1.add(new Label("No annotation left!"));
			vp.add(hp1);
			Timer timer = new Timer() {
				public void run() {
					// _annotator.removeSpan(currentId);
					hide();
				}
			};
			timer.schedule(600);
		} else {
			Iterator<VerticalPanel> itEs = tmpV.iterator();
			while (itEs.hasNext()) {
				vp.add(itEs.next());
			}
		}
	}
	*/
}
