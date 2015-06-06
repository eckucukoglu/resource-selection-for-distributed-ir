import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.util.ArrayList;


public class CollectionScorer {

    private ArrayList<String> sampledCollections;
    private TopDocs topDocs;
    private ArrayList<Integer> topDocsCollections;
    private ArrayList<Float> reddeTopScoresWRTTopTen;
    private ArrayList<Float> reddeTopScoresWRTTopHundred;
    private ArrayList<Float> GAVGScores;

    public CollectionScorer(ArrayList<String> sampledCollections,TopDocs topDocs, String[] topDocsNames) {
        this.sampledCollections = sampledCollections;
        this.topDocs = topDocs;
        this.topDocsCollections = new ArrayList<Integer>();
        this.reddeTopScoresWRTTopTen = new ArrayList<Float>();
        this.reddeTopScoresWRTTopHundred = new ArrayList<Float>();
        this.GAVGScores = new ArrayList<Float>();

        for (int i = 0; i < topDocsNames.length; i++) {
            String docName = topDocsNames[i];
            Integer collectionId = Integer.parseInt(docName.substring(docName.lastIndexOf("-") + 1));
            this.topDocsCollections.add(collectionId);
        }
    }

    public void scoreCollections() {
        calculateReddeTopScores();
        calculateGAVGScores();

    }

//    private void calculateReddeScores() {
//        for (int i = 0; i < sampledCollections.size(); i++) {
//            reddeScores.add(0);
//            for (int j = 0; j < topDocsCollections.size(); j++){
//                if (topDocsCollections.get(j) == i) {
//                    reddeScores.set(i,reddeScores.get(i) + 1);
//                }
//            }
//        }
//    }

    private void calculateReddeTopScores() {
        for (int i = 0; i < sampledCollections.size(); i++) {
            reddeTopScoresWRTTopTen.add(new Float(0));
            reddeTopScoresWRTTopHundred.add(new Float(0));

            for (int j = 0; j < topDocsCollections.size(); j++){
                if (j > 100) {
                    break;
                }
                if (topDocsCollections.get(j) == i) {
                    if ( j < 10 ) {
                        reddeTopScoresWRTTopTen.set(i,reddeTopScoresWRTTopTen.get(i) + (topDocs.scoreDocs[j].score * new Float(0.05)));
                    }
                    reddeTopScoresWRTTopHundred.set(i,reddeTopScoresWRTTopHundred.get(i) + (topDocs.scoreDocs[j].score * new Float(0.05)));
                }
            }
        }
    }

    private void calculateGAVGScores() {
        for (int i = 0; i < sampledCollections.size(); i++) {
            GAVGScores.add(new Float(0));
            int noOfRetrievedDocForCollection = 0;
            for (int j = 0; j < topDocsCollections.size(); j++){
                if (topDocsCollections.get(j) == i) {
                    noOfRetrievedDocForCollection = noOfRetrievedDocForCollection + 1;
                    if (noOfRetrievedDocForCollection == 1) {
                        GAVGScores.set(i,topDocs.scoreDocs[j].score);
                    } else {
                        GAVGScores.set(i,GAVGScores.get(i) * topDocs.scoreDocs[j].score);
                        System.out.println("Fatih");
                    }
                }
            }
            if (noOfRetrievedDocForCollection != 0) {
                GAVGScores.set(i, new Float(Math.pow(GAVGScores.get(i), new Float(1) / new Float(noOfRetrievedDocForCollection))));

            }
        }
    }

}
