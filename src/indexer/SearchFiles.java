package indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class SearchFiles {
	
	private String indexDir;
	private ArrayList<TopDocs> topDocsList = new ArrayList<TopDocs>();
	
	public void search (String index, String queries, int K) throws Exception {
		
		String field = "contents";
		this.indexDir = index;
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		BufferedReader in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
		QueryParser parser = new QueryParser(field, analyzer);
		
		while (true) {
			
			String line = in.readLine();
			
			if (line == null || line.length() == -1) {
				break;
			}
			
			line = line.trim();
			if (line.length() == 0) {
				break;
			}
			
			Query query = parser.parse(line);
			
			System.out.println("Searching for: " + query.toString(field));
			
			this.topDocsList.add(searcher.search(query, K));
		}
		reader.close();
	}
	
	/**
	 * @return the topDocsList
	 */
	public ArrayList<TopDocs> getTopDocsList() {
		return topDocsList;
	}
	
	public String[] getNames (TopDocs results) throws IOException {
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(this.indexDir)));
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc[] hits = results.scoreDocs;
		int numTotalHits = results.totalHits;
		int end = Math.min(numTotalHits, hits.length);
		String[] resultDocNames = new String[end];
		
		for (int i = 0; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String name = doc.get("name");
			
			resultDocNames[i] = name;
			
		}
		
		return resultDocNames;
	}
}
