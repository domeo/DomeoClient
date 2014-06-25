package org.mindinformatics.gwt.domeo.plugins.annotation.spls.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ISearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MTextSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class SPLsSearchComponent extends ASearchComponent implements
		ISearchComponent {

	public SPLsSearchComponent(IDomeo domeo) {
		super(domeo);
	}

	/**
	 * The search on the text match is NOT case sensitive.
	 */

	@Override
	/**
	 * The search on the text match is NOT case sensitive.
	 */
	public boolean filterByText(MAnnotation annotation, String textSearch) {
		if (super.filterByText(annotation, textSearch))
			return true;
		if (annotation instanceof MSPLsAnnotation) {

			// System.out.println("filterByText SPL");

			return filterByFields((MSPLsAnnotation) annotation,
					textSearch.toLowerCase());

			// return ((MSPLsAnnotation) annotation).getText().toLowerCase()
			// .contains(textSearch.toLowerCase());
		}
		return false;
	}

	// if search string matches any of value of fields
	public boolean filterByFields(MSPLsAnnotation annot, String textSearchL) {

		// System.out.println("search:" + textSearchL + "| DOF:" +
		// annot.getDrugOfInterest().getLabel().toLowerCase());

		if (annot.getDrugOfInterest() != null) {
			boolean boo_DofI = annot.getDrugOfInterest().getLabel()
					.toLowerCase().contains(textSearchL);
			if (boo_DofI)
				return true;
		}

		if (annot.getHGNCGeneSymbol() != null) {
			boolean boo_HGNCGene = annot.getHGNCGeneSymbol().getLabel()
					.toLowerCase().contains(textSearchL);
			if (boo_HGNCGene)
				return true;
		}

		if (annot.getProductLabelSelection() != null) {
			boolean boo_PLS = annot.getProductLabelSelection().getLabel()
					.toLowerCase().contains(textSearchL);
			if (boo_PLS)
				return true;
		}

		if (annot.getPhenotype() != null) {
			boolean boo_pheno = annot.getPhenotype().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_pheno)
				return true;
		}

		if (annot.getTest() != null) {
			boolean boo_testRes = annot.getTest().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_testRes)
				return true;
		}

		if (annot.getAllelesbody() != null
				&& !annot.getAllelesbody().trim().equals("")) {
			boolean boo_alleles = annot.getAllelesbody().toLowerCase()
					.contains(textSearchL);
			if (boo_alleles)
				return true;
		}

		if (annot.getPKImpact() != null) {
			boolean boo_pk = annot.getPKImpact().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_pk)
				return true;
		}

		if (annot.getPdImpact() != null) {
			boolean boo_pd = annot.getPdImpact().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_pd)
				return true;
		}

		if (annot.getDrugRec() != null) {
			boolean boo_drugRec = annot.getDrugRec().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_drugRec)
				return true;
		}

		if (annot.getDoseRec() != null) {
			boolean boo_doseRec = annot.getDoseRec().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_doseRec)
				return true;
		}

		if (annot.getMonitRec() != null) {
			boolean boo_monitRec = annot.getMonitRec().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_monitRec)
				return true;
		}

		if (annot.getTestRec() != null) {
			boolean boo_testRec = annot.getTestRec().getLabel().toLowerCase()
					.contains(textSearchL);
			if (boo_testRec)
				return true;
		}

		for (MLinkedResource r : annot.getStatements()) {
			if (r.getLabel().toLowerCase().contains(textSearchL)) {
				return true;
			}
		}

		if (annot.getMedconditbody() != null
				&& !annot.getMedconditbody().trim().equals("")) {
			boolean boo_mediCond = annot.getMedconditbody().toLowerCase()
					.contains(textSearchL);
			if (boo_mediCond)
				return true;
		}

		if (annot.getComment() != null && !annot.getComment().trim().equals("")) {
			boolean boo_commnt = annot.getComment().toLowerCase()
					.contains(textSearchL);
			if (boo_commnt)
				return true;
		}

		// System.out.println("filterByFields:");

		return false;

	}
}
