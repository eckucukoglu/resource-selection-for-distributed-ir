package indexer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexer {
	
	/**
	* Main function of project.
	*
	* @param args
	*/
	public static void main(String[] args) {
		
		// Document collection directory path
		Path path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "govDatSamples");
		String docsPath = path.toString();
		// Index files directory path
		path = Paths.get(System.getProperty("user.dir"), "data", "index");
		String indexPath = path.toString();
		// Query list file path
		path = Paths.get(System.getProperty("user.dir"), "data", "processed-aol", "training-queries", "test");
		String queryListPath = path.toString();
		// Top-K documents retrieved
		int K = 50;
		
		IndexFiles indexFile = new IndexFiles(docsPath, indexPath);
		
		try {
			SearchFiles searchFile = new SearchFiles();
			searchFile.search(indexPath, queryListPath, K);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}