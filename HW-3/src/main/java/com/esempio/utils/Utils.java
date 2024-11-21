package com.esempio.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utils {
    public void readJsonFileThenBuildInvertedIndexAndSet2Count(String pathToJson,
                                                               Map<String, Collection<String>> invertedIndex,
                                                               HashMap<String, Integer> set2count,
                                                               List<String> tokenizedUserInput){
        ObjectMapper mapper = new ObjectMapper();
        HashSet<String> propertyNames = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(pathToJson))){
            String line;
            while ( (line = reader.readLine())!=null){
                JsonNode jsonNode = mapper.readTree(line);
                String oid = jsonNode.path("_id").path("$oid").asText();
                JsonNode cells = jsonNode.path("cells");
                // add tokens to inverted index as key, and add ids as value
                if (cells.isArray()) {
                    for (JsonNode cell : cells) {
                        String cleanedText = cell.path("cleanedText").asText();
                        JsonNode rows = cell.path("Rows");
                        if (rows.isArray()) {
                            for (JsonNode row : rows) {
                                Iterator<String> fieldNames = row.fieldNames();
                                while (fieldNames.hasNext()) {
                                    propertyNames.add(fieldNames.next());
                                }
                            }
                        }
                        if(invertedIndex.get(cleanedText)==null ||
                                invertedIndex.get(cleanedText).isEmpty()){
                            invertedIndex.put(cleanedText,new ArrayList<String>() {{
                                add(oid);}});
                            set2count.put(oid,0);
                        } else{
                            if(!invertedIndex.get(cleanedText).contains(oid)){
                                invertedIndex.get(cleanedText).add(oid);
                            }
                        }
                    }
                }
                for (String token : tokenizedUserInput) {
                    if (invertedIndex.containsKey(token)) {
                        // read entire posting list and add 1 to counts
                        for (String _oid : invertedIndex.get(token)) {
                            set2count.put(_oid, set2count.getOrDefault(_oid, 0) + 1);
                        }
                    }
                }
            }
            System.out.println("Property names are: "+ propertyNames.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printStats(Map<String, Collection<String>> invertedIndex){
        System.out.println("InvertedIndex contains: "+ invertedIndex.size()+ " entries");
        double avgCollectionSize = 0;
        double avgSingleValueSize = 0;
        long allMapValuesCounter = 0;
        for (Collection<String> collection : invertedIndex.values()) {
            avgCollectionSize+=collection.size();
            allMapValuesCounter+=collection.size();
            for (String s : collection){
                avgSingleValueSize+=s.length();
            }
        }
        avgCollectionSize=avgCollectionSize/ invertedIndex.size();
        avgSingleValueSize=avgSingleValueSize/allMapValuesCounter;
        System.out.println("avgCollectionSize is: "+avgCollectionSize);
        System.out.println("avgSingleValueSize is: "+avgSingleValueSize);
        System.out.println("Total values: "+allMapValuesCounter);
    }
    public class Timer{

        private long startTime = 0;
        private long stopTime = 0;
        public void start(){
            this.startTime = System.currentTimeMillis();
        }
        public void stop(String messageToDisplay){
            this.stopTime = System.currentTimeMillis();
            System.out.println(messageToDisplay + (this.stopTime - this.startTime)/1000 + "s");
        }

        public void reset(){
            this.startTime = 0;
            this.stopTime = 0;
        }
    }
}
