package com.tianyu.Controller;

import com.alibaba.fastjson.JSONObject;
import com.tianyu.service.BookContentService;
import com.tianyu.pojo.BookContent;
import com.tianyu.service.TestContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BookContentController {
    /*
     * Receives the title of a book that the user has read from the frontend.
     * Should mark the book as read in the class for the user
     */
    @PostMapping("/read/{title}")
    public void markBookAsRead(@PathVariable("title") String title) throws IOException {
        // TODO mark book as read in backend information about user
        System.out.println(title);
    }

    /*
     * Should return the books found using the query string "searched" as JSON objects
     * TODO should not return a string
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/books/{searched}")
    public BookContent findSearchedBooks(@PathVariable("searched") String searched) throws IOException {
        BookContentService bookContentService = new BookContentService();
        BookContent result = bookContentService.searchBookTitle(searched); // Enter the search into elasticsearch
        if(result == null){
            return new BookContent();
        }
        // TODO make function communicate with backend to perform search in elasticsearch and return json object books
        System.out.println("Retrieved the search: " + searched);
        return result;
    }
    /*
     * TODO Should return recommended books for the current backend user as Json objects
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/books/recommendations")
    public String getRecommendedBooks() {

        String jsonStr = "[{\"title\": \"A Prayer for Owen Meany\",\n" +
                "            \"authors\": [\n" +
                "                \"John Irving\"\n" +
                "            ],\n" +
                "            \"ranking\": 4.24,\n" +
                "            \"ranking_count\": 301234,\n" +
                "            \"abstract\": \"Eleven-year-old Owen Meany, playing in a Little League baseball game in Gravesend, New Hampshire, hits a foul ball and kills his best friend's mother. Owen doesn't believe in accidents; he believes he is God's instrument. What happens to Owen after that 1953 foul is both extraordinary and terrifying. At moments a comic, self-deluded victim, but in the end the principal, tragic actor in a divine plan, Owen Meany is the most heartbreaking hero John Irving has yet created.\"}]";


        return jsonStr;
    }

}
