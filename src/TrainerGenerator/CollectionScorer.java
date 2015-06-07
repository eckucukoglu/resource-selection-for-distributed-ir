package TrainerGenerator;
import Utils.Enums;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    }

    public void writeScoresToCollectionTrainingFiles(int hitCollection) {
        for (int i = 0; i < sampledCollectionIds.size(); i++) {
            if (i == hitCollection) {
                writeScoresToCollectionTrainingFile(sampledCollectionIds.get(i), true, reddeTopScoresWRTTopTen.get(i),
                        reddeTopScoresWRTTopHundred.get(i), GAVGScores.get(i), CORIScores.get(i));
            } else {
                writeScoresToCollectionTrainingFile(sampledCollectionIds.get(i), false, reddeTopScoresWRTTopTen.get(i),
                        reddeTopScoresWRTTopHundred.get(i), GAVGScores.get(i), CORIScores.get(i));
            }
        }
    }

    private void writeScoresToCollectionTrainingFile(String collectionID,boolean isHitCollection, Float reddeTopTenScore, Float reddeTopHundredScore, Float GAVGScore, Float CORIScore) {
        Path path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, Enums.LIBLINEAR_DIR,Enums.TRAINING_FILES_DIR, collectionID);
        String collectionTrainingFilePath = path.toString();

        BufferedWriter output = null;
        try {
            File collectionTrainingFile = new File(collectionTrainingFilePath);
            if (!collectionTrainingFile.exists()) {
                collectionTrainingFile.createNewFile();
            }
            output = new BufferedWriter(new FileWriter(collectionTrainingFile,true));
            if (isHitCollection) {
                output.write("+1 " + "1:" + reddeTopTenScore.toString() + " " + "2:" + reddeTopHundredScore.toString()
                        + " " + "3:" + GAVGScore.toString() + " " + "4:" + CORIScore.toString() + " \n");
            } else {
                output.write("-1 " + "1:" + reddeTopTenScore.toString() + " " + "2:" + reddeTopHundredScore.toString()
                        + " " + "3:" + GAVGScore.toString() + " " + "4:" + CORIScore.toString() + " \n");
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeScoresToCollectionTestingFiles(Integer collectionIndex, Integer collectionOffset) {
        int rowNumber = collectionIndex * 10 + collectionOffset;
        for (int i = 0; i < sampledCollectionIds.size(); i++) {
            writeScoresToCollectionTestingFile(sampledCollectionIds.get(i),rowNumber,reddeTopScoresWRTTopTen.get(i),
                    reddeTopScoresWRTTopHundred.get(i), GAVGScores.get(i), CORIScores.get(i));
        }
    }

    private void writeScoresToCollectionTestingFile(String collectionID,Integer rowNumber,Float reddeTopTenScore, Float reddeTopHundredScore, Float GAVGScore, Float CORIScore) {
        Path path = Paths.get(System.getProperty("user.dir"), Enums.DATA_DIR, Enums.LIBLINEAR_DIR,Enums.TESTING_FILES_DIR, collectionID);
        String collectionTrainingFilePath = path.toString();

        BufferedWriter output = null;
        try {
            File collectionTrainingFile = new File(collectionTrainingFilePath);
            if (!collectionTrainingFile.exists()) {
                collectionTrainingFile.createNewFile();
            }
            output = new BufferedWriter(new FileWriter(collectionTrainingFile,true));
            output.write(rowNumber.toString() + " " + "1:" + reddeTopTenScore.toString() + " " + "2:" + reddeTopHundredScore.toString()
                    + " " + "3:" + GAVGScore.toString() + " " + "4:" + CORIScore.toString() + " \n");
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
