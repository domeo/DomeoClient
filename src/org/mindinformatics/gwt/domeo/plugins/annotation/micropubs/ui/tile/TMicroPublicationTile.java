package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.tile;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpData;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpStatement;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TMicroPublicationTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TMicroPublicationTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private MMicroPublicationAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	@UiField HTML icon;
	@UiField Label type;
	@UiField HTML text;
	@UiField HTML tags;
	@UiField HTML support;
	
	public TMicroPublicationTile(IDomeo domeo, IAnnotationEditListener listener) {
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
			_annotation = (MMicroPublicationAnnotation) annotation;
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
			_domeo.getLogger().debug(this, "t-1 missing type!!!!");
			createProvenanceBar("", provenance, _annotation.getMicroPublication().getType(), _annotation);

//			if(_annotation.getType() == PostitType.COMMENT_TYPE)
//				icon.setHTML("<img src='" + Domeo.resources.littleCommentIcon().getSafeUri().asString() + "'/>");
			
			//type.setText(_annotation.getType().getName()+":");
			//type.setText("Claim:");
			_domeo.getLogger().debug(this, "t00 ");
			text.setHTML(_annotation.getMicroPublication().getArgues().getText());
			text.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit or display?");
				}
			});
			_domeo.getLogger().debug(this, "t01 ");
			int statementsCounter = 0;
			int dataCounter = 0;
			int refCounter = 0;
			int challengeStatementsCounter = 0;
			int challengeDataCounter = 0;
			int challengeRefCounter = 0;
			
			for(MMpRelationship rel:_annotation.getMicroPublication().getEvidence()) {
				if(rel.getObjectElement() instanceof MMpReference) {
					if(rel.getName().equals(IMicroPublicationsOntology.supportedBy)) refCounter++;
					else if(rel.getName().equals(IMicroPublicationsOntology.challengedBy)) challengeRefCounter++;
				} else if(rel.getObjectElement() instanceof MMpData) {
					if(rel.getName().equals(IMicroPublicationsOntology.supportedBy)) dataCounter++;
					else if(rel.getName().equals(IMicroPublicationsOntology.challengedBy)) challengeDataCounter++;
				} else if(rel.getObjectElement() instanceof MMpStatement) {
					if(rel.getName().equals(IMicroPublicationsOntology.supportedBy)) statementsCounter++;
					else if(rel.getName().equals(IMicroPublicationsOntology.challengedBy)) challengeStatementsCounter++;
				}
			}
			_domeo.getLogger().debug(this, "t02 ");
			boolean flag = false;
			StringBuffer buffer = new StringBuffer();
			if(_annotation.getMicroPublication().getEvidence().size()>0) {
				if(dataCounter>0 || statementsCounter>0 || refCounter>0) {
					flag = true;
					buffer.append("Supported by ");
					if(dataCounter>0) {
						if(dataCounter==1) buffer.append("1 data item");
						else buffer.append(dataCounter + " data items");
					}					
					if(statementsCounter>0) {
						if(dataCounter>0 && refCounter==0) buffer.append(" and ");
						else if(dataCounter>0 && refCounter>0) buffer.append(", ");
						if(statementsCounter==1) buffer.append("1 statement");
						else buffer.append(statementsCounter + " statements");
					}
					if((dataCounter>0 || statementsCounter>0) && refCounter>0) {
						buffer.append(" and ");
					} 
					if(refCounter>0) {
						if(refCounter==1) buffer.append("1 reference");
						else buffer.append(refCounter + " references");
					}
					if(challengeDataCounter==0 && challengeStatementsCounter==0 && challengeRefCounter==0)  buffer.append(".<br/>");
					else buffer.append(". ");	
				}				
				if(challengeDataCounter>0 || challengeStatementsCounter>0 || challengeRefCounter>0) {
					flag = true;
					buffer.append("Challenged by ");
					if(challengeDataCounter>0) {
						if(challengeDataCounter==1) buffer.append("1 data item");
						else buffer.append(challengeDataCounter + " data items");
					}					
					if(challengeStatementsCounter>0) {
						if(challengeDataCounter>0 && challengeRefCounter==0) buffer.append(" and ");
						else if(challengeDataCounter>0 && challengeRefCounter>0) buffer.append(", ");
						if(challengeStatementsCounter==1) buffer.append("1 statement");
						else buffer.append(challengeStatementsCounter + " statements");
					}
					if((challengeDataCounter>0 || challengeStatementsCounter>0) && challengeRefCounter>0) {
						buffer.append(" and ");
					} 
					if(challengeRefCounter>0) {
						if(challengeRefCounter==1) buffer.append("1 reference");
						else buffer.append(challengeRefCounter + " references");
					}
					buffer.append(".<br/>");		
				}				
			} 
			
			if(!flag) {
				support.setVisible(false);
			} else {
				support.setHTML(buffer.toString());
			}
			
			// http://cssglobe.com/pure-css3-post-tags/
			if(_annotation.getMicroPublication().getQualifiers().size()>0) {
				StringBuffer sb = new StringBuffer();
				sb.append(" <ul class='tags'>");
				//sb.append("<li>&nbsp;&nbsp;&nbsp;Qualified by:</li>");
				for(MMpRelationship rel: _annotation.getMicroPublication().getQualifiers()) {
					sb.append("<li><a href='" + ((MMpQualifier)rel.getObjectElement()).getQualifier().getUrl() + "' target='_blank'>" + 
							((MMpQualifier)rel.getObjectElement()).getQualifier().getLabel() + " <span class='source'>- " +  
							((MMpQualifier)rel.getObjectElement()).getQualifier().getSource().getLabel() + "</span></a></li>");
				}
				
				sb.append("</ul>");
				tags.setHTML(sb.toString());
			} else {
				tags.setVisible(false);
			}
			
			/*
			highlight.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					Window.alert("Display actions?");
				}
			});
			*/
			
			if(SelectorUtils.isOnMultipleTargets(_annotation.getSelectors())) {
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(new HTML("Targets:&nbsp;"));
				for(MSelector sel: _annotation.getSelectors()) {
					SimpleIconButtonPanel bu = new SimpleIconButtonPanel(_domeo, ActionShowAnnotation.getClickHandler(_domeo, 
							_annotation.getLocalId()+":"+sel.getLocalId()),
							Domeo.resources.showLittleIcon().getSafeUri().asString(), "Show target in context");
					hp.add(bu);		
				}
				content.add(hp);
			}
			
			injectButtons("", content, _annotation);
			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
}
