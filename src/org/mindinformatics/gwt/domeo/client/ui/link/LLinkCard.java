package org.mindinformatics.gwt.domeo.client.ui.link;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.framework.component.reporting.src.testing.JsonReportManager;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.utils.src.UrlUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LLinkCard extends Composite implements ICommandCompleted {

	interface Binder extends UiBinder<VerticalPanel, LLinkCard> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	IDomeo _domeo;
	Element _link;
	
	@UiField VerticalPanel body;
	
	public LLinkCard(IDomeo domeo, Element link) {
		_domeo = domeo;
		_link = link;
		
		initWidget(binder.createAndBindUi(this));
		
		refresh();
	}
	
	protected native void openAnnotationWindow(String link) 
	/*-{
		window.open(link);
	}-*/;
	
	// TODO simplify the construction (too many panels)
	private void refresh() {
		body.clear();
		
		final String link = _link.getAttribute("href");
		/*
		String displaylink = link;
		if(link.length()>60) {
			displaylink = link.substring(0, 35) + "..." + link.substring(link.length()-10);
		} 
		*/
		
		// Annotate page
		Image annotatePageIcon = new Image(Domeo.resources.editLittleIcon());
		annotatePageIcon.setTitle("Annotate Page");
		annotatePageIcon.setStyleName(Domeo.resources.css().actionIcon());
		
		Label annotatePageLabel = new Label("Annotate page");
		annotatePageLabel.setStyleName(Domeo.resources.css().noWrapActionText());
		
		/*
		if (AnnotatorHelper.ifRealMode()) {
			editIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String url = Annotator.getDocumentUrl().length()>0?Annotator.getDocumentUrl():
						_annotator.getAnnotationPersistenceManager().getCurrentSourceDocument().getUrl();					
					AnnotatorHelper.openUrl(AnnotatorHelper.getAnnotationToolLink(url, link));
				}
			});
			editLabel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String documentUrl =  Annotator.getDocumentUrl();
					String url = documentUrl.length()>0?documentUrl:
						_annotator.getAnnotationPersistenceManager().getCurrentSourceDocument().getUrl();
					AnnotatorHelper.openUrl(AnnotatorHelper.getAnnotationToolLink(url, link));
				}
			});
		}	
		*/	
		
		HorizontalPanel annotatePagePanel = new HorizontalPanel();
		annotatePagePanel.setStyleName(CurationPopup.localResources.popupCss().containerPanelWithPadding());
		annotatePagePanel.add(annotatePageIcon);
		annotatePagePanel.setCellWidth(annotatePageIcon, "20px");
		annotatePagePanel.setCellHorizontalAlignment(annotatePageIcon, HasHorizontalAlignment.ALIGN_CENTER);
		annotatePagePanel.add(annotatePageLabel);
		
		// Open page
		Image openPageIcon = new Image(Domeo.resources.externalLinkIcon());
		openPageIcon.setTitle("Open in a new tab");
		openPageIcon.setStyleName(Domeo.resources.css().actionIcon());
		
		final String documentUrl =  _domeo.getContentPanel().getAnnotationFrameWrapper().getUrl();
		final String jumpLink =  UrlUtils.getAbsoluteLink(documentUrl, link);
		
		final ICommandCompleted _this = this;
		annotatePageLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String finalLink = ApplicationUtils.getAnnotationToolLink(jumpLink);
				Window.alert(finalLink);
				JsonReportManager mgr = new JsonReportManager(_domeo, _this);
				mgr.recordPathEntry(_domeo.getPersistenceManager().getCurrentResourceUrl(), finalLink);
				openAnnotationWindow(finalLink);
			}
		});
		
		HorizontalPanel openPagePanel = new HorizontalPanel();
		openPagePanel.setStyleName(CurationPopup.localResources.popupCss().containerPanelWithPadding());
		HTML h = new HTML("<a target=\"_blank\" href=\"" + jumpLink + "\" title=\"" + jumpLink + "\">Open page</a>");
		h.setStyleName(Domeo.resources.css().noWrapText());
		openPagePanel.add(openPageIcon);
		openPagePanel.setCellWidth(openPageIcon, "20px");
		openPagePanel.setCellHorizontalAlignment(openPageIcon, HasHorizontalAlignment.ALIGN_CENTER);
		openPagePanel.add(h);
		
		// Annotate link
		Image annotateLinkIcon = new Image(Domeo.resources.editLinkIcon());
		annotateLinkIcon.setTitle("Annotate Link");
		annotateLinkIcon.setStyleName(Domeo.resources.css().actionIcon());

		Label annotateLinkLabel = new Label("Annotate link");
		annotateLinkLabel.setStyleName(Domeo.resources.css().noWrapActionText());
		annotateLinkLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Feature not yet implemented");
			}
		});
		
		HorizontalPanel annotateLinkPanel = new HorizontalPanel();
		annotateLinkPanel.setStyleName(CurationPopup.localResources.popupCss().containerPanelWithPadding());
		annotateLinkPanel.add(annotateLinkIcon);
		annotateLinkPanel.setCellWidth(annotateLinkIcon, "20px");
		annotateLinkPanel.setCellHorizontalAlignment(annotateLinkIcon, HasHorizontalAlignment.ALIGN_CENTER);
		annotateLinkPanel.add(annotateLinkLabel);
		
		// Complete panel
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.add(openPagePanel);
		//hp.setCellWidth(openPagePanel, "50%");
		hp.add(annotatePagePanel);
		hp.setCellHorizontalAlignment(annotatePagePanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.add(annotateLinkPanel);
		
		body.add(hp);
	}

	@Override
	public void notifyStageCompletion() {
		// TODO Auto-generated method stub
		
	}
}
