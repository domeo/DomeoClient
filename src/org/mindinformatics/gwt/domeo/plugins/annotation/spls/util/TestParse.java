package org.mindinformatics.gwt.domeo.plugins.annotation.spls.util;

import static org.junit.Assert.*; //requires junit4

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject; //requires JSON-Lib

import org.junit.Test;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class TestParse extends GWTTestCase{
	
	private JsonParser jsonpaser;

	@Test
	public void test() {
		
		String path = "C:\\Users\\ivan\\Desktop\\STS_WP\\DomeoClient\\existing-annotation\\Warfarin-ab047628-67d0-4a64-8d77-36b054969b44";
		FileOperation fo = new FileOperation();
		String jsonString = fo.readFileFromPath(path);

		JSONObject jsonObj = JsonParser.parseJsonFromString(jsonString);

		List<MSPLsAnnotation> ann = new ArrayList<MSPLsAnnotation>();
		JsonParser.loadJsonIntoPOJO(jsonObj);
		
		System.out.println(ann.size());
		//System.out.println("TestParse");
	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

}
