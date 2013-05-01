package org.mindinformatics.gwt.domeo.component.linkeddata.digesters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LinkedDataDigestersManager {

	private HashMap<String, ITrustedResourceDigester> 
		linkedDataDigesters = new HashMap<String, ITrustedResourceDigester>();
	
	public boolean registerLnkedDataDigester(ITrustedResourceDigester digester) {
		if(!linkedDataDigesters.containsKey(digester.getClass().getName())) {
			linkedDataDigesters.put(digester.getClass().getName(), digester);
			return true;
		}
		return false;
	}
	
	public List<ITrustedResourceDigester> getLnkedDataDigesters(MLinkedResource resource) {
		List<ITrustedResourceDigester> digesters = new ArrayList<ITrustedResourceDigester>();
		Iterator<ITrustedResourceDigester> it = linkedDataDigesters.values().iterator();
		while(it.hasNext()) {
			ITrustedResourceDigester digester = it.next();
			if(digester.doesDigesterApply(resource)) 
				digesters.add(digester);
		}
		return digesters;
	}
}
