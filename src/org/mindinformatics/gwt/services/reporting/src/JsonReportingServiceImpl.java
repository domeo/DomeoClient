package org.mindinformatics.gwt.services.reporting.src;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class JsonReportingServiceImpl extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("POST - URL : " + req.getRequestURL().toString() + "?" + req.getQueryString() + " - from IP " + req.getRemoteAddr());
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
		
		resp.setHeader("Content-type", "application/json");
		PrintWriter out = resp.getWriter();  
		out.println(content);  
		out.flush();
		out.close();
	}
}
