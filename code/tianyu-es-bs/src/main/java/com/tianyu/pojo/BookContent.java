package com.tianyu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class BookContent {

    private String title;
    public ArrayList<String> authors;
    private float ranking;
    private float rankingCount;
    private String abstractForBook;
    private boolean partOfSeries;
    private ArrayList<String> genreList;

    public void add_authors(String author){
        authors.add(author);
    }

    public void add_genreList(String genre){
        genreList.add(genre);
    }

    public BookContent(){
        authors = new ArrayList<String>();
        genreList = new ArrayList<String>();
    }

}