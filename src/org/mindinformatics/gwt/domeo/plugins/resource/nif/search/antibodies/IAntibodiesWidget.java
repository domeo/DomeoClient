package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;

public interface IAntibodiesWidget {
	public void addAntibody(MAntibody antibody);
	
	public MAntibody getAntibody();
	
	public ArrayList<MAntibody> getAntibodiesResults();
	
	public void removeAntibody(MAntibody antibody);
	
	public String getFilterValue();
}
