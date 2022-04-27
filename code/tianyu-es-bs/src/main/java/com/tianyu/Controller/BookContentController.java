package com.tianyu.Controller;

import com.tianyu.service.TestContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/books/{searched}")
    public String findSearchedBooks(@PathVariable("searched") String searched) throws IOException {

        // TODO make function communicate with backend to perform search in elasticsearch and return json object books
        System.out.println("Retrieved the search: " + searched);
        return "temp";
    }
    /*
     * TODO Should return recommended books for the current backend user as Json objects
     */
    @GetMapping("/books/recommendations")
    public String getRecommendedBooks(){

        return Null;
    }

}
