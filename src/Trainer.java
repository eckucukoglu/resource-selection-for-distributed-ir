import indexer.SearchFiles;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

class Trainer {

    /**
     * Main function for trainer.
     *
     * @param args
     */
    public static void main(String[] args) {

        // Sampled documents index path
        Path path = Paths.get(System.getProperty("user.dir"), "data", "index", "govDatSamples");
        String sampledDocumentsIndexPath = path.toString();
        // Concatenated sampled documents index path
        path = Paths.get(System.getProperty("user.dir"), "data", "index", "govDatSamplesConcatenated");
        String concatenatedSampledDocumentsIndexPath = path.toString();


        JSONParser parser = new JSONParser();
        path = Paths.get(System.getProperty("user.dir"), "data", "processed-gov2", "sampled_collections.json");
        String sampledCollectionJsonPath = path.toString();
        JSONArray sampledCollectionsJson = new JSONArray();

        try {
            Object sampledCollectionsObject = parser.parse(new FileReader(sampledCollectionJsonPath));
            sampledCollectionsJson = (JSONArray) sampledCollectionsObject;

            for (int i = 0; i < sampledCollectionsJson.size(); i++) {
                path = Paths.get(System.getProperty("user.dir"), "data", "processed-aol", "training-queries",sampledCollectionsJson.get(i).toString());
                String trainingQueriesPath = path.toString();

                int K = 100000000;
                SearchFiles searchFile = new SearchFiles();
                searchFile.search(sampledDocumentsIndexPath, trainingQueriesPath, K);
                ArrayList<TopDocs> topDocs = searchFile.getTopDocsList();

                String[] topDocsName = searchFile.getNames(topDocs.get(0));
//                for (int j = 0; j < topDocs.size(); j++) {
//                    TopDocs topDocsForQuery = topDocs.get(i);
//                    ScoreDoc[] scoreDocs = topDocsForQuery.scoreDocs;
//                    ScoreDoc scoreDoc = scoreDocs[0];
//                    String[]
//                    break;
//                }
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
