package org.mindinformatics.gwt.domeo.plugins.resource.nif.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalTextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.INifConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.INifDataRequestCompleted;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

public class StandaloneNifConnector implements INifConnector {

	@Override
	public void searchData(INifDataRequestCompleted completionCallback,
			String textQuery, String type, String vendor, String resource, int pageNumber, int pageSize)
			throws IllegalArgumentException {
		if(resource.equals("nif-0000-07730-1")) {
			ArrayList<MGenericResource> data = new ArrayList<MGenericResource>();
			
			MGenericResource antibodyRegistry = new MGenericResource("http://antibodyregistry.org", "Antibodyregistry.org");
			
			MAntibody antibody = new MAntibody(
					"http://antibodyregistry.org/Antibody12/antibodyform.html?gui_type=advanced&ab_id=2145602", 
					"Mouse Anti-MLH1 Monoclonal antibody, Unconjugated, Clone G168-15", antibodyRegistry);
			antibody.setCloneId("G168-15");
			antibody.setVendor("ABR, now sold as Thermo Scientific Pierce Antibodies");
			antibody.setOrganism("mouse");
			data.add(antibody);
			
			MAntibody antibody2 = new MAntibody(
					"http://antibodyregistry.org/Antibody12/antibodyform.html?gui_type=advanced&ab_id=784582", 
					"Mouse Anti-MLH1 Monoclonal antibody, Unconjugated, Clone g168-15", antibodyRegistry);
			antibody2.setCloneId("G168-15");
			antibody2.setVendor("Santa Cruz Biotechnology, Inc.");
			antibody2.setOrganism("mouse");
			data.add(antibody2);

			completionCallback.returnData(data);
		} else {
		    ArrayList<MGenericResource> data = new ArrayList<MGenericResource>();
		    completionCallback.returnData(data);
		}
	}

	@Override
	public void annotate(
			IBioPortalTextminingRequestCompleted completionCallback,
			String url, String textContent, String include, String exclude)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
