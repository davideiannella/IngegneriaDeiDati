package com.esempio.models;

import org.apache.lucene.document.Document;

public interface MyAbstractTable {
    public String getTableName();
    public String getTable();
    public Document getDocument();
}
