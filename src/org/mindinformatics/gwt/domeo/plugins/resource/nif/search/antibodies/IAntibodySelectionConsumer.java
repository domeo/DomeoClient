package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;

public interface IAntibodySelectionConsumer {
	public String getFilterValue();
	public void addAntibody(MAntibody antibody);
	public ArrayList<MAntibody> getAntibodiesList();
}
