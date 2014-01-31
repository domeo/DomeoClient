package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedDatabase;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubmedReferenceSearchPanel extends Composite implements IContentPanel, IPubmedSearchObjectContainer {

	private static final String TITLE = "PubMed Search";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, PubmedReferenceSearchPanel> {}
	private static final Binder binder = GWT.create(Binder.class);	
	
	// By contract 
	private IDomeo _domeo;
	
	private IContainerPanel _glassPanel;
	
	@UiField VerticalPanel reference;
	@UiField VerticalPanel main;
	
	public PubmedReferenceSearchPanel(IDomeo domeo) {
		this(domeo, 600);
	}
	
	public PubmedReferenceSearchPanel(IDomeo domeo, int width) {
		_domeo = domeo;

		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		PubmedSearchWidget widget = new PubmedSearchWidget(_domeo, this, false);
		main.add(widget);
		
		main.setWidth(width+"px");
		//reference.add(PubMedCitationPainter.getCitationAnnotation(_annotation));
	}
	
	@Override
	public void addBibliographicObject(MPublicationArticleReference reference) {
		_domeo.getLogger().debug(this, "Adding publication: " + reference.getAuthorNames() + " " + reference.getTitle() + " " + reference.getJournalPublicationInfo());
		
		MAnnotationReference annotation = AnnotationFactory.createCitation(
				(MAgent)_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(), reference, _domeo.getPersistenceManager().getCurrentResource());
		((MDocumentResource)_domeo.getPersistenceManager().getCurrentResource()).setSelfReference(annotation);
		((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet().addAnnotation(annotation);
		
		
		
		/*
		_annotation.setReference(reference);
		_annotation.setCreatedOn(new Date());
		_annotation.setCreator(_domeo.getAgentManager().getUserPerson());
		_annotation.setHasChanged(true);
		*/
		
		if(_domeo.getPersistenceManager().getCurrentResource() instanceof MDocumentResource) {
			((MDocumentResource)_domeo.getPersistenceManager().getCurrentResource()).setSource(EPubMedDatabase.getInstance());
			((MDocumentResource)_domeo.getPersistenceManager().getCurrentResource()).setSelfReference(annotation);
		}
		
		_domeo.getPersistenceManager().getBibliographicSet().setHasChanged(true);
		_domeo.refreshResourceComponents();
		_domeo.getPersistenceManager().saveBibliography();
		getContainer().hide();
	}

	@Override
	public ArrayList<MPublicationArticleReference> getBibliographicObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MAnnotationReference> getBibliographicObjectAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContainer(IContainerPanel glassPanel) {
		_glassPanel = glassPanel;
	}

	@Override
	public IContainerPanel getContainer() {
		return _glassPanel;
	}
	
	public String getTitle() {
		return TITLE;
	}
}
