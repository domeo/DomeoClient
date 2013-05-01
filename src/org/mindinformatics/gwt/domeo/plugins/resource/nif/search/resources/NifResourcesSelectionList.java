package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.ITermsSelectionConsumer;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.TermsSelectionList;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;

public class NifResourcesSelectionList extends TermsSelectionList {

    public NifResourcesSelectionList(IDomeo domeo,
            ITermsSelectionConsumer main, ArrayList<MLinkedResource> terms,
            HashMap<String, MLinkedResource> currentTerms) {
        super(domeo, main, terms, currentTerms);
    }
    
    @Override
    protected void initTable() {
        // Initialize the header.
        header.getColumnFormatter().setWidth(0, "25px");
        header.getColumnFormatter().setWidth(1, "180px");
        //header.getColumnFormatter().setWidth(2, "300px");
        

        checkAll = new CheckBox();
        checkAll.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                for (CheckBox checkBox : _checkBoxes) {
                    if (checkAll.getValue()) {
                        checkBox.setValue(true);
                    } else
                        checkBox.setValue(false);
                }
                checkSelection();
            }
        });

        header.setWidget(0, 0, checkAll);
        header.setText(0, 1, "Term");
        header.setText(0, 2, "Description");

        // Initialize the table.
        table.getColumnFormatter().setWidth(0, "25px");
        table.getColumnFormatter().setWidth(1, "180px");
        //table.getColumnFormatter().setWidth(2, "300px");
    }
    
    @Override
    protected void displayRow(int row, MLinkedResource item, CheckBox chk) {
        StringBuffer sb = new StringBuffer();
        List<ITrustedResourceDigester> digesters = _domeo.getLinkedDataDigestersManager().getLnkedDataDigesters(item);
        for(ITrustedResourceDigester digester: digesters) {
            sb.append("<a target=\"_blank\"href=\""+digester.getLinkUrl(item)+"\">@"+digester.getLinkLabel(item)+"</a>&nbsp;");
        }
        table.setWidget(row, 0, chk);
        table.setWidget(row, 1, new HTML("<a target=\"_blank\"href=\""+item.getUrl()+"\">"+item.getLabel()+"</a>"));
        table.setWidget(row, 2, new HTML(((item.getDescription()!=null)? item.getDescription():"") + "; " + sb.toString()));
    }

}
