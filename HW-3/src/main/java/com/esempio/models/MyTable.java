package com.esempio.models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

public class MyTable implements MyAbstractTable {
    // Nome della tabella, alias "caption"
    private String name;
    // La tabella in essere
    private String table;
    // Il documento Lucene
    private Document luceneDoc;

    // Se ha caption e tabella, popola l'oggetto
    public MyTable (String _name, String _table){
        this.name=_name;
        this.table=_table;
        this.luceneDoc = new Document();
        this.luceneDoc.add(new TextField("name", name, Field.Store.YES));
        this.luceneDoc.add(new TextField("table", table, Field.Store.YES));
    }

    // Popola oggetto partendo da documento Lucene
    public MyTable (Document _luceneDoc){
        this.luceneDoc = _luceneDoc;
        this.name = luceneDoc.get("titolo");
        this.table = luceneDoc.get("contenuto");
    }


    @Override
    public String getTableName() {
        return name;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public Document getDocument() {
        return this.luceneDoc;
    }

}

