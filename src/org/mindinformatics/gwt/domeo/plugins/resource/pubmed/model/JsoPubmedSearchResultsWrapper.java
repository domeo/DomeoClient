package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoPubmedSearchResultsWrapper extends JavaScriptObject {

	protected JsoPubmedSearchResultsWrapper() {}
	
	public final native String getRange() /*-{ return this.range; }-*/;
	public final native String getTotal() /*-{ return this.total; }-*/;
	public final native String getOffset() /*-{ return this.offset; }-*/;
	public final native String getException() /*-{ return this.exception; }-*/;
	public final native JsArray getResults() /*-{ return this.results; }-*/;
}
