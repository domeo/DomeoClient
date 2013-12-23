package org.mindinformatics.gwt.domeo.plugins.annotation.spls.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileOperation {

	public String readFileFromPath(String path) {
		String str = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line.replace(" ", ""));
				sb.append('\n');
				line = br.readLine();
			}
			str = sb.toString();

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

}
