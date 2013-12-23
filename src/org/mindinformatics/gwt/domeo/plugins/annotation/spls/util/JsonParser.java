package org.mindinformatics.gwt.domeo.plugins.annotation.spls.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentGroup;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

public class JsonParser {
	
	/*
	 * Iteratively load jsonObject into a list of POJO(MSPLsAnnotation)
	 */
	public static List<MSPLsAnnotation> loadJsonIntoPOJO(JSONObject jsonObj) {
		List<MSPLsAnnotation> annList = new ArrayList<MSPLsAnnotation>();

		JSONObject results = (JSONObject) jsonObj.get("results");
		JSONArray bindings = (JSONArray) results.get("bindings");
		//System.out.println(bindings.toString());

		// attributes name and value
		Set<JsonBinding> set = new HashSet<JsonBinding>();

		for (int i = 0; i < bindings.size(); i++) {
			JSONObject jsonItem = bindings.getJSONObject(i);

			JsonBinding exact = new JsonBinding("exact", jsonItem.getJSONObject(
					"exact").getString("value"));
			JsonBinding target = new JsonBinding("target", jsonItem.getJSONObject(
					"target").getString("value"));
			JsonBinding prefix = new JsonBinding("prefix", jsonItem.getJSONObject(
					"prefix").getString("value"));
			JsonBinding postfix = new JsonBinding("postfix", jsonItem.getJSONObject(
					"postfix").getString("value"));
			
			if (!set.contains(exact)&&!set.contains(target)&&!set.contains(prefix)&&!set.contains(postfix)) {
				System.out.println("step1");
				set.add(exact);
				set.add(prefix);
				set.add(postfix);
				set.add(target);
				
				annList.add(parseJsonItem(jsonItem));
			}

		}

		return annList;
	}

	
	/*
	 * parse JsonObject to create MSPLsAnnotation as a POJO
	 */
	public static MSPLsAnnotation parseJsonItem(JSONObject jsonItem) {
		
		System.out.println("step2");

		MSPLsAnnotation annotation = new MSPLsAnnotation();

		try {
			
			System.out.println("step3");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date annCreatedOn = sdf.parse(jsonItem
					.getJSONObject("annCreatedOn").getString("value"));
			
			String target = jsonItem.getJSONObject("target").getString("value");
			String prefix = jsonItem.getJSONObject("prefix").getString("value");
			String suffix = jsonItem.getJSONObject("postfix")
					.getString("value");
			
			String exact = jsonItem.getJSONObject("exact").getString("value");
			String annCreatedBy = jsonItem.getJSONObject("annCreatedBy")
					.getString("value");

			/*
			 * url: The url of the Trusted Resource, label: The human readable
			 * label, description: The human readable description
			 */

			String url = jsonItem.getJSONObject("s").getString("value");
			String describes = jsonItem.getJSONObject("describes").getString(
					"value");
			String description = jsonItem.getJSONObject("description")
					.getString("value");

			MLinkedResource source_target = new MLinkedResource(url, describes,
					description);

			MAgentGroup creator = new MAgentGroup();
			creator.setName(annCreatedBy);
			creator.setUuid(target);

			MTextQuoteSelector selector = AnnotationFactory
					.createPrefixSuffixTextSelector(creator,
							(MGenericResource) source_target, exact, prefix,
							suffix);

			annotation.setCreatedOn(annCreatedOn);
			annotation.setCreator(creator);
			annotation.setSelector(selector);
			annotation.setUuid(target);
			annotation.setNewVersion(true);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return annotation;
	}
	
	/*
	 * convert from String to JSON Object for parsing
	 */
	public static JSONObject parseJsonFromString(String josntext) {
		return JSONObject.fromObject(josntext);
	}

}
