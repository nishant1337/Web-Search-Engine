package final_project;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.Hashtable;
import java.util.regex.Matcher;

public class PatternSearching {
	public Hashtable<String, Integer> searchPatterns(String patternToSearch) throws IOException {
		patternToSearch = patternToSearch.toLowerCase();
		Hashtable<String, Integer> pageRank = new Hashtable<String, Integer>();
		File directoryPath = new File("TextFiles");
		String fileNameList[] = directoryPath.list();
		String textToJoin = "";
		for (int i = 0; i < fileNameList.length; i++) {
			String fileName = fileNameList[i];
			textToJoin = readFileContents(fileName);
			textToJoin = textToJoin.toLowerCase();
			Pattern patternObject = Pattern.compile(patternToSearch);
			Matcher matcherObject = patternObject.matcher(textToJoin);
			while (matcherObject.find()) {
				pageRank.merge(fileName, 1, Integer::sum);
			}
		}
		return pageRank;
	}

	public String readFileContents(String fileName) throws IOException {
		byte[] fileContentsInBytes = Files.readAllBytes(Paths.get("./TextFiles/" + fileName));
		String fileContent = new String(fileContentsInBytes, StandardCharsets.US_ASCII);
		return fileContent;
	}
}