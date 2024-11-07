package davideiannella.example;

import davideiannella.example.mylucene.InputManager;
import davideiannella.example.mylucene.LuceneManager;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main( String[] args ) throws Exception {

        QueryParser parsertitolo = new QueryParser("titolo", new WhitespaceAnalyzer());

        QueryParser parsercontenuto = new QueryParser("contenuto", new WhitespaceAnalyzer());
        // Builds query
        List<Document> docsList = new ArrayList<Document>();

        new LuceneManager().runHw_2(parsercontenuto,docsList);
}
}
