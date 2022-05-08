package com.tianyu;

import com.tianyu.pojo.BookContent;
import com.tianyu.service.BookContentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
public class BookContentServiceTest {

    BookContentService bookContentService = new BookContentService();
    // Use this function to Insert all the books into elasticsearch.s
    @Test
    void contextloads() throws IOException {
        //BookContentService bookContentService = new BookContentService();
        ArrayList<BookContent> bookContents_test = bookContentService.parseBookContent();
        System.out.println("In Total: "+ bookContents_test.size()+ " Books");
        //for (int i = 0 ; i < bookContents_test.size() ; i++){
        //    System.out.println(bookContents_test.get(i));
        //}
    }


    @Test
    void test_searchBookTitle() throws IOException {
        ArrayList<BookContent> bookContentArrayList = bookContentService.searchBookTitle("The Hunger Games");
        for (int i = 0; i < bookContentArrayList.size(); i++) {
            System.out.println(bookContentArrayList.get(i));
        }
    }

    @Test
    void test_searchBookAuthor() throws  IOException{
        /*
        HashMap<> bookContentArrayList = bookContentService.searchBookAuthor(new ArrayList<>());
        for (int i = 0; i < bookContentArrayList.size(); i++) {
            System.out.println(bookContentArrayList.get(i));
        }
        BookContent b1 = bookContentArrayList.get(0);
        BookContent b2 = new BookContent(b1.getTitle(), b1.getAuthors(), b1.getRanking(), b1.getRankingCount(), b1.getAbstractForBook(), b1.isPartOfSeries(),b1.getGenreList(), b1.getScore());
        System.out.println("First book");
        System.out.println(b1);
        System.out.println("Second book");
        System.out.println(b2);
        System.out.println(b1.equals(b2));
        */
    }

    @Test
    void test_searchBookGenre() throws  IOException{
        /*
        ArrayList<BookContent> bookContentArrayList = bookContentService.searchBookGenre("Fantasy");

        for (int i = 0; i < bookContentArrayList.size(); i++) {
            System.out.println(bookContentArrayList.get(i));
        }

         */
    }

    @Test
    void test_getAllBookList() throws IOException{
        ArrayList<BookContent> testArrayBookList = new ArrayList<BookContent>();
        ArrayList<BookContent> getAllBookList = bookContentService.getAllBookList();
        System.out.println(getAllBookList.size());// check so it matches with amount of books inserted,
        //testArrayBookList.add(getAllBookList.get(0));
        //testArrayBookList.add(getAllBookList.get(1));
        //ArrayList<BookContent> getBookResult = bookContentService.getRecommendationList(testArrayBookList, "test", new HashMap<>());
        //for (int i = 0; i < getBookResult.size(); i++) {
            //System.out.println(getBookResult.get(i));
        //}
    }
}
