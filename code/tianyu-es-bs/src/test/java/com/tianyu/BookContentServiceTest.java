package com.tianyu;

import com.tianyu.pojo.BookContent;
import com.tianyu.service.BookContentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
public class BookContentServiceTest {

    // by running this function will let all the information in json file install into elastic search
    @Test
    void contextloads() throws IOException {
        BookContentService bookContentService = new BookContentService();
        ArrayList<BookContent> bookContents_test = bookContentService.parseBookContent();
        for (int i = 0 ; i < bookContents_test.size() ; i++){
            System.out.println(bookContents_test.get(i));
        }
    }

    @Test
    void test_searchBookTitle() throws IOException {
        BookContentService bookContentService = new BookContentService();
        bookContentService.searchBookTitle("The Hunger Games");
    }
}
