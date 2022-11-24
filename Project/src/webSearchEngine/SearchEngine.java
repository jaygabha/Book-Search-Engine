package webSearchEngine;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SearchEngine {
	
	private static Scanner sc = new Scanner(System.in);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SearchEngine engine = new SearchEngine();
		String choose = "n";
		System.out.println("\n---------------------------------------------------\n");
		System.out.println("              Welcome to Book Search Engine             ");
		System.out.println("The Book Search Engine has a list of various library and book websites that it will crawl to with the depth of 4 to find your books.");
		System.out.println("\n---------------------------------------------------\n");
		do {
			System.out.println(" Please Enter y to continue or n to exit");
			System.out.println("---------------------------------------------------");
		
			String option = sc.next();

			switch (option) {
			case "y":
				String[] url = {			//List of libraries for the books to search from with depth 4
						"https://www.reddit.com/r/books/",
						"https://en.wikipedia.org/wiki/Book",
						"http://www.newyorker.com/books",
						"http://www.powells.com",
						"http://www.npr.org/books/",
						"http://books.disney.com",
						"http://onlinebooks.library.upenn.edu",
						"http://www.orbooks.com",
						"http://www.nytimes.com/section/books",
						"http://www.ebay.com/rpp/books",
						"https://en.wikipedia.org/wiki/List_of_science_fiction_novels",
						"https://en.wikipedia.org/wiki/List_of_Christian_apologetic_works",
						"https://en.wikipedia.org/wiki/Psychedelic_literature",
						"http://www.brazosbookstore.com/shop/books",
				        "https://www.amazon.com/s/ref=lp_283155_nr_n_10?fst=as%3Aoff&rh=n%3A283155%2Cn%3A%211000%2Cn%3A8975347011&bbn=1000&ie=UTF8&qid=1479310486&rnid=1000",
				        "https://www.amazon.com/s/ref=lp_283155_nr_n_2?fst=as%3Aoff&rh=n%3A283155%2Cn%3A%211000%2Cn%3A3&bbn=1000&ie=UTF8&qid=1479594436&rnid=1000",
				        "https://www.amazon.com/s/ref=lp_283155_nr_n_3?fst=as%3Aoff&rh=n%3A283155%2Cn%3A%211000%2Cn%3A3248857011&bbn=1000&ie=UTF8&qid=1479594436&rnid=1000",
				        "https://www.abebooks.com",
				        "http://www.nytimes.com/section/books?action=click&contentCollection=Arts%2FBooks&contentPlacement=2&module=SectionsNav&pgtype=sectionfront&region=TopBar&version=BrowseTree",

				};
				choose = engine.searchWord(url);
				break;

			case "n":
				System.out.println("Exit...");
				choose = "n";
				break;

			default:
				System.out.println("Please enter correct option");
				choose = "y";

			}
		} while (choose.equals("y"));

		System.out.println("\n---------------------------------------------------\n");
		System.out.println("	:) THANK YOU FOR USING BOOK SEARCH ENGINE :)        ");
		System.out.println("\n---------------------------------------------------\n");
	}

	private String searchWord(String[] url) {
		
				
		System.out.println("Crawling Started...");
		for(int i = 0;i< url.length;i++)
			Crawler.startCrawler(url[i], 0); //crawling the URLs one by one
		System.out.println("Crawling Compelted...");

		// Hash table is used instead of Hash Map as it don't allow null value in insertion
		Hashtable<String, Integer> listOfFiles = new Hashtable<String, Integer>();
		
		String choice = "y";
		do {
			System.out.println("---------------------------------------------------");
			System.out.println("\n Enter the book name or author name to search ");
			String wordToSearch = sc.nextLine();
			while(wordToSearch.isEmpty()) {
				wordToSearch = sc.nextLine();
			}
			System.out.println("---------------------------------------------------");
			int frequency = 0;
			int noOfFiles = 0;
			listOfFiles.clear();
			File files = null;
			try {
				System.out.println("\nSearching...");
				files = new File(Path.txtDirectoryPath);
				File[] fileArray = files.listFiles(); //Store all the files in a file array
				
				for (int i = 0; i < fileArray.length; i++) { //Search all the files in the file array

					In data = new In(fileArray[i].getAbsolutePath());

					String txt = data.readAll();
					data.close();
					Pattern p = Pattern.compile("::");
					String[] file_name = p.split(txt); //get the name of the file using regex to format
					//Search the word in the file
					frequency = SearchWord.wordSearch(txt, wordToSearch.toLowerCase(), file_name[0]); // search word in txt files

					if (frequency != 0) {
						listOfFiles.put(file_name[0], frequency);
						noOfFiles++;
					}
					
				}

				if(noOfFiles>0) {
				System.out.println("\nTotal Number of Sites containing books related to: " + wordToSearch + " is : " + noOfFiles);
				}else {
					System.out.println("\n No Books found for search: "+ wordToSearch);
				}

				SearchWord.rankFiles(listOfFiles, noOfFiles); //rank the files based on frequency of word count
				

			} 
			catch (Exception e) {
				System.out.println("Exception:" + e);
			}
			System.out.println("\n Do you want return to search another book(y/n)?");
			choice = sc.next();
		} while (choice.equals("y"));
		
		deleteFiles();
		
		return "n";
	}

	private void deleteFiles() {
		File files = new File(Path.txtDirectoryPath);
		File[] fileArray = files.listFiles();

		for (int i = 0; i < fileArray.length; i++) {
			fileArray[i].delete();
		}
		
		File fileshtml = new File(Path.htmlDirectoryPath);
		File[] fileArrayhtml = fileshtml.listFiles();

		for (int i = 0; i < fileArrayhtml.length; i++) {
			
			fileArrayhtml[i].delete();
		}
	}
	
}
