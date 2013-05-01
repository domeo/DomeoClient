package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;

public interface IAntibodiesSearchContainer {

	public void addAntibody(MAntibody term);
	public ArrayList<MAntibody> getAntibodies();
}
