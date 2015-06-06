package indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	
	SearchFiles (String index, String queries, int K) throws Exception {
		
		String field = "contents";
			
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
			
			doSearch(in, searcher, query, K);
			
		}
		reader.close();
	}
	
	public static void doSearch (BufferedReader in, IndexSearcher searcher, Query query,
			int K) throws IOException {
		
		TopDocs results = searcher.search(query, K);
		ScoreDoc[] hits = results.scoreDocs;
		
		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");
		
		int end = Math.min(numTotalHits, K);
		
		for (int i = 0; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String name = doc.get("name");
			
			System.out.println((i+1) + ". " + name + " score="+hits[i].score);
		}
	}
}
