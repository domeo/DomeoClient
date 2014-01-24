package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
    public class MSPLsAnnotation extends MAnnotation implements IPharmgxOntology{

	private MSPLPharmgxUsage pharmgxUsage;
	
	public MLinkedResource getBiomarkers() {
		return pharmgxUsage.getBiomarkers();
	}

	public void setBiomarkers(MLinkedResource biomarker) {
		pharmgxUsage.setBiomarkers(biomarker);
	}

	public MSPLPharmgxUsage getPharmgxUsage() {
	    return pharmgxUsage;
	}

	public void setPharmgxUsage(MSPLPharmgxUsage pharmgxUsage) {
	    this.pharmgxUsage = pharmgxUsage;
	}

	public String getComment() {
	    return pharmgxUsage.getComment();
	}

	public void setComment(String comment) {
	    pharmgxUsage.setComment(comment);
	}

	public String getText() {
	    return pharmgxUsage.getPharmgx().getLabel();
	}
	
	public MLinkedResource getPKImpact(){
	    return pharmgxUsage.getPkImpact();
	}

	public void setPKImpact(MLinkedResource pkImpact){
	    pharmgxUsage.setPkImpact(pkImpact);
	}
	
	public MLinkedResource getPdImpact(){
	    return pharmgxUsage.getPdImpact();
	}

	public void setPdImpact(MLinkedResource pdImpact){
	    pharmgxUsage.setPdImpact(pdImpact);
	}
	
	public MLinkedResource getDoseRec(){
	    return pharmgxUsage.getDoseRec();
	}

	public void setDoseRec(MLinkedResource doseRec){
	    pharmgxUsage.setDoseRec(doseRec);
	}
	
	public MLinkedResource getDrugRec(){
	    return pharmgxUsage.getDrugRec();
	}

	public void setDrugRec(MLinkedResource drugRec){
	    pharmgxUsage.setDrugRec(drugRec);
	}
	
	public MLinkedResource getMonitRec(){
	    return pharmgxUsage.getMonitRec();
	}

	public void setMonitRec(MLinkedResource monitRec){
	    pharmgxUsage.setMonitRec(monitRec);
	}
	
	public MLinkedResource getTestRec(){
	    return pharmgxUsage.getTestRec();
	}

	//statements refers to ...
	public void setStatements(Set<MLinkedResource> statement){
	    pharmgxUsage.setStatements(statement);
	}
	
	public Set<MLinkedResource> getStatements(){
	    return pharmgxUsage.getStatements();
	}

	public void setTestRec(MLinkedResource testRec){
	    pharmgxUsage.setTestRec(testRec);
	}
	
	public Set<MLinkedResource> getPrevalence() {
		return pharmgxUsage.getPrevalence();
	}

	public void setPrevalence(Set<MLinkedResource> prevalence) {
		pharmgxUsage.setPrevalence(prevalence);
	}

	public String getAllelesbody() {
		return pharmgxUsage.getAllelesbody();
	}

	public void setAllelesbody(String Allelesbody) {
		pharmgxUsage.setAllelesbody(Allelesbody);
	}
	
	public String getMedconditbody() {
	    return pharmgxUsage.getMedconditbody();
	}

	public void setMedconditbody(String mediconditbody) {
	    pharmgxUsage.setMedconditbody(mediconditbody);
	}
	
	@Override
	    public String getLabel() {
	    return LABEL;
	}
	
	@Override
	    public String getAnnotationType() {
	    return TYPE;
	}
    }
