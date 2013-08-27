package org.mindinformatics.gwt.services.profiles.src;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class JsonProfilesServiceImpl extends HttpServlet {

	public static SimpleDateFormat dayTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("POST - URL : " + req.getRequestURL().toString() + "?" + req.getQueryString());
		Enumeration paramNames = req.getParameterNames();
		String paramName, paramValue;
		System.out.println("Params");
		while(paramNames.hasMoreElements()){
			paramName = (String) paramNames.nextElement();
			paramValue = req.getParameter(paramName);
			System.out.println(paramName+"-"+paramValue);
		}
		String content = req.getReader().readLine();
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(content);
		System.out.println(el);
		System.out.println(req.getRemoteAddr());
		
		resp.setHeader("Content-type", "application/json");
		PrintWriter out = resp.getWriter();  
		out.println(content);  
		out.flush();
		out.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("URL : " + req.getRequestURL().toString() + "?" + req.getQueryString());
		String pathInfo = req.getPathInfo(); 
		System.out.println("PATH: " + pathInfo);
		if(pathInfo.indexOf("/")==0 && pathInfo.lastIndexOf("/")>0 && pathInfo.length()>pathInfo.lastIndexOf("/")+1) {
			String username = pathInfo.substring(1, pathInfo.lastIndexOf("/"));
			System.out.println(":: username: " + username);
			String request = pathInfo.substring(pathInfo.lastIndexOf("/")+1);
			System.out.println(":: request: " + pathInfo.substring(pathInfo.lastIndexOf("/")+1));
		
			MProfile profile = getProfileOne();
			
			String format = req.getParameter("format");
			System.out.println(":: format: " + format);
			if(format.equals("json") && username.equals("admin")) {
				if(request.equals("info")) {
					PrintWriter out = resp.getWriter();

					out.print("[{\"createdOn\":\"08/19/2013 09:46:12 -0400\","+
						"\"createdBy\":[{\"@type\":\"foafx:Person\",\"foafx:name\":\"Dr. John Doe\",\"@id\":\"8a70347a4096d318014096d369480000\"}],"+
						"\"plugins\":[{\"status\":\"disabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.qualifier\"}," +
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies\"},"+
							"{\"status\":\"disabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.micropubs\"},"+
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.spls\"},"+
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.pubmed\"},"+"" +
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral\"},"+
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.omim\"},"+
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.bioportal\"},"+
							"{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.client.component.clipboard\"}],"+
						"\"description\":\"All the tools that Domeo has to offer for biomedicine\","+
						"\"name\":\"Complete Biomedical Profile\","+
						"\"uuid\":\"8a70347a4096d318014096d36a5c0008\"}]");					
					out.flush();
				} else if (request.equals("all")) {
					PrintWriter out = resp.getWriter();
					
					out.print("[{\"createdOn\":\"08/19/2013 09:46:12 -0400\","+
						"\"createdBy\":[{\"@type\":\"foafx:Person\",\"foafx:name\":\"Dr. John Doe\",\"@id\":\"8a70347a4096d318014096d369480000\"}],"+
						"\"plugins\":[{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.qualifier\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.annotation.micropubs\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.pubmed\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.omim\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.plugins.resource.bioportal\"},{\"status\":\"enabled\",\"name\":\"org.mindinformatics.gwt.domeo.client.component.clipboard\"}],\"description\":\"All the tools that Domeo has to offer for biomedicine\",\"name\":\"Complete Biomedical Profile\",\"uuid\":\"8a70347a4096d318014096d36a5c0008\"},{\"createdOn\":\"08/19/2013 09:46:12 -0400\",\"createdBy\":[{\"@type\":\"foafx:Person\",\"foafx:name\":\"Dr. John Doe\",\"@id\":\"8a70347a4096d318014096d369480000\"}],\"plugins\":[],\"description\":\"Simple profile: notes and commenting\",\"name\":\"Simple profile\",\"uuid\":\"8a70347a4096d318014096d36b1b0009\"}]");
					
//					out.println('[');
//					
//					out.println("  {");
//					out.print("    \"uuid\": \"");
//					out.print(profile.getUuid());
//					out.println("\",");
//					out.print("    \"name\": \"");
//					out.print(profile.getName());
//					out.print("\",");
//					out.print("    \"description\": \""); 
//					out.print(profile.getDescription());
//					out.println("\",");
//					out.print("    \"createdOn\": \"");
//					out.print(dayTime.format(profile.getLastSavedOn()));
//					out.println("\",");
//					out.println("    \"createdBy\": [");
//					out.println("      {");
//					out.print("        \"@id\": \"");
//					out.print(profile.getLastSavedBy().getUuid());
//					out.println("\",");
//					out.print("    \"@type\": \"");
//					out.print("foafx:Person");
//					out.println("\",");
//					out.print("        \"foafx:name\": \"");
//					out.print(profile.getLastSavedBy().getName());
//					out.println("\"");
//					out.println("      }");
//					out.println("    ],");
//					out.println("    \"statusplugins\": [");
//					
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.annotation.qualifier");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.annotation.micropubs");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmed");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.omim");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					out.println("       {");
//					out.print("         \"uuid\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.bioportal");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.client.component.clipboard");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println(" \"}   ]");
//					out.println("  },");
//					
//					MProfile profile2 = getProfileTwo();
//					out.println("  {");
//					out.print("    \"uuid\": \"");
//					out.print(profile2.getUuid());
//					out.println("\",");
//					out.print("    \"name\": \"");
//					out.print(profile2.getName());
//					out.print("\",");
//					out.print("    \"description\": \""); 
//					out.print(profile2.getDescription());
//					out.println("\",");
//					out.print("    \"createdOn\": \"");
//					out.print(dayTime.format(profile2.getLastSavedOn()));
//					out.println("\",");
//					out.println("    \"createdBy\": [");
//					out.println("      {");
//					out.print("        \"@id\": \"");
//					out.print(profile2.getLastSavedBy().getUuid());
//					out.println("\",");
//					out.print("    \"@type\": \"");
//					out.print("foafx:Person");
//					out.println("\",");
//					out.print("        \"foafx:name\": \"");
//					out.print(profile2.getLastSavedBy().getName());
//					out.println("\"");
//					out.println("      }");
//					out.println("    ],");
//					out.println("    \"statusplugins\": [");
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmed");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       },");
//					out.println("       {");
//					out.print("         \"name\": \"");
//					out.print("org.mindinformatics.gwt.domeo.plugins.resource.bioportal");
//					out.println("\",");
//					out.print("         \"status\": \"");
//					out.print("enabled");
//					out.println("\"");
//					out.println("       }");
//					out.println("    ]");
//					out.println("  }");
//					
//					out.println(']');
					out.flush();
				}
			} else {
				PrintWriter out = resp.getWriter();
				out.println("unrecognized format request");
				out.flush();
			}
		}
	}
	
	private MProfile getProfileOne() {
		MProfile profile = new MProfile();
		profile.setName("Complete Bio Profile");
		profile.setDescription("All the tools you need for biocuration");
		profile.setUuid("4fa09e38adb4d0.96200877");
		profile.setLastSavedOn(new Date());
		MAgentPerson person = new MAgentPerson();
		person.setName("Dr. Maurizio Mosca");
		person.setUuid("maurizio.mosca");
		profile.setLastSavedBy(person);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.opentrials", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.annotation.qualifier", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.annotation.spls", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.omim", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmed", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.bioportal", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.bioportal", MProfile.PLUGIN_ENABLED);
		return profile;
	}
	
	private MProfile getProfileTwo() {
		MProfile profile = new MProfile();
		profile.setName("Simple Bio Profile");
		profile.setDescription("A few tools to start");
		profile.setUuid("4fa09e38adb4d0.96200878");
		profile.setLastSavedOn(new Date());
		MAgentPerson person = new MAgentPerson();
		person.setName("Dr. Paolo Ciccarese");
		person.setUuid("paolo.ciccarese");
		profile.setLastSavedBy(person);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.opentrials", MProfile.PLUGIN_DISABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.omim", MProfile.PLUGIN_DISABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmed", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.bioportal", MProfile.PLUGIN_ENABLED);
		return profile;
	}
}
