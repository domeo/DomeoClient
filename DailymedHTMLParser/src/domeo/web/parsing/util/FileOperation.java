package domeo.web.parsing.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileOperation {

	// write string into file of specified format
	public File createFile(String source, String path, String fileName) {

		String absPath = System.getProperty("user.dir") + path;
		File folder = new File(absPath);
		
		System.out.println("!!creating file: "+absPath + fileName);
		File file = new File(absPath + fileName);

		if (file.exists()) {
			System.out
					.println("***the file " + fileName + " already exists***");
			return null;
		}

		if (folder != null && !folder.exists()) {
			folder.mkdirs();
			System.out.println("create directory:" + folder.getAbsolutePath());
		}
		try {
			if (file.createNewFile())
				System.out.println("createfile:" + fileName);
			else
				System.out.println("createfile Exception:" + fileName);

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			byte[] target = source.getBytes();
			fos.write(target);
			fos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return file;
	}

	// get file name from path
	public static String getFileNameFromPath(String path) {

		if (path == "" || path.equals(" ")) {
			return null;
		}
		if (!path.contains("/"))
			return path;
		int index = path.indexOf("/");
		return getFileNameFromPath(path.substring(index + 1));
	}

	// check htmlsource if need add prefix before url of css
	public String readFile(File source) {
		try {
			if (source.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(source));

				StringBuffer htmlSource = new StringBuffer();

				String line = br.readLine();
				while (line != null) {
					htmlSource.append(line);
					htmlSource.append('\n');
					line = br.readLine();
				}
				return htmlSource.toString();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// If omitted, add dailymed before URL of CSS
	public String replaceInHtmlSource(String htmlSrouce, String origin,
			String replacement) {
		System.out.println("replace:" + origin + "|" + replacement);
		String source = htmlSrouce.replaceAll(origin, replacement);
		return source;
	}

	// remove parameters after filename
	public String removeParameter(String filename, String filepostfix) {
		if (!filename.contains(filepostfix)) {
			return null;
		}

		if (filename.endsWith(filepostfix))
			return filename;

		int index = filename.indexOf(filepostfix);
		return filename.substring(0, index + filepostfix.length());
	}

}
