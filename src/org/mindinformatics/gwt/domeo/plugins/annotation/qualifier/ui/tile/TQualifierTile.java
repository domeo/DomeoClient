package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.tile;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.info.QualifierPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
public class TQualifierTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TQualifierTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private MQualifierAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	//@UiField FlowPanel content;
	//@UiField HTML icon;
	@UiField HTML description;
	
	public TQualifierTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	@Override
	public void initializeLens(MAnnotation annotation) {
		try {
			_annotation = (MQualifierAnnotation) annotation;
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}	
	@Override
	public Widget getTile() {
		return this;
	}
	@Override
	public void refresh() {
		try {
			createProvenanceBar(QualifierPlugin.getInstance().getPluginName(), provenance, "Qualifier", _annotation);
			
			StringBuffer sb2 = new StringBuffer();
			
			sb2.append(" <ul class='tags'>");
			for(MLinkedResource rel: _annotation.getTerms()) {
				sb2.append("<li><a href='" + rel.getUrl() + "' target='_blank'>" + 
						rel.getLabel() + " <span class='source'>- " +  
						rel.getSource().getLabel() + "</span></a></li>");
			}
			
			sb2.append("</ul>");
			description.setHTML(sb2.toString());
			
			/*
			StringBuffer sb = new StringBuffer();

			
			for(int j=0; j<_annotation.getTerms().size(); j++) {
				sb.append("<img src='" + Domeo.resources.tagIcon().getSafeUri().asString() + "'/>" +  "<b>" + _annotation.getTerms().get(j).getLabel()+"</b> from <a target=\"_blank\"href=\""+ _annotation.getTerms().get(j).getSource().getUrl() +"\">"+
						_annotation.getTerms().get(j).getSource().getLabel()+"</a>");
				if(_annotation.getTerms().get(j).getDescription()!=null && _annotation.getTerms().get(j).getDescription().length()>0)
					sb.append(", "+ _annotation.getTerms().get(j).getDescription());
				boolean nodigester = true;
				List<ITrustedResourceDigester> digesters = _domeo.getLinkedDataDigestersManager().getLnkedDataDigesters(_annotation.getTerms().get(j));
				for(ITrustedResourceDigester digester: digesters) {
					if(digester.getLinkLabel(_annotation.getTerms().get(j)).trim().length()>0) {
						sb.append(", <a target=\"_blank\"href=\""+digester.getLinkUrl(_annotation.getTerms().get(j))+"\">@"+digester.getLinkLabel(_annotation.getTerms().get(j))+"</a>&nbsp;");
						nodigester = false;
					}
				}
				
				
					

				
				if(_annotation.getAnnotatedBy().size()>0) {
					int right = 0;
					int broad = 0;
					int wrong = 0;
					for(MAnnotation ann: _annotation.getAnnotatedBy()) {
						if(ann instanceof MCurationToken) {
							if(((MCurationToken)ann).getStatus().equals(MCurationToken.CORRECT)) 
								right++;
							if(((MCurationToken)ann).getStatus().equals(MCurationToken.CORRECT_BROAD)) 
								broad++;
							if(((MCurationToken)ann).getStatus().equals(MCurationToken.INCORRECT)) 
								wrong++;
						}
					}
					if(right>0) sb.append(" <img src='" + Domeo.resources.acceptIcon().getSafeUri().asString() + "'/>"+right+" ");
					if(broad>0) sb.append(" <img src='" + Domeo.resources.acceptBroadIcon().getSafeUri().asString() + "'/>"+broad+" ");
					if(wrong>0) sb.append(" <img src='" + Domeo.resources.rejectIcon().getSafeUri().asString() + "'/>"+wrong+" ");
				}
				
				if(j<(_annotation.getTerms().size()-1)) sb.append("; ");
			}
			
			description.setHTML(sb.toString());
			*/
			
			//injectButtons(QualifierPlugin.getInstance().getPluginName(), content, _annotation);
			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
}
