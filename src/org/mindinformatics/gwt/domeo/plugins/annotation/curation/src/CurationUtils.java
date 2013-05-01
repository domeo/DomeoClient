package org.mindinformatics.gwt.domeo.plugins.annotation.curation.src;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CurationUtils {

    public static String countCurationTokens(IDomeo domeo, MAnnotation annotation) {
        
        Map<String, Integer> tokens= new HashMap<String, Integer>();
        String myToken="";
        
        for(MAnnotation item: annotation.getAnnotatedBy()) {
            if(item instanceof MCurationToken) {
                if(item.getCreator().getUri().equals(domeo.getAgentManager().getUserPerson().getUri())) {
                    myToken = ((MCurationToken)item).getStatus();
                }
            } else {
                String token = ((MCurationToken)item).getStatus();
                if(tokens.containsKey(token)) {
                    Integer count = tokens.get(token);
                    count++;
                    tokens.put(token, count);
                } else {
                    tokens.put(token, 1);
                }
            }
        }
        
        return myToken;
    }
}
