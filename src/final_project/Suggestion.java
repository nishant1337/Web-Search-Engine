package final_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Suggestion {
	public void provideSuggestion(String wordToCalculateED) throws FileNotFoundException, IOException {
		EditDistance editDistance = new EditDistance();
		File dictionaryfile = new File("dictionary.txt");
		try (BufferedReader bufferReader = new BufferedReader(new FileReader(dictionaryfile))) {
			ArrayList<String> dictionary = new ArrayList<String>();
			String fileLine;

			while ((fileLine = bufferReader.readLine()) != null)
				dictionary.add(fileLine);
			bufferReader.close();

			int distance, editDistanceOne = 10, editDistanceTwo = 10;
			String wordOne = null, wordTwo = null;

			for (String eachWord : dictionary) {
				distance = editDistance.editDistance(eachWord, wordToCalculateED);

				if (distance < editDistanceTwo) {
					if (distance < editDistanceOne) {
						editDistanceOne = distance;
						wordOne = eachWord;
					} else {
						editDistanceTwo = distance;
						wordTwo = eachWord;
					}
				}
			}
			System.out.println("Did you mean: " + wordOne + " or " + wordTwo);
		}
	}

	public void checkSpelling(String wordToCalculateED) throws FileNotFoundException, IOException {

		EditDistance editDistance = new EditDistance();
		File dictionaryfile = new File("dictionary.txt");
		try (BufferedReader bufferReader = new BufferedReader(new FileReader(dictionaryfile))) {
			ArrayList<String> dictionary = new ArrayList<String>();
			String fileLine;

			while ((fileLine = bufferReader.readLine()) != null)
				dictionary.add(fileLine);
			bufferReader.close();

			int distance, editDistanceOne = 10, editDistanceTwo = 10;
			String wordOne = null;

			for (String eachWord : dictionary) {
				if (eachWord.equals(wordToCalculateED)) {
					System.out.println("Spelling is correct");
					return;
				}
				distance = editDistance.editDistance(eachWord, wordToCalculateED);
				if (distance < editDistanceTwo) {
					if (distance < editDistanceOne) {
						editDistanceOne = distance;
						wordOne = eachWord;
					}
				}
			}
			System.out.println("Please check the spelling did you mean: " + wordOne);
		}
	}
}
