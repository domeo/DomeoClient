package domeo.web.parsing.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Test {

	/**
	 * test web crawling
	 */
	public static void main(String[] args) {

		String url1 = "http://dailymed.nlm.nih.gov/dailymed/lookup.cfm?setid=ab047628-67d0-4a64-8d77-36b054969b44";
		String path1 = "/tests/spls/";
		String filename1 = "Warfarin";
		saveDailymedPages(url1, path1, filename1);

		String url2 = "http://dailymed.nlm.nih.gov/dailymed/lookup.cfm?setid=1086a7b4-b89b-4bee-8120-5f752626c046";
		String path2 = "/tests/spls/";
		String filename2 = "Clopidogrel";
		saveDailymedPages(url2, path2, filename2);

	}

	// save dailymed webpage to local folder
	public static void saveDailymedPages(String url, String local_dir,
			String filename) {

		WebParsing wp = new WebParsing();
		FileOperation fo = new FileOperation();
		String htmlSource = "";

		try {
			htmlSource = WebParsing.getHtmlSource(url);
			System.out.println("html:" + htmlSource);

			String source = fo.replaceInHtmlSource(htmlSource, "href=\"/",
					"href=\"");

			fo.createFile(source, local_dir, "\\" + filename + ".html");

			// fetch css styles
			List<String> css_dir = wp.getCssLink(htmlSource);
			fetchFilesFromSource(css_dir, local_dir, "css");

			// fetch images
			//List<String> image_dir = wp.getImageLink(htmlSource);
			//List<String> temp = image_dir.subList(0, 1);
			//fetchFilesFromSource(temp, local_dir, "png");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// fetch files in String from html source
	// for css javascript 
	public static void fetchFilesFromSource(List<String> filedir,
			String local_dir, String fileformat) {
		WebParsing wp = new WebParsing();

		List<String> completedurl = wp.completeURL(filedir);

		for (String s : completedurl)
			System.out.println("file url: " + s);

		for (int i = 0; i < filedir.size() && i < completedurl.size(); i++) {
			String completeURL = completedurl.get(i);
			String dir = local_dir + filedir.get(i);
			try {
				saveFileFromURL(completeURL, dir, fileformat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// save contents from URL to local with original path
	public static void saveFileFromURL(String url, String pathandfile,
			String fileformat) throws IOException {

		FileOperation fo = new FileOperation();

		// get source from URL
		String source = WebParsing.getHtmlSource(url);

		// get file name
		int index = url.indexOf("/dailymed/");
		String postfix = url.substring(index);
		String filename = FileOperation.getFileNameFromPath(postfix);

		String nameNoParams = fo.removeParameter(filename, fileformat);

		// get path
		int cut = pathandfile.indexOf(nameNoParams);
		String path = pathandfile.substring(0, cut);
		if (!path.startsWith("/")) {
			path = '/' + path;
		}

		fo.createFile(source, path, nameNoParams);
	}
}
