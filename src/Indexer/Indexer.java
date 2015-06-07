package Indexer;

import java.nio.file.Path;
import java.nio.file.Paths;

import Utils.Enums;

/**
 * Calls Lucene indexer to index collection.
 * Index files are stored in index path to search later.
 */
public class Indexer {
	
	public static void main(String[] args) {
		
		Path path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, 
				Enums.PROC_GOV2_DATA_DIR, Enums.SAMPLE_DOCS_DIR, "test");
		String docsPath = path.toString();

		path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, Enums.INDEX_DIR);
		String indexPath = path.toString();
		
		IndexFiles indexFile = new IndexFiles();
		indexFile.index(docsPath, indexPath);
	}
}