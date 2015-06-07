package TrainerGenerator;
import org.apache.lucene.search.TopDocs;

import java.util.ArrayList;

public class CollectionScorer {

    private ArrayList<String> sampledCollectionIds;
    private TopDocs topDocsSampledDocs;
    private TopDocs topDocsConcatenatedSampledDocs;

    private ArrayList<Integer> topDocsSampledCollectionIds;
    private ArrayList<Integer> topDocsConcatenatedSampledCollectionIds;
    
    private ArrayList<Float> reddeTopScoresWRTTopTen;
    private ArrayList<Float> reddeTopScoresWRTTopHundred;
    private ArrayList<Float> GAVGScores;
    private ArrayList<Float> CORIScores;

    public CollectionScorer(ArrayList<String> sampledCollectionIds, TopDocs topDocsSampledDocs, TopDocs topDocsConcatenatedSampledDocs, String[] sampledDocsNames, String[] concatenatedSampledDocsNames) {
        this.sampledCollectionIds = sampledCollectionIds;
        this.topDocsSampledDocs = topDocsSampledDocs;
        this.topDocsConcatenatedSampledDocs = topDocsConcatenatedSampledDocs;

        this.topDocsSampledCollectionIds = new ArrayList<Integer>();
        for (int i = 0; i < sampledDocsNames.length; i++) {
            String docName = sampledDocsNames[i];
            Integer collectionId = Integer.parseInt(docName.substring(docName.lastIndexOf("-") + 1));
            this.topDocsSampledCollectionIds.add(collectionId);
        }
        this.topDocsConcatenatedSampledCollectionIds = new ArrayList<Integer>();
        for (int i = 0; i < concatenatedSampledDocsNames.length; i++) {
            String collectionName = concatenatedSampledDocsNames[i];
            Integer collectionId = Integer.parseInt(collectionName);
            this.topDocsConcatenatedSampledCollectionIds.add(collectionId);
        }

        
        this.reddeTopScoresWRTTopTen = new ArrayList<Float>();
        this.reddeTopScoresWRTTopHundred = new ArrayList<Float>();
        this.GAVGScores = new ArrayList<Float>();
        this.CORIScores = new ArrayList<Float>();
    }

    public void scoreCollections() {
        calculateReddeTopScores();
        calculateGAVGScores();
        calculateCORIScores();
        System.out.println("Ebelek");
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
        for (int i = 0; i < sampledCollectionIds.size(); i++) {

            reddeTopScoresWRTTopTen.add(new Float(0));
            reddeTopScoresWRTTopHundred.add(new Float(0));

            for (int j = 0; j < topDocsSampledCollectionIds.size(); j++) {
                if (j > 100)
                    break;
                
                if (topDocsSampledCollectionIds.get(j) == i) {
                    if ( j < 10 ) {
                        reddeTopScoresWRTTopTen.set(i, 
                        		reddeTopScoresWRTTopTen.get(i) + (topDocsSampledDocs.scoreDocs[j].score * new Float(0.05)));
                    }
                    reddeTopScoresWRTTopHundred.set(i, 
                			reddeTopScoresWRTTopHundred.get(i) + (topDocsSampledDocs.scoreDocs[j].score * new Float(0.05)));
                }
            }
        }
    }

    private void calculateGAVGScores() {
        int m = 10;
        for (int i = 0; i < sampledCollectionIds.size(); i++) {

            int noOfRetrievedDocForCollection = 0;
            GAVGScores.add(new Float(0));
            float lastScoreMultiplied = new Float(0);

            for (int j = 0; j < topDocsSampledCollectionIds.size(); j++){
                if (noOfRetrievedDocForCollection == m) {
                    break;
                }

                if (topDocsSampledCollectionIds.get(j) == i) {
                    noOfRetrievedDocForCollection++;
                    
                    if (noOfRetrievedDocForCollection == 1) {
                        GAVGScores.set(i, topDocsSampledDocs.scoreDocs[j].score);
                    } else {
                        GAVGScores.set(i, GAVGScores.get(i) * topDocsSampledDocs.scoreDocs[j].score);
                    }
                    lastScoreMultiplied = topDocsSampledDocs.scoreDocs[j].score;
                }
            }

            if (noOfRetrievedDocForCollection == 0) {
                continue;
            } else if (noOfRetrievedDocForCollection < m) {
                for (; noOfRetrievedDocForCollection < m; noOfRetrievedDocForCollection++) {
                    GAVGScores.set(i, GAVGScores.get(i) * lastScoreMultiplied);
                }
            }
            GAVGScores.set(i, new Float(Math.pow(GAVGScores.get(i), new Float(0.1))));
        }
    }

    private void calculateCORIScores() {
        for (int i = 0; i < sampledCollectionIds.size(); i++) {
            CORIScores.add(new Float(0));
            for (int j = 0; j < topDocsConcatenatedSampledCollectionIds.size(); j++) {
                if (topDocsConcatenatedSampledCollectionIds.get(j) == i) {
                    CORIScores.set(i, topDocsConcatenatedSampledDocs.scoreDocs[j].score);
                }
            }
        }
    }

}
