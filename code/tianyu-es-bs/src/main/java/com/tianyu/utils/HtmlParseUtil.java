package com.tianyu.utils;

import com.tianyu.pojo.TestContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlParseUtil {
    //test
    public static void main(String[] args) throws IOException {
        new HtmlParseUtil().parseJD("java").forEach(System.out::println);
    }

    public List<TestContent> parseJD(String keywords) throws IOException {
        String url = "https://search.jd.com/Search?keyword="+keywords;

        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        System.out.println(element.html());

        Elements elements = element.getElementsByTag("li");

        ArrayList<TestContent> goodsList = new ArrayList<>();

        for (Element el : elements) {
            if (el.attr("class").equalsIgnoreCase("gl-item"))         {
                String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
                String price = el.getElementsByClass("p-price").eq(0).text();
                String title = el.getElementsByClass("p-name").eq(0).text();
                TestContent testContent = new TestContent();
                testContent.setTitle(title);
                testContent.setPrice(price);
                testContent.setImg(img);
                goodsList.add(testContent);
            }
        }
        return goodsList;
    }
}
