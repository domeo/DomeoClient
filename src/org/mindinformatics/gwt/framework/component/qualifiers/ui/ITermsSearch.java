package org.mindinformatics.gwt.framework.component.qualifiers.ui;

public interface ITermsSearch {

    public void initialize(ISearchWidget searchWidget, String textContent);
    public void updateResultsStats();
    public String getSourceFilterValue();
    public void setSearchBoxValue(String value);
}
