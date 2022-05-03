package com.tianyu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
public class BookContent {
    private String title;
    private ArrayList<String> authors;
    private float ranking;
    private float rankingCount;
    private String abstractForBook;

    private boolean partOfSeries;
    private ArrayList<String> genreList;
    private float score;
    

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


    /*
        Constructor for creating a bookContent object off of an elasticsearch searchhit
     */
    public BookContent(SearchHit hit){
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();

        this.title = sourceAsMap.get("title").toString();
        this.authors = new ArrayList<>();

        ArrayList<String> authorList = (ArrayList<String>)sourceAsMap.get("authors");

        for (int j = 0 ; j < authorList.size() ; j++ ){
            this.authors.add(authorList.get(j));
        }
        this.ranking = Float.parseFloat(sourceAsMap.get("ranking").toString());
        this.rankingCount = Float.parseFloat(sourceAsMap.get("rankingCount").toString());
        this.abstractForBook = sourceAsMap.get("abstractForBook").toString();
        this.partOfSeries = Boolean.parseBoolean(sourceAsMap.get("partOfSeries").toString());

        this.genreList = new ArrayList<>();
        ArrayList<String> genreList = (ArrayList<String>)sourceAsMap.get("genreList");
        for (int j = 0 ; j < genreList.size() ; j++){
            this.genreList.add(genreList.get(j));
        }


    }

}



