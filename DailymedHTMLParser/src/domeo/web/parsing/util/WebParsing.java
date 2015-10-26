package domeo.web.parsing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParsing {

	// fetch source of whole web page by url
	public static String getHtmlSource(final String htmlurl) throws IOException {
		URL url;
		String temp;
		final StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlurl);
			// read web contents
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "utf-8"));

			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			System.out.println("the format of url is wrong, reenter please");
			me.getMessage();
			throw me;
		} catch (final IOException e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	// find css style by match tag <link> and end with .css
	public List<String> getCssLink(String htmlSource) {

		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(htmlSource);

		Elements links = doc.getElementsByTag("link");
		for (Element link : links) {
			String href = link.attr("href");
			if (href.contains(".css"))
				list.add(href);
		}
		return list;
	}

	// find imges by match tag <img> 
	public List<String> getImageLink(String htmlSource) {

		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(htmlSource);

		Elements links = doc.getElementsByTag("img");
		for (Element link : links) {
			String href = link.attr("src");
			list.add(href);
		}
		return list;
	}

	// complete URL fetched from dailymed
	public List<String> completeURL(List<String> links) {
		List<String> list = new ArrayList<String>();
		for (String str : links) {

			if (str.contains("dailymed")) {
				String url = "http://dailymed.nlm.nih.gov" + str;
				list.add(url);
			} else {
				String url = "http://dailymed.nlm.nih.gov/dailymed/" + str;
				list.add(url);
			}
		}
		return list;
	}

	// compensate daillymed if omitted by CSS URL
	public void replaceHtmlPage(String source, String replaceStr) {
		source.replaceAll(replaceStr, "/dailymed/" + replaceStr);
	}

}
