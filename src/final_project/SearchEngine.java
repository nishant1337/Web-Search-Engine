package final_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SearchEngine {

	public static int pageRankValue = 1;

	public static void main(String[] args) throws Exception {
		System.out.println(""
				+ "███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗    ███████╗███╗   ██╗ ██████╗ ██╗███╗   ██╗███████╗\r\n"
				+ "██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║    ██╔════╝████╗  ██║██╔════╝ ██║████╗  ██║██╔════╝\r\n"
				+ "███████╗█████╗  ███████║██████╔╝██║     ███████║    █████╗  ██╔██╗ ██║██║  ███╗██║██╔██╗ ██║█████╗  \r\n"
				+ "╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║    ██╔══╝  ██║╚██╗██║██║   ██║██║██║╚██╗██║██╔══╝  \r\n"
				+ "███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║    ███████╗██║ ╚████║╚██████╔╝██║██║ ╚████║███████╗\r\n"
				+ "╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝    ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝╚═╝  ╚═══╝╚══════╝\r\n");
		System.out.println("Welcome to our Search Engine");
		System.out.println("Search engine by\n");
		System.out.println("Avadh Patel (110074366)");
		System.out.println("Jay Paresh Patel (110074516)");
		System.out.println("Nishant Chandubhai Pansuriya (110074312)");
		System.out.println("Sanket Pravinkumar Patel (110074218)");
		System.out.print("What would you like to do?\n\n");
		System.out.print("1) Crawl websites (Press 1) \n\n");
		System.out.print("2) Convert HTML to Text (Press 2) \n\n");
		System.out.print("3) Create Dictionary (Press 3) \n\n");
		System.out.print("4) Count Frequency (Press 4) \n\n");
		System.out.print("5) Check Spelling (Press 5) \n\n");
		System.out.print("6) Search Something (Press 6) \n\n");
		System.out.print("Press F to exit. \n\n");
		String userInput = null;
		System.out.println("So what do you want to do? ");

		Crawler crawler = new Crawler();

		try (Scanner scanner = new Scanner(System.in)) {
			userInput = scanner.nextLine();
			while (!userInput.toLowerCase().equals("f")) {
				switch (userInput) {
				case "1":
					System.out.println("Please enter base url from which the crawling will start: ");
					Scanner baseWebPageUrl = new Scanner(System.in);
					crawler.crawl(baseWebPageUrl.nextLine(), 1);
					break;
				case "2":
					String htmlFilesList[] = crawler.getFileNames("WebPages");
					for (int i = 0; i < htmlFilesList.length; i++) {
						if (!htmlFilesList[i].contains(".html")) {
							String currentDirectoryPath = System.getProperty("user.dir");
							Path filePath = Paths.get(currentDirectoryPath + "\\WebPages\\" + htmlFilesList[i]);
							Files.delete(filePath);
						}
					}
					for (int i = 0; i < htmlFilesList.length; i++) {
						System.out.println("Converting file: " + htmlFilesList[i]);
						String textFileContent = crawler.convertHtmlToText(htmlFilesList[i]);
						crawler.saveTextFile(htmlFilesList[i], textFileContent);
						System.out.println("Successfully converted html file to txt file\n");
					}
					break;
				case "3":
					Dictionary dictionary = new Dictionary();
					dictionary.createDictionary();
					System.out.println("Dictionary created.");
					break;
				case "4":
					System.out.println("Enter a word to count its frequency : ");
					String wordToCount = scanner.nextLine();
					CountFrequency countFrequency = new CountFrequency();
					countFrequency.countWords(wordToCount);
					break;
				case "5":
					System.out.print("Enter something to search: ");
					String userSearchValue = scanner.nextLine();
					Suggestion suggestSpelling = new Suggestion();
					suggestSpelling.checkSpelling(userSearchValue);
					break;
				case "6":
					System.out.print("Enter something to search: ");
					Long startTime;
					Long endTime;
					float totalTime;
					String userQuery = scanner.nextLine();

					File dictionaryfile = new File("dictionary.txt");
					boolean isWordFound = false;
					try (BufferedReader bufferReader = new BufferedReader(new FileReader(dictionaryfile))) {
						ArrayList<String> readDictionary = new ArrayList<String>();
						String fileLine;

						while ((fileLine = bufferReader.readLine()) != null)
							readDictionary.add(fileLine);
						bufferReader.close();

						for (String eachWord : readDictionary) {
							if (userQuery.toLowerCase().equals(eachWord.toLowerCase())) {
								isWordFound = true;
							}
						}
					}

					if (!isWordFound) {
						System.out.println("User entered word not found in dictionary");
						Suggestion suggestion = new Suggestion();
						suggestion.provideSuggestion(userQuery);
						break;
					}

					PatternSearching patternSearching = new PatternSearching();
					startTime = System.currentTimeMillis();
					Hashtable<String, Integer> pageRank = patternSearching.searchPatterns(userQuery);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;

					if (pageRank.size() == 0) {
						Suggestion suggestion = new Suggestion();
						suggestion.provideSuggestion(userQuery);
					} 
					else {
						System.out.println("Just a sec....\n");
						int totalOccurences = 0;
						for (int occurences : pageRank.values())
							totalOccurences += occurences;

						System.out
								.println("Found " + totalOccurences + " matches in (" + totalTime / 1000 + " seconds)");
						System.out.println("Matches found in " + pageRank.size() + " web pages.\n");
						System.out.println("Rank	Frequency   Page");

						Map<String, Integer> sortPagesByRank = pageRank.entrySet().stream()
								.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
										LinkedHashMap::new));
						sortPagesByRank.forEach((key, value) -> {
							System.out.println(String.format("%02d", pageRankValue) + "	" + String.format("%02d", value)
									+ "          " + key);
							pageRankValue++;
						});
					}
				}
				System.out.print("\n1) Crawl websites (Press 1) \n\n");
				System.out.print("2) Convert HTML to Text (Press 2) \n\n");
				System.out.print("3) Create Dictionary (Press 3) \n\n");
				System.out.print("4) Count Frequency (Press 4) \n\n");
				System.out.print("5) Check Spelling (Press 5) \n\n");
				System.out.print("6) Search Something (Press 6) \n\n");
				System.out.print("Press F to exit. \n\n");
				System.out.print("Enter your choice: \n");
				userInput = scanner.nextLine();
			}

			System.out.println("Thank you for using our Search Engine");
		}
	}
}