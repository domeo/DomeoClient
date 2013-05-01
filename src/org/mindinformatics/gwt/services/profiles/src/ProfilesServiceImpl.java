package org.mindinformatics.gwt.services.profiles.src;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.service.ProfilesService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProfilesServiceImpl extends RemoteServiceServlet implements ProfilesService {

	public static SimpleDateFormat dayTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
	
	@Override
	public ArrayList<MProfile> getUserAvailableProfiles(String username)
			throws IllegalArgumentException {
		System.out.println("Gwt:ProfilesServiceImpl:getUserAvailableProfiles():"+System.currentTimeMillis());
		ArrayList<MProfile> profiles = new ArrayList<MProfile>();
		profiles.add(getProfileOne());
		profiles.add(getProfileTwo());
		return profiles;
	}

	@Override
	public MProfile getUserCurrentProfile(String username)
			throws IllegalArgumentException {
		System.out.println("Gwt:ProfilesServiceImpl:getUserCurrentProfile():"+System.currentTimeMillis());
		return getProfileOne();
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
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.omim", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmed", MProfile.PLUGIN_ENABLED);
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
	
	/*
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
			if(format.equals("json") && username.equals("paolo.ciccarese")) {
				if(request.equals("info")) {
					PrintWriter out = resp.getWriter();
					out.println('[');
		
					out.println("  {");
					out.print("    \"uuid\": \"");
					out.print(profile.getUuid());
					out.println("\",");
					out.print("    \"name\": \"");
					out.print(profile.getName());
					out.println("\",");
					out.print("    \"createdon\": \"");
					out.print(dayTime.format(profile.getLastSavedOn()));
					out.println("\",");
					out.println("    \"statusplugins\": [");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmed");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.omim");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.bioportal");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("    ]");
					out.println("  },");
					
					out.println(']');
					out.flush();
				} else if (request.equals("all")) {
					PrintWriter out = resp.getWriter();
					out.println('[');
					
					out.println("  {");
					out.print("    \"uuid\": \"");
					out.print(profile.getUuid());
					out.println("\",");
					out.print("    \"name\": \"");
					out.print(profile.getName());
					out.println("\",");
					out.print("    \"createdon\": \"");
					out.print(dayTime.format(profile.getLastSavedOn()));
					out.println("\",");
					out.println("    \"statusplugins\": [");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmed");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.omim");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.bioportal");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("    ]");
					out.println("  },");
					
					MProfile profile2 = getProfileTwo();
					out.println("  {");
					out.print("    \"uuid\": \"");
					out.print(profile2.getUuid());
					out.println("\",");
					out.print("    \"name\": \"");
					out.print(profile2.getName());
					out.println("\",");
					out.print("    \"createdon\": \"");
					out.print(dayTime.format(profile2.getLastSavedOn()));
					out.println("\",");
					out.println("    \"statusplugins\": [");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmed");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.gwt.domeo.plugins.resource.bioportal");
					out.println("\",");
					out.print("         \"status\": \"");
					out.print("enabled");
					out.println("\"");
					out.println("       },");
					out.println("    ]");
					out.println("  },");
					
					out.println(']');
					out.flush();
				}
			} else {
				PrintWriter out = resp.getWriter();
				out.println("unrecognized format request");
				out.flush();
			}
		}
	}
	*/

}
