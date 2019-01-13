package com.cloudcmr.app.member.web.rest;

public class Searchable {
    private String searchText;

    public Searchable(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    @Override
    public String toString() {
        return "Searchable{" +
            "searchText='" + searchText + '\'' +
            '}';
    }

    public boolean isActive() {
        return searchText != null;
    }
}
