package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.framework.model.references.IReferences;
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
public class PubmedReferencesSearchPanel extends Composite implements IContentPanel, IPubmedSearchObjectContainer {

	private static final String TITLE = "PubMed Search";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, PubmedReferencesSearchPanel> {}
	private static final Binder binder = GWT.create(Binder.class);	
	
	// By contract 
	private IDomeo _domeo;
	
	private IContainerPanel _glassPanel;
	private MAnnotationCitationReference _annotation;
	
	@UiField VerticalPanel reference;
	@UiField VerticalPanel main;
	
	public PubmedReferencesSearchPanel(IDomeo domeo, int referenceIndex) {
		this(domeo, referenceIndex, 600);
	}
	
	public PubmedReferencesSearchPanel(IDomeo domeo, int referenceIndex, int width) {
		_domeo = domeo;
		_annotation = ((MAnnotationCitationReference)((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(referenceIndex));
		
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		PubmedSearchWidget widget = new PubmedSearchWidget(_domeo, this, false);
		main.add(widget);
		
		main.setWidth(width+"px");
		reference.add(PubMedCitationPainter.getCitationAnnotation(_annotation));
	}
	
	@Override
	public void addBibliographicObject(MPublicationArticleReference reference) {
		_domeo.getLogger().debug(this, "Adding publication: " + reference.getAuthorNames() + " " + reference.getTitle() + " " + reference.getJournalPublicationInfo());
		_annotation.setReference(reference);
		_annotation.setCreatedOn(new Date());
		_annotation.setCreator(_domeo.getAgentManager().getUserPerson());
		_annotation.setHasChanged(true);
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
