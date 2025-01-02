package com.esempio.models;

import java.util.*;

public class MetricCalculator {

    /**
     * Calcola il NDCG medio su tutte le query.
     *
     * @param queryResults    Mappa delle query ai risultati recuperati (ordinati).
     * @param relevanceScores Mappa delle query ai punteggi di rilevanza delle tabelle.
     * @param p               Posizione fino alla quale calcolare l'NDCG (tipicamente 10).
     * @return                Il valore medio di NDCG.
     */
    public static double computeAverageNDCG(Map<String, List<MyAbstractTable>> queryResults,
                                            Map<String, Map<MyAbstractTable, Integer>> relevanceScores,
                                            int p) {
        double sumNDCG = 0.0;
        int totalQueries = queryResults.size();

        for (String query : queryResults.keySet()) {
            List<MyAbstractTable> retrievedTables = queryResults.get(query);
            Map<MyAbstractTable, Integer> relevanceMap = relevanceScores.get(query);
            if(relevanceMap == null || relevanceMap.isEmpty()) return 0;

            double dcg = computeDCG(retrievedTables, relevanceMap, p);
            double idcg = computeIDCG(relevanceMap, p);

            double ndcg = (idcg == 0.0) ? 0.0 : dcg / idcg;
            sumNDCG += ndcg;
        }

        return sumNDCG / totalQueries;
    }

    private static double computeDCG(List<MyAbstractTable> retrievedTables,
                                     Map<MyAbstractTable, Integer> relevanceMap,
                                     int p) {
        double dcg = 0.0;
        double rel1= 0.0;
        for (int i = 0; i < Math.min(retrievedTables.size(), p); i++) {
            MyAbstractTable table = retrievedTables.get(i);
            int rel = relevanceMap.getOrDefault(table, 0);
            if(i==1){
                rel1= relevanceMap.getOrDefault(table, 0);
            }
            dcg += rel / Math.log(2); // i + 2 perché i parte da 0
        }
        return rel1 + dcg;
    }

    private static double computeIDCG(Map<MyAbstractTable, Integer> relevanceMap, int p) {
        // Ottiene le rilevanze ordinate in modo decrescente
        List<Integer> sortedRelevances = new ArrayList<>(relevanceMap.values());
        Collections.sort(sortedRelevances, Collections.reverseOrder());

        double idcg = 0.0;
        for (int i = 0; i < Math.min(sortedRelevances.size(), p); i++) {
            int rel = sortedRelevances.get(i);
            idcg += rel / Math.log(2);
        }
        return idcg;
    }

    public static double computeMRR(Map<String, List<MyAbstractTable>> queryResults,
                                    Map<String, Set<MyAbstractTable>> relevantTables) {
        if(relevantTables.isEmpty()){
            System.out.println("No relevant tables found");
            return -1;
        }
        double sumReciprocalRanks = 0.0;
        int totalQueries = queryResults.size();

        for (String query : queryResults.keySet()) {
            List<MyAbstractTable> retrievedTables = queryResults.get(query);
            Set<MyAbstractTable> relevant = relevantTables.get(query);

            if(relevant==null ||  relevant.isEmpty()){
                System.out.println("No relevant tables with query name found");
                return -1;
            }

            double reciprocalRank = 0.0;

            // Trova la posizione del primo risultato rilevante
            for (int i = 0; i < retrievedTables.size(); i++) {
                MyAbstractTable table = retrievedTables.get(i);
                if (relevant.contains(table)) {
                    reciprocalRank = 1.0 / (i + 1); // Posizione 1-based
                    break;
                }
            }

            sumReciprocalRanks += reciprocalRank;
        }

        return sumReciprocalRanks / totalQueries;
    }
}

