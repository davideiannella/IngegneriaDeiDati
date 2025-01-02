package com.esempio.utils;

import com.esempio.models.MyTable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonExtractor {

    /**
     * Metodo che legge un file JSON e restituisce una lista di MyTable contenente,
     * per ogni entry, una tabella con i campi "caption" e "table".
     *
     * @param jsonFile il file JSON da leggere
     * @return List di MyTable contenente le entry con i campi "caption" e "table", una tabella per ogni entry
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public static List<MyTable> estraiCaptionsETables(File jsonFile) throws IOException {
        // Inizializza l'ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // Legge il file JSON e ottiene il nodo radice
        JsonNode rootNode = mapper.readTree(jsonFile);

        // Risultato
        List<MyTable> tablesToReturn = new ArrayList<>();

        // Itera su tutte le entry al livello superiore
        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String chiave = entry.getKey();
            JsonNode valore = entry.getValue();

            // Estrae "caption" e "table" dal valore
            String caption = valore.path("caption").asText();
            String table = valore.path("table").asText();

            // Crea tabella usando "caption" come nome e "table" come contenuto
            MyTable tmpTable = new MyTable(caption, table);
            // Aggiunge la tabella appena creata alla lista di tabelle da ritornare
            tablesToReturn.add(tmpTable);
        }
        // Ritorna tutte le tabelle nel documento JSON
        return tablesToReturn;
    }
}
