package org.mindinformatics.gwt.domeo.model.selectors;

import java.util.ArrayList;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SelectorUtils {

	public static boolean isOnTextFragment(ArrayList<MSelector> selectors) {
		return (selectors.size()==1 && selectors.get(0) instanceof MTextSelector);
	}
	
	public static boolean isOnMultipleTargets(ArrayList<MSelector> selectors) {
		return (selectors.size()>1);
	}
	
	public static boolean isOnResourceTarget(ArrayList<MSelector> selectors) {
		return (selectors.size()==1 && selectors.get(0) instanceof MTargetSelector);
	}
	
	public static boolean isOnAnnotation(ArrayList<MSelector> selectors) {
		return (selectors.size()==1 && selectors.get(0) instanceof MAnnotationSelector);
	}
	
	public static String getMatch(MSelector selector) {
		if(selector instanceof MTextSelector) {
			return ((MTextSelector)selector).getExact();
		} 
		throw new RuntimeException("Selector not for text " + selector.getClass().getName());
	}
	
	public static String getMatch(ArrayList<MSelector> selectors) {
		int counter = 0;
		StringBuffer matches = new StringBuffer();
		for(MSelector selector: selectors) {
			if(selector instanceof MTextSelector) {
				matches.append("'" + ((MTextSelector)selector).getExact() + "'");
			} 
			counter++;
			if(counter<selectors.size()) matches.append(", "); 
		}
		return matches.toString();
		// throw new RuntimeException("Selector not for text " + selector.getClass().getName());
	}
	
	public static String getPrefix(MSelector selector) {
		if(selector instanceof MTextSelector) {
			if(selector instanceof MTextQuoteSelector) {
				return ((MTextQuoteSelector)selector).getPrefix();
			}
		} 
		throw new RuntimeException("Selector not for text " + selector.getClass().getName());
	}
	
	public static String getSuffix(MSelector selector) {
		if(selector instanceof MTextSelector) {
			if(selector instanceof MTextQuoteSelector) {
				return ((MTextQuoteSelector)selector).getSuffix();
			}
		} 
		throw new RuntimeException("Selector not for text " + selector.getClass().getName());
	}
}
