package final_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountFrequency {

	public void countWords(String word) throws FileNotFoundException, IOException {
		try {
			File directoryName = new File("TextFiles");
			File[] fileList = directoryName.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				int wordCount = 0;
				if (fileList[i].isFile()) {
					File file = new File("TextFiles/" + fileList[i].getName());
					InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
					BufferedReader bufferReader = new BufferedReader(inputStreamReader);
					StringBuffer stringBuffer = new StringBuffer();
					String tempString = null;
					while ((tempString = bufferReader.readLine()) != null) {
						stringBuffer.append(tempString.toLowerCase());
					}
					Pattern patternObject = Pattern.compile(word.toLowerCase());
					Matcher matcherObject = patternObject.matcher(stringBuffer);
					while (matcherObject.find()) {
						wordCount++;
					}
					bufferReader.close();
					if(wordCount>0) {
					System.out.println("Word : " + word + "\nWord Frequency Count : " + wordCount
							+ " \nFile Name : " + fileList[i].getName() + "\n");
				}}
			}
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}
