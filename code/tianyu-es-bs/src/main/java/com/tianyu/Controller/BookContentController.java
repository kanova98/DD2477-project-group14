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
        // TODO mark book as read in backend information about user

        for(int i = 0; i < displayedBooks.size(); i++){
            if(displayedBooks.get(i).getTitle().equals(title)){

                //TODO solve issue of adding same book twice
                currentUser.addBook(displayedBooks.get(i));
                displayedBooks.remove(i);
                break;
            }
        }
        System.out.println(currentUser.getReadBooks().size());


    }

    /*
     * Should return the books found using the query string "searched" as JSON objects
     * TODO should return more than 1 result ideally
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
     * TODO Should return recommended books for the current backend user as Json objects
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/books/recommendations")
    public List<BookContent> getRecommendedBooks() throws IOException{

        BookContentService service = new BookContentService();
        System.out.println(currentUser.computeAbstractCentroid());
        ArrayList<BookContent> recommendedBooks = service.getRecommendationList(currentUser.getReadBooks());

        return recommendedBooks;
    }

}
