package org.mindinformatics.gwt.domeo.plugins.annotation.spls.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLsFactory;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentGroup;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) {

		
		String path = "C:\\Users\\ivan\\Desktop\\STS_WP\\DomeoClient\\existing-annotation\\Warfarin-ab047628-67d0-4a64-8d77-36b054969b44";
		FileOperation fo = new FileOperation();
		String jsonString = fo.readFileFromPath(path);

		JSONObject jsonObj = JsonParser.parseJsonFromString(jsonString);

		List<MSPLsAnnotation> ann = new ArrayList<MSPLsAnnotation>();
		JsonParser.loadJsonIntoPOJO(jsonObj);
		
		System.out.println(ann.size());
	}
	
	

}
