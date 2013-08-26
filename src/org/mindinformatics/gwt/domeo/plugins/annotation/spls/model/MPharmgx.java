package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class MPharmgx extends MTrustedResource {

	String pkImpact, pdImpact, doseRec, drugRec, monitRec, testRec;
	
	public MPharmgx(String url, String label, MGenericResource source) {
		super(url, label, source);
	}
	
	public String getPkImpact() {
		return pkImpact;
	}
	public void setPkImpact(String pkImpact) {
		this.pkImpact = pkImpact;
	}

	public String getPdImpact() {
		return pdImpact;
	}
	public void setPdImpact(String pdImpact) {
		this.pdImpact = pdImpact;
	}

	public String getDoseRec() {
		return doseRec;
	}
	public void setDoseRec(String doseRec) {
		this.doseRec = doseRec;
	}

	public String getDrugRec() {
		return drugRec;
	}
	public void setDrugRec(String drugRec) {
		this.drugRec = drugRec;
	}

	public String getMonitRec() {
		return monitRec;
	}
	public void setMonitRec(String monitRec) {
		this.monitRec = monitRec;
	}

	public String getTestRec() {
		return testRec;
	}
	public void setTestRec(String testRec) {
		this.testRec = testRec;
	}
}
