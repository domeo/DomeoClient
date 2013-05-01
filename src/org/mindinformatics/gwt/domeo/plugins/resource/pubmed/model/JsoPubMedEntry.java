package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoPubMedEntry extends JavaScriptObject implements IDocumentBibliographicData {

	protected JsoPubMedEntry() {}
	
	public final native String getUrl() /*-{ return this.url; }-*/;
	public final native String getTitle() /*-{ return this.title; }-*/;
	public final native String getPublicationAuthors() /*-{ return this.publicationAuthors; }-*/;
	public final native String getPublicationInfo() /*-{ return this.publicationInfo; }-*/;
	public final native String getPublicationDate() /*-{ return this.publicationDate; }-*/;
	public final native String getPmcId() /*-{ return this.pmcid; }-*/;
	public final native String getPmId() /*-{ return this.pmid; }-*/;
	public final native String getDoi() /*-{ return this.doi; }-*/;
	public final native String getJournalName() /*-{ return this.journalName; }-*/;
	public final native String getJournalIssn() /*-{ return this.journalIssn; }-*/;
	//public final native JsArray<JsoPerson> getAuthorNames() /*-{ return this.authorNames; }-*/;
}
