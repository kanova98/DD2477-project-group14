package com.tianyu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookContent {

    private String title;
    private ArrayList<String> authors;
    private int ranking;
    private int rankingCount;
    private String abstractForBook;
    private boolean partOfSeries;
    private ArrayList<String> genreList;

}
