package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.TermsSelectionList;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.INifDataRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.NifManager;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.ISearchTermsContainer;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.ITermsSearch;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.SearchTermsWidget;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.user.client.ui.Image;

public class SearchNifResourcesWidget extends SearchTermsWidget implements INifDataRequestCompleted {

    public SearchNifResourcesWidget(IDomeo annotator, ISearchTermsContainer container, ITermsSearch termsSearch, TermsSelectionList resultList, boolean showTitle) {
        super(annotator, container, termsSearch, resultList, showTitle);
    }
    
    @Override
    public void performSearch(String textQuery, String resource) {
        _domeo.getLogger().info(this, "Searching for " + textQuery);
        resultsContainerPanel.clear();
        resultsContainerPanel.add(new Image(_resources.littleProgressIcon()));
        
        /*if(_domeo.isHostedMode()) {
            Window.alert("Live search not available in hosted mode: " + textQuery + "-" + resource);
            NifManager nifManager = NifManager.getInstance();
            nifManager.selectConnector(_domeo);
            nifManager.searchData(this, textQuery, "", "", resource, 0, 0);
        } else {*/
            try {
                NifManager nifManager = NifManager.getInstance();
                nifManager.selectConnector(_domeo);
                nifManager.searchData(this, textQuery, "", "", resource, 0, 0);
                //nifManager.searchData(this, textQuery, typeQuery, vendor, "nif-0000-07730-1", 0, 0);
            } catch(Exception e) {
                _domeo.getLogger().exception(this, "Nif Manager terminated with exception: " + e.getMessage());
            }            
        //}
    }

    @Override
    public String getWidgetTitle() {
        return "Search NIF";
    }

    @Override
    public void returnData(ArrayList<MGenericResource> data) {
        ArrayList<MLinkedResource> results = new ArrayList<MLinkedResource>();
        for(MGenericResource datum: data) {
            if(datum instanceof MLinkedResource)
                results.add((MLinkedResource)datum);
        }
        searchTermsResult = results;
        displayResults(results);
    }

    @Override
    public void returnData(int totalPages, int pageSize, int pageNumber,
            ArrayList<MGenericResource> data) {
        // TODO Auto-generated method stub
    }
}
