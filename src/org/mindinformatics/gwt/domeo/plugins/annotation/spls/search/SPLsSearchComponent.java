package org.mindinformatics.gwt.domeo.plugins.annotation.spls.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ISearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MTextSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
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
	public boolean filterByText(MAnnotation annotation, String textSearch) {

		if (super.filterByText(annotation, textSearch))
			return true;

		if (annotation instanceof MSPLsAnnotation) {

			_domeo.getLogger().debug(this, "search annotation in SPL");

			String textSearchL = textSearch.toLowerCase();

			// if search string matches selector
			boolean boo_exact = ((MTextSelector) annotation.getSelector())
					.getExact().toLowerCase().contains(textSearchL);

			return boo_exact
					|| filterByFields((MSPLsAnnotation) annotation, textSearchL);
		}

		return false;

	}

	// if search string matches any of value of fields
	public boolean filterByFields(MSPLsAnnotation annot, String textSearchL) {

		boolean boo_DofI = annot.getDrugOfInterest().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_HGNCGene = annot.getHGNCGeneSymbol().getLabel()
				.toLowerCase().contains(textSearchL);
		boolean boo_PLS = annot.getProductLabelSelection().getLabel()
				.toLowerCase().contains(textSearchL);
		boolean boo_pheno = annot.getPhenotype().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_testRes = annot.getTest().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_alleles = annot.getAllelesbody().toLowerCase()
				.contains(textSearchL);
		boolean boo_pk = annot.getPKImpact().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_pd = annot.getPdImpact().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_drugRec = annot.getDrugRec().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_doseRec = annot.getDoseRec().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_monitRec = annot.getMonitRec().getLabel().toLowerCase()
				.contains(textSearchL);
		boolean boo_testRec = annot.getTestRec().getLabel().toLowerCase()
				.contains(textSearchL);

		boolean boo_statement = false;
		for (MLinkedResource r : annot.getStatements()) {
			if (r.getLabel().toLowerCase().contains(textSearchL))
				boo_statement = true;
			break;
		}

		boolean boo_mediCond = annot.getMedconditbody().toLowerCase()
				.contains(textSearchL);
		boolean boo_commnt = annot.getComment().toLowerCase()
				.contains(textSearchL);

		return boo_DofI || boo_HGNCGene || boo_PLS || boo_pheno || boo_testRes
				|| boo_alleles || boo_pk || boo_pd || boo_drugRec
				|| boo_doseRec || boo_monitRec || boo_testRec || boo_statement
				|| boo_mediCond || boo_commnt;

	}
}
