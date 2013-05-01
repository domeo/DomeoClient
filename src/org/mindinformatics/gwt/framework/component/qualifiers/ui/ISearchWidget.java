package org.mindinformatics.gwt.framework.component.qualifiers.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.ui.services.IWidget;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

public interface ISearchWidget extends IWidget {
    public void performSearch(String textQuery, String source);
    public void filterBySource(String sourceId);
    public ArrayList<MLinkedResource> getSearchTermsResult();
    public HashMap<String, String> getSearchTermsResultSources();
}
