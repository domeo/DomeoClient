package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLType;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
    public class MSPLsAnnotation extends MAnnotation implements IPharmgxOntology{

	private MSPLPharmgxUsage pharmgxUsage;
	private SPLType type;
	private MContentAsRdf body;
	
	public SPLType getType() {
		return type;
	}
	public void setType(SPLType type) {
		this.type = type;
	}
	
	public MContentAsRdf getBody() {
		return body;
	}
	
	public void setBody(MContentAsRdf body) {
		this.body = body;
	}
	
	public MLinkedResource getDrugOfInterest() {
		return pharmgxUsage.getDrugOfInterest();
	}

	public void setDrugOfInterest(MLinkedResource drugofinterest) {
		pharmgxUsage.setDrugOfInterest(drugofinterest);
	}
	
	public MLinkedResource getProductLabelSelection() {
		return pharmgxUsage.getProductLabelSelection();
	}

	public void setProductLabelSelection(MLinkedResource productlabelselection) {
		pharmgxUsage.setProductLabelSelection(productlabelselection);
	}
	
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
	
	public MLinkedResource getVariant(){
	    return pharmgxUsage.getVariant();
	}

	public void setVariant(MLinkedResource variant){
	    pharmgxUsage.setVariant(variant);
	}
	
	public MLinkedResource getTest(){
	    return pharmgxUsage.getTest();
	}

	public void setTest(MLinkedResource test){
	    pharmgxUsage.setTest(test);
	}
	
	public String getOtherVariant() {
	    return pharmgxUsage.getOtherVariant();
	}

	public void setOtherVariant(String othervariant) {
	    pharmgxUsage.setOtherVariant(othervariant);
	}
	
	public String getOtherTest() {
	    return pharmgxUsage.getOtherTest();
	}

	public void setOtherTest(String othertest) {
	    pharmgxUsage.setOtherTest(othertest);
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
