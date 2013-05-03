package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.card;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.info.QualifierPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CQualifierCard extends ACardComponent {

	interface Binder extends UiBinder<Widget, CQualifierCard> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private int _index = -1;
	private MQualifierAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	@UiField HTML description;
	@UiField HorizontalPanel socialPanel;
	@UiField HorizontalPanel curationPanel;
	
	public CQualifierCard(IDomeo domeo) {
		super(domeo);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	@Override
	public void initializeCard(int index, CurationPopup curationPopup,
			MAnnotation annotation) {
		_index = index;
		initializeCard(curationPopup, annotation);
	}
	
	@Override
	public void initializeCard(CurationPopup curationPopup, MAnnotation annotation) {
		_annotation = (MQualifierAnnotation) annotation;
		_curationPopup = curationPopup;
		refresh();
	}

	@Override
	public Widget getCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		try {
			if (_index>-1) createProvenanceBar(QualifierPlugin.getInstance().getPluginName(), _index, provenance, _annotation);
			else createProvenanceBar(QualifierPlugin.getInstance().getPluginName(), provenance, _annotation);
			//label.setText(_annotation.getTerm().getLabel()+",");
			
			StringBuffer sb = new StringBuffer();
			for(int j=0; j<_annotation.getTerms().size(); j++) {
				sb.append("<img src='" + Domeo.resources.tagIcon().getSafeUri().asString() + "'/>" + "<b>" + _annotation.getTerms().get(j).getLabel()+"</b> from "+
						_annotation.getTerms().get(j).getSource().getLabel()+", "+
						_annotation.getTerms().get(j).getDescription());
				List<ITrustedResourceDigester> digesters = _domeo.getLinkedDataDigestersManager().getLnkedDataDigesters(_annotation.getTerms().get(j));
				for(ITrustedResourceDigester digester: digesters) {
					if(digester.getLinkLabel(_annotation.getTerms().get(j)).trim().length()>0) 
						sb.append(", <a target=\"_blank\"href=\""+digester.getLinkUrl(_annotation.getTerms().get(j))+"\">@"+digester.getLinkLabel(_annotation.getTerms().get(j))+"</a>&nbsp;");
				}
				if(j<(_annotation.getTerms().size()-1)) sb.append("; ");
			}
			
			description.setHTML(sb.toString());
			description.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit or display?");
				}
			});
			
			createSocialBar(socialPanel, _annotation);
			
			if(_domeo.isHostedMode() || _annotation.getCreator() instanceof ISoftware)
				createCurationBar(socialPanel, _annotation);

			injectButtons(QualifierPlugin.getInstance().getPluginName(),content, _annotation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}

    @Override
    public void setSpan(Element element) {
        _span = element;
    }
}
