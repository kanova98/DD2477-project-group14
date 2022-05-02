package com.tianyu;

import com.tianyu.pojo.BookContent;
import com.tianyu.service.BookContentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
public class BookContentServiceTest {

    BookContentService bookContentService = new BookContentService();
    // by running this function will let all the information in json file install into elastic search
    @Test
    void contextloads() throws IOException {
        //BookContentService bookContentService = new BookContentService();
        ArrayList<BookContent> bookContents_test = bookContentService.parseBookContent();
        for (int i = 0 ; i < bookContents_test.size() ; i++){
            System.out.println(bookContents_test.get(i));
        }
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
        ArrayList<BookContent> bookContentArrayList = bookContentService.searchBookAuthor("Hanna Hörl");
        for (int i = 0; i < bookContentArrayList.size(); i++) {
            System.out.println(bookContentArrayList.get(i));
        }
    }

    @Test
    void test_searchBookGenre() throws  IOException{
        ArrayList<BookContent> bookContentArrayList = bookContentService.searchBookGenre("Fantasy");
        for (int i = 0; i < bookContentArrayList.size(); i++) {
            System.out.println(bookContentArrayList.get(i));
        }
    }

    @Test
    void test_getAllBookList() throws IOException{
        ArrayList<BookContent> testArrayBookList = new ArrayList<BookContent>();
        ArrayList<BookContent> getAllBookList = bookContentService.getAllBookList();
        testArrayBookList.add(getAllBookList.get(0));
        testArrayBookList.add(getAllBookList.get(1));
        ArrayList<BookContent> getBookResult = bookContentService.getRecommendationList(testArrayBookList);
        for (int i = 0; i < getBookResult.size(); i++) {
            System.out.println(getBookResult.get(i));
        }
    }
}
