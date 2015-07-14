package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.reporting.src.testing.JsonReportManager;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.Utils;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;


public class PubMedCitationPainter {

	public static final String DOI_SYSTEM_PREFIX = "http://dx.doi.org/";
	public static final String PUBMED_PREFIX = "http://www.ncbi.nlm.nih.gov/pubmed/";
	public static final String PUBMED_CENTRAL_PREFIX = "http://www.ncbi.nlm.nih.gov/pmc/articles/";
	
	
	public static HTML getCitationAnnotation(MAnnotationReference reference) {
		if(((MPublicationArticleReference)reference.getReference()).getUnrecognized()==null || 
				((MPublicationArticleReference)reference.getReference()).getUnrecognized().trim().length()==0) {
			HTML html = new HTML(
				((MPublicationArticleReference)reference.getReference()).getAuthorNames() + "&nbsp;<b>" +
				((MPublicationArticleReference)reference.getReference()).getTitle() + "</b>&nbsp;" +
				((MPublicationArticleReference)reference.getReference()).getJournalPublicationInfo());
			return html;
		}  else {
			HTML html = new HTML(((MPublicationArticleReference)reference.getReference()).getUnrecognized());
			return html;
		}
	}
	
	
	
	public static FlowPanel getCitationAnnotationWithIds(MAnnotationReference reference, IDomeo domeo) {
		
		FlowPanel fp = new FlowPanel();
		StringBuffer htmlBuffer=new StringBuffer();
		if(((MPublicationArticleReference)reference.getReference()).getUnrecognized()==null || 
				((MPublicationArticleReference)reference.getReference()).getUnrecognized().trim().length()==0) {
			StringBuffer sb = new StringBuffer();
			if(getPubMedHtmlString(((MPublicationArticleReference)reference.getReference()).getPubMedId(), domeo)!=null) sb.append(getPubMedHtmlString(((MPublicationArticleReference)reference.getReference()).getPubMedId(), domeo) + "&nbsp;");
			if(getPmcHtmlString(((MPublicationArticleReference)reference.getReference()).getPubMedCentralId(), domeo)!=null) sb.append(getPmcHtmlString(((MPublicationArticleReference)reference.getReference()).getPubMedCentralId(), domeo) + "&nbsp;");
			if(getDoiHtmlString(((MPublicationArticleReference)reference.getReference()).getDoi(), domeo)!=null) sb.append(getDoiHtmlString(((MPublicationArticleReference)reference.getReference()).getDoi(), domeo));
			htmlBuffer.append(
				((MPublicationArticleReference)reference.getReference()).getAuthorNames() + "&nbsp;<b>" +
				((MPublicationArticleReference)reference.getReference()).getTitle() + "</b>&nbsp;" +
				((MPublicationArticleReference)reference.getReference()).getJournalPublicationInfo() +
				"&nbsp;" + sb.toString());
		}  else {
			htmlBuffer.append(((MPublicationArticleReference)reference.getReference()).getUnrecognized());
		}
		HTML ht = new HTML(htmlBuffer.toString());
		fp.add(ht);
		
		/*
		if(((MPublicationArticleReference)reference.getReference()).getUnrecognized()!=null && 
				((MPublicationArticleReference)reference.getReference()).getUnrecognized().trim().length()>0) {
			SimpleIconButtonPanel bu = new SimpleIconButtonPanel(domeo, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
				}},
			Domeo.resources.littleSquareAddIcon().getURL(), "Add reference metadata");
			fp.add(bu);
		}
		*/

		if(((BooleanPreference)domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_PERFORM_ANNOTATION_OF_CITATIONS)).getValue()) {
			List<MSelector> citationSelectors = ((MAnnotationCitationReference)reference).getCitationSelectors();
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("410px");
			if(((MAnnotationCitationReference)reference).getReferenceSelector()!=null) {
				hp.add(new HTML("Reference:&nbsp;"));
	
				SimpleIconButtonPanel showReferenceIcon = new SimpleIconButtonPanel(domeo, ActionShowAnnotation.getClickHandler(domeo, ""+reference.getLocalId()),
						Domeo.resources.showLittleIcon().getSafeUri().asString(), "Show reference in context");
				hp.add(showReferenceIcon);
				hp.setCellWidth(showReferenceIcon, "100%");
				hp.setCellHorizontalAlignment(showReferenceIcon, HasHorizontalAlignment.ALIGN_LEFT);
				
				if(citationSelectors.size()>0)  {
					hp.add(new HTML("&nbsp;Citations:&nbsp;"));
					for(MSelector sel: citationSelectors) {
						SimpleIconButtonPanel bu = new SimpleIconButtonPanel(domeo, ActionShowAnnotation.getClickHandler(domeo, reference.getLocalId()+":"+sel.getLocalId()),
								Domeo.resources.linkLittleIcon().getSafeUri().asString(), "Show citation in context");
						hp.add(bu);				}
				}
			}
			// Provenance
			// Different icons for different agents
			Image icon = new Image(Domeo.resources.computerLittleIcon());
			if(reference.getCreator().getAgentType().equals("foafx:Person")) {
				icon = new Image(Domeo.resources.privateLittleIcon());
			} 
			icon.setTitle("Created by " + reference.getCreator().getName() + " on " + reference.getFormattedCreationDate());
			hp.add(icon);
			hp.setCellWidth(icon, "100%");
			hp.setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_RIGHT);
			
			fp.add(hp);
		}
		return fp;
	}
	
	public static HTML getCitation(MPublicationArticleReference reference) {
		if(reference.getUnrecognized()==null || reference.getUnrecognized().trim().length()==0) {
			HTML html = new HTML(
				reference.getAuthorNames() + "&nbsp;<b>" +
				reference.getTitle() + "</b>&nbsp;" +
				reference.getJournalPublicationInfo());
			return html;
		}  else {
			HTML html = new HTML(reference.getUnrecognized());
			return html;
		}
	}
	
	public static HTML getFullCitation(MPublicationArticleReference reference, IDomeo domeo) {
		if(reference.getUnrecognized()==null || reference.getUnrecognized().trim().length()==0) {
			
			StringBuffer sb = new StringBuffer();
			if(getPubMedHtmlString(reference.getPubMedId(), domeo)!=null) sb.append(getPubMedHtmlString(reference.getPubMedId(), domeo, false, false) + "&nbsp;");
			if(getPmcHtmlString(reference.getPubMedCentralId(), domeo)!=null) sb.append(getPmcHtmlString(reference.getPubMedCentralId(), domeo, false, false) + "&nbsp;");
			if(getDoiHtmlString(reference.getDoi(), domeo)!=null) sb.append(getDoiHtmlString(reference.getDoi(), domeo, false, false) + "<br/>");
			
			HTML html = new HTML(
				reference.getAuthorNames() + "&nbsp;<b>" +
				reference.getTitle() + "</b>&nbsp;" +
				reference.getJournalPublicationInfo()+
				"&nbsp;" + sb.toString());
			return html;
		}  else {
			HTML html = new HTML(reference.getUnrecognized());
			return html;
		}
	}
	
	public static HTML getCitationWithLink(MPublicationArticleReference reference) {
		if(reference.getUnrecognized()==null || reference.getUnrecognized().trim().length()==0) {
			HTML html = new HTML(
				reference.getAuthorNames() + 
				"&nbsp;<a target=\"_blank\" href=\"" + 
				reference.getUrl() + "\">" + reference.getTitle() + 
				"</a></b>&nbsp;" + reference.getJournalPublicationInfo());
			return html;
		}  else {
			HTML html = new HTML(reference.getUnrecognized());
			return html;
		}
	}
	
	public static HTML getCitationWithIds(MPublicationArticleReference reference, IDomeo domeo) {
		if(reference.getUnrecognized()==null || reference.getUnrecognized().trim().length()==0) {
			HTML html = new HTML(
				reference.getAuthorNames() + 
				"&nbsp;<a target=\"_blank\" href=\"" + 
				reference.getUrl() + "\">" + reference.getTitle() + 
				"</a></b>&nbsp;" + reference.getJournalPublicationInfo() +
				"&nbsp;" + getDoiHtmlString(reference.getDoi(), domeo) + 
				"&nbsp;" + getPmcHtmlString(reference.getPubMedCentralId(), domeo));
			return html;
		}  else {
			HTML html = new HTML(reference.getUnrecognized());
			return html;
		}
	}
	
	public static HTML getIdentifiers(MPublicationArticleReference reference, IDomeo domeo) {
		
		StringBuffer sb = new StringBuffer();
		if(getPubMedHtmlString(reference.getPubMedId(), domeo)!=null) sb.append(getPubMedHtmlString(reference.getPubMedId(), domeo) + "<br/>");
		if(getPmcHtmlString(reference.getPubMedCentralId(), domeo)!=null) sb.append(getPmcHtmlString(reference.getPubMedCentralId(), domeo) + "<br/>");
		if(getDoiHtmlString(reference.getDoi(), domeo)!=null) sb.append(getDoiHtmlString(reference.getDoi(), domeo) + "<br/>");
		return new HTML(sb.toString());
	}
	
	public static String getPubMedHtmlString(String pmid, IDomeo domeo, boolean displayId, boolean displayEditButton) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));	
		String finalLink = Utils.getAnnotationToolLink(domeo, url, PUBMED_PREFIX + pmid);
		
		String text = (pmid!=null)? 
			"<img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> <a target=\"_blank\" href=\"" + PUBMED_PREFIX + 
			pmid + "\" title=\"Open document in PubMed\">PubMed</a> " + (displayId?pmid:"") + 
			(displayEditButton?
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\"><img src='" + 
			Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>":"") :null;	
		return text;
	}
	
	public static String getPubMedHtmlString(String pmid, IDomeo domeo) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));	
		String finalLink = Utils.getAnnotationToolLink(domeo, url, PUBMED_PREFIX + pmid);
		
		String text = (pmid!=null)? 
			"<img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> <a target=\"_blank\" href=\"" + PUBMED_PREFIX + 
			pmid + "\" title=\"Open document in PubMed\">PubMed</a> " + pmid + 
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\"><img src='" + 
			Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>" :null;	
		return text;
	}
	
	public static String getPubMedHtmlStringWithIcon(String pmid, IDomeo domeo) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));	
		String finalLink = Utils.getAnnotationToolLink(domeo, url, PUBMED_PREFIX + pmid);
		
		String text = (pmid!=null)? 
			"<a target=\"_blank\" href=\"" + PUBMED_PREFIX + 
			pmid + "\" title=\"Open document in PubMed\"><img src='" +Domeo.resources.pubmedLittleColorIcon().getSafeUri().asString() + "' style='border: 0px; width:35px; height: 14px;' /></a>" + pmid + 
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\"><img src='" + 
			Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>" :null;	
		return text;
	}
	
	public static String getDoiHtmlString(String doi, IDomeo domeo) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));	
		String finalLink = Utils.getAnnotationToolLink(domeo, url, DOI_SYSTEM_PREFIX + doi);
		
		String text = (doi!=null)? 
			"<img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> <a target=\"_blank\" href=\"" + DOI_SYSTEM_PREFIX + 
			doi + "\" title=\"Open document in DOI System\">The DOI System</a> " + doi + 
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\"><img src='" + 
			Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>" :null;	
		return text;
	}
	
	public static String getDoiHtmlString(String doi, IDomeo domeo, boolean displayId, boolean displayEditButton) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));	
		String finalLink = Utils.getAnnotationToolLink(domeo, url, DOI_SYSTEM_PREFIX + doi);
		
		String text = (doi!=null)? 
			"<img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> <a target=\"_blank\" href=\"" + DOI_SYSTEM_PREFIX + 
			doi + "\" title=\"Open document in DOI System\">The DOI System</a> " + (displayId?doi:"") + 
			(displayEditButton?
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\"><img src='" + 
			Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>":""):null;	
		return text;
	}
	
	public static String getDoiHtmlStringWithIcon(String doi, IDomeo domeo) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));	
		String finalLink = Utils.getAnnotationToolLink(domeo, url, DOI_SYSTEM_PREFIX + doi);
		
		String text = (doi!=null)? 
			"<a target=\"_blank\" href=\"" + DOI_SYSTEM_PREFIX + 
			doi + "\" title=\"Open document in DOI System\"><img src='" +Domeo.resources.doiLittleColorIcon().getSafeUri().asString() + "' style='border: 0px; width:35px; height: 10px; width:20px;' /></a>" + doi + 
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\"><img src='" + 
			Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>" :null;	
		return text;
	}

	public static String getPmcHtmlString(String pmcid, IDomeo domeo) {
	
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));
		String finalLink = Utils.getAnnotationToolLink(domeo, url, PUBMED_CENTRAL_PREFIX + pmcid + "/");
		
		String text = (pmcid!=null && pmcid.trim().length()>0)? 
			"<img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> <a target=\"_blank\" href=\"" + PUBMED_CENTRAL_PREFIX + 
			pmcid + "\" title=\"Open document in PubMed Central\">PubMed Central</a> " + pmcid +
			" <a target=\"_blank\" onclick=\"trackpath('" + finalLink 
			+ "','" + domeo.getPersistenceManager().getCurrentResourceUrl() 
			+ "','" + PUBMED_CENTRAL_PREFIX +  pmcid
			+ "','" + domeo.getUserManager().getUser().getUserName()
			+ "')\" title=\"Edit document in Domeo\">"+ 
			"<img src='" + Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>" :null;
			/*
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\">"+ 
			"<img src='" + Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>" :null;
			*/
		return text;
	}
	
	public static String getPmcHtmlString(String pmcid, IDomeo domeo, boolean displayId, boolean displayEditButton) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));
		String finalLink = Utils.getAnnotationToolLink(domeo, url, PUBMED_CENTRAL_PREFIX + (displayId?pmcid:"") + "/");
		
		String text = (pmcid!=null && pmcid.trim().length()>0)? 
			"<img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> <a target=\"_blank\" href=\"" + PUBMED_CENTRAL_PREFIX + 
			pmcid + "\" title=\"Open document in PubMed Central\">PubMed Central</a> " + (displayId?pmcid:"") +
			(displayEditButton?
			" <a target=\"_blank\" onclick=\"trackpath('" + finalLink 
			+ "','" + domeo.getPersistenceManager().getCurrentResourceUrl() 
			+ "','" + PUBMED_CENTRAL_PREFIX +  pmcid
			+ "','" + domeo.getUserManager().getUser().getUserName()
			+ "')\" title=\"Edit document in Domeo\">"+ 
			"<img src='" + Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>":"") :null;
					
			/*
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Edit document in Domeo\">"+ 
			"<img src='" + Domeo.resources.editLittleIcon().getSafeUri().asString() + "' style='border: 0px;' /></a>":"") :null;
					
			*/
		return text;
	}
	
	public static String getPmcHtmlStringWithIcon(String pmcid, IDomeo domeo) {
		
		String url = domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url.indexOf("?")>0) 
			url = url.substring(0, url.indexOf("?"));
		//String finalLink = Utils.getAnnotationToolLink(url, PUBMED_CENTRAL_PREFIX + pmcid + "/");
		
		String text = (pmcid!=null && pmcid.trim().length()>0)? 
			" <a target=\"_blank\" href=\"" + PUBMED_CENTRAL_PREFIX + 
			pmcid + "\" title=\"Open document in PubMed Central\">" + 
			"<img src='" + Domeo.resources.pmcLittleColorIcon().getSafeUri().asString() + "' style='border: 0px; height: 16px;' /></a> " + pmcid:null;
		/*+
			" <a target=\"_blank\" onclick=\"window.open('" + finalLink + "')\" title=\"Open document in PubMed\"><img src='" + Domeo.resources.editLittleIcon().getURL() + "' style='border: 0px;' /></a>" :null;*/
		return text;
	}
	
	public static void annotateDocument(IDomeo _domeo, String url) {
		JsonReportManager mgr = new JsonReportManager(_domeo, null);
		mgr.recordPathEntry(_domeo.getPersistenceManager().getCurrentResourceUrl(), url);
	}
}
