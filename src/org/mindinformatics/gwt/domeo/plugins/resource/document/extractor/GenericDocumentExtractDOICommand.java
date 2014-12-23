package org.mindinformatics.gwt.domeo.plugins.resource.document.extractor;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.APubMedBibliograhyExtractorCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralExtractor;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/** Class to handle searching for a DOI in a normal web document.
 * @author Tom Wilkin */
public class GenericDocumentExtractDOICommand extends APubMedBibliograhyExtractorCommand
		implements ICommand, IPubMedItemsRequestCompleted 
{
	
	protected IDomeo domeo;
	protected IContentExtractorCallback callback;
	protected ICommandCompleted completionCallback;
	
	/** The list of DOIs found in the document that could be the DOI. */
	private List<String> doiList;
	
	/** The index in the DOI list for the DOI that is currently being queried. */ 
	private int currentDoi;

	public GenericDocumentExtractDOICommand(final IDomeo domeo, IContentExtractorCallback callback, 
			ICommandCompleted completionCallback) 
	{
		this.domeo = domeo;
		this.callback = callback;
		this.completionCallback = completionCallback;
	}
	
	@Override
	public void execute( ) {
		domeo.getLogger( ).info(this, "Searching for document DOI.");
		((ProgressMessagePanel)((DialogGlassPanel)domeo.getDialogPanel( )).getPanel( )).setMessage(
				"Searching for document DOI"
		);
		
		IFrameElement iframe = IFrameElement.as(
				domeo.getContentPanel( ).getAnnotationFrameWrapper( ).getFrame( ).getElement( )
		);
		if(iframe != null) {
			// search for any DOIs in the document
			doiList = new ArrayList<String>( );
			String body = iframe.getContentDocument( ).getDocumentElement( ).getInnerHTML( );
			RegExp regExp = RegExp.compile(
					"(10[.][0-9]{4,}[0-9:.\\-/a-z]+)",
					"gi"
			);
			MatchResult matcher;
			while((matcher = regExp.exec(body)) != null) {
				for(int i = 0; i < matcher.getGroupCount( ); i++) {
					if(!doiList.contains(matcher.getGroup(i))) {
						// add this DOI to the list to compare against PubMed
						domeo.getLogger( ).debug(this, "Found " + matcher.getGroup(i));
						doiList.add(matcher.getGroup(i));
					}
				}
			}
		}
		
		// start querying them against PubMed
		currentDoi = -1;
		nextDoi( );
	}

	@Override
	public void returnBibliographicObject(final ArrayList<MPublicationArticleReference> citations) {
		try {
			if(citations.size( ) > 0) {
				MAnnotationReference annotation = AnnotationFactory.createCitation(
						EPubMedCentralExtractor.getInstance( ),
						domeo.getAgentManager( ).getSoftware( ),
						citations.get(0),
						domeo.getPersistenceManager( ).getCurrentResource( )
				);
				
				// get the titles and process them for the comparison
				String docTitle = domeo.getPersistenceManager( ).getCurrentResource( ).getLabel( );
				docTitle = docTitle.toLowerCase( ).trim( );
				String pubMedTitle = citations.get(0).getTitle( );
				if(pubMedTitle != null) {
					pubMedTitle = pubMedTitle.toLowerCase( ).trim( );
					domeo.getLogger( ).debug(this, "docTitle: " + docTitle);
					domeo.getLogger( ).debug(this, "pubMedTitle: " + pubMedTitle);
					// compare the document title with the version in PubMed
					if(stringSimilarity(docTitle, pubMedTitle)) {
						// we have a match, use this DOI
						domeo.getLogger( ).info(this, "Found matching DOI " + doiList.get(currentDoi));
						currentDoi = doiList.size( );
						domeo.getLogger( ).debug(this, "Accepted: " + citations.get(0).getDoi());
						((MDocumentResource)domeo.getPersistenceManager( ).getCurrentResource( )).setSelfReference(annotation);
						((AnnotationPersistenceManager)domeo.getPersistenceManager( )).getBibliographicSet( ).addAnnotation(annotation);
					}
				}
			}
		} catch(Exception e) {
			domeo.getLogger( ).exception(this, 
					"Exception while extracting Bibliographic information. " + e.getMessage( ));
		} finally {
			nextDoi( );
		}
	}

	@Override
	public void bibliographyObjectNotFound( ) {
		domeo.getLogger( ).warn(this, "bibliographyObjectNotFound");
		nextDoi( );
	}

	@Override
	public void bibliographyObjectNotFound(final String message) {
		domeo.getLogger( ).warn(this, "bibliographyObjectNotFound " + message);
		nextDoi( );		
	}
	
	/** Attempt to retrieve the title information for the next DOI from the document and compare
	 * against the title for this document to identify if they are the same. */
	private void nextDoi( ) {
		currentDoi++;
		if(currentDoi >= doiList.size( ) || currentDoi>2) {
			// iterated over all the discovered DOIs
			completionCallback.notifyStageCompletion( );
			return;
		}
		
		domeo.getLogger( ).debug(this, "checking doi: " + doiList.get(currentDoi));
		// Submit a request to PubMed for information relation to the current DOI
		try {
			PubMedManager pubMedManager = PubMedManager.getInstance( );
			pubMedManager.selectPubMedConnector(domeo, this);
			pubMedManager.getBibliographicObject(this, "dois", doiList.get(currentDoi));
		} catch(Exception e) {
			domeo.getLogger( ).exception(this, 
					"Exception while requesting Bibliographic information. " + e.getMessage( ));
		}
	}
	
	/** Return whether the two strings are similar enough to indicate that the titles are the same.
	 * Algorithm based on http://www.ibm.com/developerworks/library/j-seqalign/
	 * @param str1 The first title string from the document.
	 * @param str2 The second title string from the PubMed query for the DOI.
	 * @return Whether the strings are similar enough. */
	private boolean stringSimilarity(final String str1, final String str2) {
		int[ ][ ] matrix = new int[str1.length( )][str2.length( )];
		int max = -1;
		for(int i = 0; i < str1.length( ); i++) {
			for(int j = 0; j < str2.length( ); j++) {
				// initialise the outside edges
				if(i == 0 || j == 0) {
					matrix[i][j] = 0;
					continue;
				}
				
				// get the values the new value may be dependent on
				int aboveScore = matrix[i - 1][j];
				int leftScore = matrix[i][j - 1];
				int aboveLeftScore = matrix[i - 1][j - 1];
				
				// check if we have a match for these characters
				int matchScore;
				if(str1.charAt(i) == str2.charAt(j)) {
					matchScore = aboveLeftScore + 1;
				} else {
					matchScore = aboveLeftScore;
				}
				
				// store the store for these characters
				if(matchScore >= aboveScore) {
					if(matchScore >= leftScore) {
						matrix[i][j] = matchScore;
					} else {
						matrix[i][j] = leftScore;
					}
				} else {
					if(aboveScore >= leftScore) {
						matrix[i][j] = aboveScore;
					} else {
						matrix[i][j] = leftScore;
					}
				}
				
				// check if this is the current maximum
				if(matrix[i][j] > max) {
					max = matrix[i][j];
				}
			}
		}
		
		// decide whether this is similar enough
		return (100.0 / str2.length( )) * max >= 75;
	}

};
