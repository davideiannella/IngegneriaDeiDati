package com.esempio;

import com.esempio.models.RelevanceCriteria;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;


import com.esempio.mylucene.InputManager;

import java.nio.file.Paths;
import java.util.*;

import com.esempio.models.*;
import com.esempio.mylucene.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
    public static void main(String[] args) {
        QueryParser parser = new QueryParser("titolo", new WhitespaceAnalyzer());
        // Builds query
        List<Document> docsList = new ArrayList<Document>();
        try {
            InputManager ih = new InputManager();
            String userInput = ih.readUserInput("What are you looking for today?\n");
            if(userInput.isEmpty()) throw new davideiannella.example.mylucene.EmptyUserInputException();
            // Compie la ricerca e salva i documenti Lucene
            TopDocs queryDocumentsResult = new com.esempio.mylucene.LuceneManager().runHw_3_davide(parser,docsList, userInput);
            // Istanzio la lista di tabelle che estrarrò dai documenti Lucene
            List<MyAbstractTable> tableExtractedFromQueryDocumentResult = new ArrayList<>();
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get("target/idx0"))));
            // Conversione da TopDocs (Lucene) a List<MyAbstractTable>
            // foreach Doc in queryDocumentsResult, extract table and put in tableExtractedFromQueryDocumentResult
            for (ScoreDoc scoreDoc : queryDocumentsResult.scoreDocs) {
                tableExtractedFromQueryDocumentResult.add(
                        new MyTable(
                                searcher.doc(
                                        scoreDoc.doc)));
            }

            // queryResults    Map che associa ogni query alla lista delle tabelle recuperate (risultati della query).
            Map<String, List<MyAbstractTable>> queryResults = new HashMap<>();

            queryResults.put(userInput, tableExtractedFromQueryDocumentResult);

            endHomeworkUsingMRR(tableExtractedFromQueryDocumentResult,userInput, queryResults);

            endHomeworkUsingNDCG(tableExtractedFromQueryDocumentResult,userInput, queryResults);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void endHomeworkUsingMRR(List<MyAbstractTable> tableExtractedFromQueryDocumentResult, String userInput, Map<String, List<MyAbstractTable>> queryResults){
        // Metodo per riempire le gold tables
        Map<String, Set<MyAbstractTable>> relevantTablesMap = new HashMap<>();
        Set<MyAbstractTable> relevantTables = new HashSet<>();

        for (MyAbstractTable table : tableExtractedFromQueryDocumentResult) {
            if (RelevanceCriteria.isRelevant(userInput, table)) {
                relevantTables.add(table);
            }
        }
        // Inserisce le tabelle trovate
        relevantTablesMap.put(userInput, relevantTables);

        double computedMRR = MetricCalculator.computeMRR(queryResults, relevantTablesMap);
        System.out.println("Current MMR is: " + computedMRR);
    }

    private static void endHomeworkUsingNDCG(List<MyAbstractTable> tableExtractedFromQueryDocumentResult, String userInput, Map<String, List<MyAbstractTable>> queryResults){
        // Metodo per riempire le gold tables
        Map<String, Map<MyAbstractTable, Integer>> relevanceScores = new HashMap<>();

        for (MyAbstractTable table : tableExtractedFromQueryDocumentResult) {
            Map<MyAbstractTable, Integer> relevantTableAndValue = new HashMap<>();
            // Valuta se la tabella corrente è una gold
            if (RelevanceCriteria.isRelevant(userInput, table)) {
                // Siccome è gold, allora generiamo un valore da associare alla tabella per il calcolo dell'NDCG
                relevantTableAndValue.put(table, RelevanceCriteria.contaCaratteriComuni(table.getTableName(), userInput));
                //Associamo alla mappa con tabella e valore appena calcolata, la query dell'utente
                relevanceScores.put(userInput, relevantTableAndValue);
            }
        }


        double computedNDCG = MetricCalculator.computeAverageNDCG(queryResults, relevanceScores, tableExtractedFromQueryDocumentResult.size());
        System.out.println("Current NDCG is: " + computedNDCG);
    }

}
