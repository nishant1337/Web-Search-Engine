package final_project;

import java.io.File;
import java.io.FileWriter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;

public class Crawler {
	/**
	 * Crawl web pages starting from base URL and continue till the specified depth
	 * 
	 * @param webPageUrl
	 * @param crawlerDepth
	 * @throws Exception
	 */
	public void crawl(String webPageUrl, int crawlerDepth) throws Exception {
		ArrayList<String> urlsToVisit = new ArrayList<String>();
		Set<String> visitedUrls = new HashSet<String>();
		ArrayList<Integer> depth = new ArrayList<Integer>();

		System.out.println("Started Crawling URL: " + webPageUrl);

		// Add web page url to crawler queue
		urlsToVisit.add(webPageUrl);
		depth.add(0);

		int depthReached = 0;

		for (int i = 0; i < urlsToVisit.size(); i++) {

			// change max page crawl limit here to crawl more pages
			File totalHtmlFiles = new File("./WebPages/");
			if (totalHtmlFiles.list().length > 99) {
				break;
			}

			// Stop crawling current web page if crawler depth reached
			/*
			 * if (depth.get(i) > crawlerDepth) { break; }
			 */

			String url = urlsToVisit.get(i);

			if (!(getDomain(webPageUrl).split("\\.")[0].equals(getDomain(url).split("\\.")[0]))) {
				System.out.println("Skipping url: " + url);
				continue;
			}

			// Skip if url already visited
			if (visitedUrls.contains(url)) {
				continue;
			}

			// Add url to visited list
			visitedUrls.add(url);

			System.out.println("Now Crawling : " + url);

			try {
				Document jsoupDocument = Jsoup.connect(url).get();
				String webPageTitle = jsoupDocument.title();
				String webPageContent = jsoupDocument.html();
				if (!webPageTitle.isEmpty()) {
					this.saveWebPage(webPageTitle, webPageContent);
				}
				Elements webPageUrls = jsoupDocument.select("a[href]");
				depthReached++;
				for (Element webPage : webPageUrls) {
					urlsToVisit.add(webPage.attr("abs:href"));
					depth.add(depthReached);
				}
			} catch (IOException error) {
				System.out.println("Error in crawling :" + error);
			}
		}
	}

	/**
	 * Get domain from url
	 * 
	 * @param url the web page url
	 * @return domain extracted from url
	 * @throws URISyntaxException
	 */
	public String getDomain(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain != null) {
			return domain.startsWith("www.") ? domain.substring(4) : domain;
		}
		return domain;
	}

	/**
	 * Save Web Page
	 * 
	 * @param webPageTitle   the title of the web page
	 * @param webPageContent the body content of the web page
	 */
	public void saveWebPage(String webPageTitle, String webPageContent) {
		try {
			webPageTitle = webPageTitle.replaceAll("[^a-zA-Z0-9]", " ");
			webPageTitle = webPageTitle.trim();
			FileWriter fileWriter = new FileWriter("./WebPages/" + webPageTitle + ".html");
			fileWriter.write(webPageContent);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Problem saving web page: " + webPageTitle);
		}

	}

	/**
	 * Get names of all files in a given directory
	 * 
	 * @param directoryName name of the directory
	 * @return list of file names in a directory
	 */
	public String[] getFileNames(String directoryName) {
		File directoryLocation = new File(directoryName);
		String fileNames[] = directoryLocation.list();
		return fileNames;
	}

	/**
	 * Convert html file to text File
	 * 
	 * @param htmlFileName name of html file
	 * @return content of html file
	 * @throws IOException
	 */
	public String convertHtmlToText(String htmlFileName) throws IOException {
		String htmlFilePath = "./WebPages/" + htmlFileName;
		Charset characterEncoding = StandardCharsets.US_ASCII;
		byte[] htmlFileContentInBytes = Files.readAllBytes(Paths.get(htmlFilePath));
		String htmlFileContent = new String(htmlFileContentInBytes, characterEncoding);
		Document jsoupDocument = Jsoup.parse(htmlFileContent);
		return jsoupDocument.text();
	}

	/**
	 * Save text file from converted html file content
	 * 
	 * @param textFileName text file name to use for saving it
	 * @param textContent  text content from converted html file
	 * @throws IOException
	 */
	public void saveTextFile(String textFileName, String textFileContent) throws IOException {
		FileWriter fileWriter = new FileWriter(
				"./TextFiles/" + textFileName.substring(0, textFileName.lastIndexOf(".")) + ".txt");
		fileWriter.write(textFileContent);
		fileWriter.close();
	}
}
