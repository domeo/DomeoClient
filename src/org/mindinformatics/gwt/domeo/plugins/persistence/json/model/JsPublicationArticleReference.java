package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsPublicationArticleReference extends JavaScriptObject {

	protected JsPublicationArticleReference() {}
	
	public final native String getId() /*-{ return this['@id']; }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;
	public final native String getTitle() /*-{ return this.title; }-*/;
	public final native String getAuthorNames() /*-{ return this.authorNames; }-*/;
	public final native String getPublicationInfo() /*-{ return this.publicationInfo; }-*/;
	public final native String getPublicationDate() /*-{ return this.publicationDate; }-*/;
	public final native String getJournalName() /*-{ return this.journalName; }-*/;
	public final native String getJournalIssn() /*-{ return this.journalIssn; }-*/;
	public final native String getDoi() /*-{ return this.doi; }-*/;
	public final native String getPubMedId() /*-{ return this.pmid; }-*/;
	public final native String getPubMedCentralId() /*-{ return this.pmcid; }-*/;
	public final native String getUnrecognized() /*-{ return this.unrecognized; }-*/;
	
	public final native String getCreatedOnAsString() /*-{ return this.createdon; }-*/;
	public final Date getCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss Z");
		return fmt.parse(getCreatedOnAsString());
	}
}
