package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoBioPortalSearchResultsWrapper extends JavaScriptObject {

	protected JsoBioPortalSearchResultsWrapper() {}
	
	public final native String getPageSize() /*-{ return this.pagesize; }-*/;
	public final native String getPageNumber() /*-{ return this.pagenumber; }-*/;
	public final native String getTotalPages() /*-{ return this.totalpages; }-*/;
	public final native String getException() /*-{ return this.exception; }-*/;
	public final native JsArray getResults() /*-{ return this.terms; }-*/;
}
