package com.esempio.models;

public class MyTable implements MyAbstractTable{
    private String name;

    @Override
    public String getTableName() {
        return name;
    }
}
