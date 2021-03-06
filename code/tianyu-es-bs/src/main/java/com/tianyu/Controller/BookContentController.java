package com.tianyu.Controller;

import com.alibaba.fastjson.JSONObject;
import com.tianyu.pojo.User;
import com.tianyu.service.BookContentService;
import com.tianyu.pojo.BookContent;
import com.tianyu.service.TestContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class BookContentController{

    public ArrayList<BookContent> displayedBooks;
    private User currentUser = new User(3);

    public BookContentController() throws IOException {
    }

    /*
     * Receives the title of a book that the user has read from the frontend.
     * Should mark the book as read in the class for the user
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/read/{title}")
    public void markBookAsRead(@PathVariable("title") String title) throws IOException {


        for(int i = 0; i < displayedBooks.size(); i++){
            System.out.println("Entered displayedbooks");
            System.out.println(displayedBooks.get(i).getTitle());
            System.out.println(title);
            if(displayedBooks.get(i).getTitle().equals(title)){

                if(!currentUser.getReadBooks().contains(displayedBooks.get(i))){
                    currentUser.addBook(displayedBooks.get(i));
                }

                displayedBooks.remove(i);
                break;
            }
        }
        System.out.println(currentUser.getReadBooks().size());


    }

    /*
     * Should return the books found using the query string "searched" as JSON objects
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/books/{searched}")
    public List<BookContent> findSearchedBooks(@PathVariable("searched") String searched) throws IOException {
        displayedBooks = new ArrayList<>();

        BookContentService bookContentService = new BookContentService();
        ArrayList<BookContent> result = bookContentService.searchBookTitle(searched); // Enter the search into elasticsearch
        displayedBooks = result;

        System.out.println("Retrieved the search: " + searched);
        System.out.println(displayedBooks.size());
        return result;
    }
    /*
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/books/recommendations")
    public List<BookContent> getRecommendedBooks() throws IOException{
        if(currentUser.getReadBooks().size() == 0){
            return new ArrayList<>();
        }
        BookContentService service = new BookContentService();
        String abstractCentroid = currentUser.computeAbstractCentroid();
        HashMap<String, Float> genreWeights = currentUser.computeGenreWeights();
        ArrayList<BookContent> recommendedBooks = service.getRecommendationList(currentUser.getReadBooks(), abstractCentroid, genreWeights);

        return recommendedBooks;
    }

}
