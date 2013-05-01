package org.mindinformatics.gwt.services.agents.src;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.component.agents.service.AgentsService;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.component.agents.src.defaults.DefaultPerson;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class AgentsServiceImpl extends RemoteServiceServlet implements AgentsService {

	@Override
	public MAgentSoftware getSoftwareInfo(String name, String version) throws IllegalArgumentException {
		System.out.println("Gwt:AgentsServiceImpl:getSoftwareInfo():"+System.currentTimeMillis());
		AgentsFactory agentsFactory = new AgentsFactory(); 
		MAgentSoftware software = agentsFactory.createAgentSoftware(name, version);
		software.setHomepage("http://www.annotationframework.org");
		return software;
	}

	@Override
	public MAgentPerson getPersonInfo(String username) throws IllegalArgumentException {
		System.out.println("Gwt:AgentsServiceImpl:getPersonInfo():"+System.currentTimeMillis());
		MAgentPerson person;
		if(username.equals("paolo.ciccarese")) {
			AgentsFactory agentsFactory = new AgentsFactory();
			person = agentsFactory.createAgentPerson(
				"http://www.commonsemantics.com/agent/paolociccarese", 
				"paolo.ciccarese@gmail.com", "Dr.", "Paolo Ciccarese", "Paolo", "Nunzio", "Ciccarese", 
				"http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg");
		} else {
			person = new DefaultPerson();
		}
		return person;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("URL : " + req.getRequestURL().toString() + "?" + req.getQueryString());
		String pathInfo = req.getPathInfo();     
		System.out.println("PATH: " + pathInfo);
		if(pathInfo.indexOf("/")==0 && pathInfo.lastIndexOf("/")>0 && pathInfo.length()>pathInfo.lastIndexOf("/")+1) {
			String username = pathInfo.substring(1, pathInfo.lastIndexOf("/"));
			System.out.println(":: agent: " + username);
			String request = pathInfo.substring(pathInfo.lastIndexOf("/")+1);
			System.out.println(":: request: " + pathInfo.substring(pathInfo.lastIndexOf("/")+1));
		
			String format = req.getParameter("format");
			System.out.println(":: format: " + format);
			if(format.equals("json") && username.equals("paolo.ciccarese")) {
				if(request.equals("info")) {
					PrintWriter out = resp.getWriter();
					out.println('[');
		
					out.println("  {");
					out.print("    \"@id\": \"");
					out.print("urn:person:uuid:paolociccarese");
					out.println("\",");
					out.print("    \"@type\": \"");
					out.print("foafx:Person");
					out.println("\",");
					out.print("    \"uri\": \"");
					out.print("urn:person:uuid:paolociccarese");
					out.println("\",");
					out.print("    \"domeo:uuid\": \"");
					out.print("paolo.ciccarese");
					out.println("\",");
					out.print("    \"foafx:email\": \"");
					out.print("paolo.ciccarese@gmail.com");
					out.println("\",");
					out.print("    \"displayname\": \"");
					out.print("Dr. Paolo Ciccarese");
					out.println("\",");
					out.print("    \"foafx:title\": \"");
					out.print("Dr.");
					out.println("\",");
					out.print("    \"foafx:email\": \"");
					out.print("paolo.ciccarese@gmail.com");
					out.println("\",");
					out.print("    \"foafx:name\": \"");
					out.print("Paolo Ciccarese");
					out.println("\",");
					out.print("    \"foafx:firstname\": \"");
					out.print("Paolo");
					out.println("\",");
					out.print("    \"foafx:middlename\": \"");
					out.print("Nunzio");
					out.println("\",");
					out.print("    \"foafx:lastname\": \"");
					out.print("Ciccarese");
					out.println("\",");
					out.print("    \"foafx:picture\": \"");
					out.print("http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg");
					out.println("\"");
					out.println("  },");
					
					out.println(']');
					out.flush();
				}
			} else if(format.equals("json") && username.toLowerCase().equals("domeo")) {
				PrintWriter out = resp.getWriter();
				out.println('[');
	
				out.println("  {");
				out.print("    \"@id\": \"");
				out.print("http://www.commonsemantics.com/agent/domeo_"+request);
				out.println("\",");
				out.print("    \"@type\": \"");
				out.print("foafx:Software");
				out.println("\",");
				out.print("    \"domeo:uuid\": \"");
				out.print("domeo_"+request);
				out.println("\",");
				out.print("    \"foafx:name\": \"");
				out.print("Domeo");
				out.println("\",");
				out.print("    \"foafx:version\": \"");
				out.print(request);
				out.println("\"");
				out.println("  },");
				
				out.println(']');
				out.flush();
			} else {
				PrintWriter out = resp.getWriter();
				out.println("unrecognized format request");
				out.flush();
			}
		}
	}
}
