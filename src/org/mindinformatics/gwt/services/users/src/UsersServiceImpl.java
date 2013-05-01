package org.mindinformatics.gwt.services.users.src;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindinformatics.gwt.framework.component.users.service.UsersService;
import org.mindinformatics.gwt.framework.component.users.src.UsersFactory;
import org.mindinformatics.gwt.framework.component.users.src.defaults.DefaultUser;
import org.mindinformatics.gwt.framework.component.users.src.defaults.DefaultUsersGroup;
import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class UsersServiceImpl extends RemoteServiceServlet implements UsersService {
	
	public static SimpleDateFormat day = new SimpleDateFormat("MM/dd/yyyy Z");
	
	@Override
	public IUser getUserInfo(String username) throws IllegalArgumentException {
		System.out.println("Gwt:UsersServiceImpl:getUserInfo():"+System.currentTimeMillis());
		IUser user;
		if(username.equals("paolo.ciccarese")) {
			UsersFactory usersFactory = new UsersFactory(); 
			try {
				user = usersFactory.createUser(username, "http://www.commonsemantics.com/account/paolociccarese", 
					"Paolo Ciccarese", day.parse("05/20/2011 -0500"));
			} catch (ParseException e) {
				user = new DefaultUser();
			}
		} else {
			user = new DefaultUser();
		}
		return user;
	}

	@Override
	public Set<IUserGroup> getUserGroups(String username)
			throws IllegalArgumentException {
		System.out.println("Gwt:UsersServiceImpl:getUserGroups():"+System.currentTimeMillis());
		UsersFactory usersFactory = new UsersFactory(); 
		Set<IUserGroup> groups = new HashSet<IUserGroup>();
		if(username.equals("paolo.ciccarese")) {
			groups.add(
				usersFactory.createGroup("mind", "http://www.commonsemantics.com/group/mind", "MIND", 
					"MIND Informatics Group", new Date(), "4", true, true)
			);
			groups.add(
				usersFactory.createGroup("nif", "http://www.commonsemantics.com/group/nif", "NIF", 
					"Neuroscience Informaiton Framework", new Date(), "1", true, true)
			);
		} else {
			groups.add(new DefaultUsersGroup());
		}
		return groups;
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
		
			String format = req.getParameter("format");
			System.out.println(":: format: " + format);
			if(format.equals("json") && username.equals("paolo.ciccarese")) {
				if(request.equals("info")) {
					PrintWriter out = resp.getWriter();
					out.println('[');
		
					out.println("  {");
					out.print("    \"uri\": \"");
					out.print("http://paolociccarese.info");
					out.println("\",");
					out.print("    \"username\": \"");
					out.print("paolo.ciccarese");
					out.println("\",");
					out.print("    \"screenname\": \"");
					out.print("Dr. Paolo Ciccarese");
					out.println("\"");
					out.println("  },");
					
					out.println(']');
					out.flush();
				} else if (request.equals("groups")) {
					PrintWriter out = resp.getWriter();
					out.println('[');
					// TODO groups
					out.println("  {");
					out.print("    \"uuid\": \"");
					out.print("nif");
					out.println("\",");
					out.print("    \"uri\": \"");
					out.print("urn:domeo:group:nif");
					out.println("\",");
					out.print("    \"name\": \"");
					out.print("NIF");
					out.println("\",");
					out.print("    \"description\": \"");
					out.print("Neuroscience Information Framework");
					out.println("\",");
					out.print("    \"mermberscount\": \"");
					out.print("1");
					out.println("\",");
					out.println("    \"roles\": [");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.domeo.uris.roles.Admin");
					out.println("\",");
					out.print("         \"name\": \"");
					out.print("Admin");
					out.println("\"");
					out.println("       },");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.domeo.uris.roles.Curator");
					out.println("\",");
					out.print("         \"name\": \"");
					out.print("Curator");
					out.println("\"");
					out.println("       }");
					out.println("    ]");
					out.print(",");
					out.println("    \"visibility\": [");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.domeo.uris.visibility.Public");
					out.println("\",");
					out.print("         \"name\": \"");
					out.print("Public");
					out.println("\"");
					out.println("       }");
					out.println("    ]");
					out.println("  },");
					
					out.println("  {");
					out.print("    \"uuid\": \"");
					out.print("mind");
					out.println("\",");
					out.print("    \"uri\": \"");
					out.print("urn:domeo:group:mind");
					out.println("\",");
					out.print("    \"name\": \"");
					out.print("MIND");
					out.println("\",");
					out.print("    \"description\": \"");
					out.print("MIND Informatics Group");
					out.println("\",");
					out.print("    \"mermberscount\": \"");
					out.print("4");
					out.println("\",");
					out.println("    \"roles\": [");
					out.println("       {");
					out.print("         \"uuid\": \"");
					out.print("org.mindinformatics.domeo.uris.roles.Member");
					out.println("\",");
					out.print("         \"name\": \"");
					out.print("Member");
					out.println("\"");
					out.println("       }");
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
}
