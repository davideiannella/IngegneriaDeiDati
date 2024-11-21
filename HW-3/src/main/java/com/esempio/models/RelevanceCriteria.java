package com.esempio.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RelevanceCriteria {

    /**
     * Determina se una tabella è rilevante per una query basandosi su criteri predefiniti.
     *
     * @param query La query per cui si valuta la rilevanza.
     * @param table La tabella da valutare.
     * @return True se la tabella è rilevante, false altrimenti.
     */
    public static boolean isRelevant(String query, MyAbstractTable table) {
        // Semplice criterio: la tabella è rilevante se contiene tutte le parole chiave della query nel nome
        Set<String> queryKeywords = new HashSet<>(Arrays.asList(query.toLowerCase().split("\\s+")));
        Set<String> tableKeywords = new HashSet<>(Arrays.asList(table.getTableName().toLowerCase().split("_")));

        return tableKeywords.containsAll(queryKeywords);
    }

    public static int contaCaratteriComuni(String s1, String s2) {
        Set<Character> set1 = new HashSet<>();
        for (char c : s1.toCharArray()) {
            set1.add(c);
        }

        Set<Character> set2 = new HashSet<>();
        for (char c : s2.toCharArray()) {
            set2.add(c);
        }

        set1.retainAll(set2); // Mantiene solo i caratteri presenti in entrambi i set
        return set1.size();
    }
}
