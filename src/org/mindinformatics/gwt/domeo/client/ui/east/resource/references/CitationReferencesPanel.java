package org.mindinformatics.gwt.domeo.client.ui.east.resource.references;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.PubmedReferencesSearchPanel;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.widget.WidgetUtilsResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CitationReferencesPanel extends Composite implements IRefreshableComponent {

	// UI Binder
	interface Binder extends UiBinder<Widget, CitationReferencesPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	public static final WidgetUtilsResources widgetUtilsResources = 
		GWT.create(WidgetUtilsResources.class);
	
	interface LocalCss extends CssResource {
		String indexOdd();
		String indexEven();
	}
	
	@UiField LocalCss style;
	@UiField VerticalPanel body;
	
	// By contract 
	private IDomeo _domeo;
	private boolean _isBibliographicSetVirtual;
	
	public CitationReferencesPanel(IDomeo domeo, boolean isBibliographicSetVirtual) {
		_domeo = domeo;
		_isBibliographicSetVirtual = isBibliographicSetVirtual;
		
		initWidget(binder.createAndBindUi(this));
		//body.setHeight("100%");
		
		widgetUtilsResources.widgetCss().ensureInjected();
		
		refresh();
	}
	
	public void refresh() {
		body.clear();
		if(_domeo.getPersistenceManager().getCurrentResource() instanceof IReferences) {
			if(((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().size()==0) {
				body.add(new HTML("No references available"));
			} else {
				_domeo.getLogger().debug(this, "References");
				FlowPanel fp = new FlowPanel();
				for(int i=0; i<((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().size(); i++) {
					HorizontalPanel hp = new HorizontalPanel();
					if(i%2 == 1) hp.setStyleName(widgetUtilsResources.widgetCss().tableOddRow());
					FlowPanel fp2 = new FlowPanel();
					HTML leftHtml = new HTML(""+(((MAnnotationCitationReference)((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(i)).getReferenceIndex().intValue()+1));
					if(i%2 == 1)  leftHtml.setStyleName(style.indexOdd());
					else leftHtml.setStyleName(style.indexEven());
					fp2.add(leftHtml);
					if(((MPublicationArticleReference)((MAnnotationCitationReference)((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(i)).getReference()).getUnrecognized()!=null
						&& !_isBibliographicSetVirtual) {
						final int index = i;
						SimpleIconButtonPanel bu = new SimpleIconButtonPanel(_domeo, new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										_domeo.getLogger().command(this, "Open panel for adding a citation for reference #" + index);
										PubmedReferencesSearchPanel afp = new PubmedReferencesSearchPanel(_domeo, index, 800);
										new EnhancedGlassPanel(_domeo, afp, afp.getTitle(), 800, false, false, false);
									}},
								Domeo.resources.littleBookAddIcon().getSafeUri().asString(), "Add reference metadata");
						
						fp2.add(bu);
					} else {
						/*
						Image icon = new Image(Domeo.resources.editLittleIcon());
						icon.setStyleName(ATileComponent.tileResources.css().button());
						fp2.add(icon);
						*/
					}
					hp.add(fp2);
					hp.setCellWidth(fp2, "18px");
					hp.add(PubMedCitationPainter.getCitationAnnotationWithIds(((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(i), _domeo));
					fp.add(hp);
				}
				body.add(fp);
			}
		} else {
			body.add(new HTML("No references available"));
		}
	}
}
