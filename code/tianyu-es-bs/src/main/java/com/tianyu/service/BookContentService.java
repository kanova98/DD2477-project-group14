package com.tianyu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tianyu.pojo.BookContent;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BookContentService {
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("127.0.0.1",9200,"http")
            )
    );

    public ArrayList<BookContent> parseBookContent() throws IOException {

        ArrayList<BookContent> bookContentArrayList = new ArrayList<BookContent>();

        for (int f = 1 ; f <= 10 ; f++ ){
            String filename = readJsonFile("D:\\DD2477-project-group14-main\\Data\\dataset" + String.valueOf(f) + ".json");
            JSONObject jobj = JSON.parseObject(filename);

            int bookListLength = jobj.getJSONArray("books").size();

            for (int i = 0; i < bookListLength; i++) {
                BookContent bookContent = new BookContent();
                JSONObject books = jobj.getJSONArray("books").getJSONObject(i);
                bookContent.setTitle(books.get("title").toString());
                for (int k = 0; k < books.getJSONArray("authors").size(); k++) {
                    bookContent.add_authors(books.getJSONArray("authors").get(k).toString());
                }
                bookContent.setRanking(Float.parseFloat(books.get("ranking").toString()));
                bookContent.setRankingCount(Float.parseFloat(books.get("ranking_count").toString()));
                bookContent.setAbstractForBook(books.get("abstract").toString());
                bookContent.setPartOfSeries(Boolean.parseBoolean(books.get("part_of_series").toString()));
                for (int k = 0; k < books.getJSONArray("genre_list").size(); k++) {
                    bookContent.add_genreList(books.getJSONArray("genre_list").get(k).toString());
                }
                bookContentArrayList.add(bookContent);
            }
        }

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        ArrayList<BookContent> bookContents_test = bookContentArrayList;

        for (int i = 0 ; i < bookContents_test.size() ; i++){
            bulkRequest.add(new IndexRequest("book_list")
                    .id(""+(i+1))
                    .source(JSON.toJSONString(bookContents_test.get(i)),XContentType.JSON));
        }
        BulkResponse bulkResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponses.hasFailures());

        return bookContentArrayList;
    }

    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    public BookContent searchBookTitle (String keyword) throws IOException {

        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyword);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Map<String, Object> sourceAsMap = searchResponse.getHits().getHits()[0].getSourceAsMap();
        BookContent searchResult = new BookContent();

        searchResult.setTitle(sourceAsMap.get("title").toString());
        ArrayList<String> authorList = (ArrayList<String>)sourceAsMap.get("authors");
        for (int i = 0 ; i < authorList.size() ; i++ ){
            searchResult.add_authors(authorList.get(i));
        }
        searchResult.setRanking(Float.parseFloat(sourceAsMap.get("ranking").toString()));
        searchResult.setRankingCount(Float.parseFloat(sourceAsMap.get("rankingCount").toString()));
        searchResult.setAbstractForBook(sourceAsMap.get("abstractForBook").toString());
        searchResult.setPartOfSeries(Boolean.parseBoolean(sourceAsMap.get("partOfSeries").toString()));
        ArrayList<String> genreList = (ArrayList<String>)sourceAsMap.get("genreList");
        for (int i = 0 ; i < genreList.size() ; i++){
            searchResult.add_genreList(genreList.get(i));
        }
        System.out.println(searchResult);
        return searchResult;
    }
    */
    public ArrayList<BookContent> searchBookTitle (String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("title",keyword);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<BookContent> bookContentArrayList = new ArrayList<BookContent>();
        for (int i = 0 ; i < searchResponse.getHits().getHits().length ; i++){
            BookContent searchResult = new BookContent();
            Map<String, Object> sourceAsMap = searchResponse.getHits().getHits()[i].getSourceAsMap();

            searchResult.setTitle(sourceAsMap.get("title").toString());
            ArrayList<String> authorList = (ArrayList<String>)sourceAsMap.get("authors");
            for (int j = 0 ; j < authorList.size() ; j++ ){
                searchResult.add_authors(authorList.get(j));
            }
            searchResult.setRanking(Float.parseFloat(sourceAsMap.get("ranking").toString()));
            searchResult.setRankingCount(Float.parseFloat(sourceAsMap.get("rankingCount").toString()));
            searchResult.setAbstractForBook(sourceAsMap.get("abstractForBook").toString());
            searchResult.setPartOfSeries(Boolean.parseBoolean(sourceAsMap.get("partOfSeries").toString()));
            ArrayList<String> genreList = (ArrayList<String>)sourceAsMap.get("genreList");
            for (int j = 0 ; j < genreList.size() ; j++){
                searchResult.add_genreList(genreList.get(j));
            }

            bookContentArrayList.add(searchResult);
        }
        return bookContentArrayList;
    }

    public ArrayList<BookContent> searchBookAuthor (String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("authors",keyword);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<BookContent> bookContentArrayList = new ArrayList<BookContent>();
        for (int i = 0 ; i < searchResponse.getHits().getHits().length ; i++){
            BookContent searchResult = new BookContent();
            Map<String, Object> sourceAsMap = searchResponse.getHits().getHits()[i].getSourceAsMap();

            searchResult.setTitle(sourceAsMap.get("title").toString());
            ArrayList<String> authorList = (ArrayList<String>)sourceAsMap.get("authors");
            for (int j = 0 ; j < authorList.size() ; j++ ){
                searchResult.add_authors(authorList.get(j));
            }
            searchResult.setRanking(Float.parseFloat(sourceAsMap.get("ranking").toString()));
            searchResult.setRankingCount(Float.parseFloat(sourceAsMap.get("rankingCount").toString()));
            searchResult.setAbstractForBook(sourceAsMap.get("abstractForBook").toString());
            searchResult.setPartOfSeries(Boolean.parseBoolean(sourceAsMap.get("partOfSeries").toString()));
            ArrayList<String> genreList = (ArrayList<String>)sourceAsMap.get("genreList");
            for (int j = 0 ; j < genreList.size() ; j++){
                searchResult.add_genreList(genreList.get(j));
            }

            bookContentArrayList.add(searchResult);
        }
        return bookContentArrayList;
    }

    public ArrayList<BookContent> searchBookGenre (String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("genreList",keyword);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<BookContent> bookContentArrayList = new ArrayList<BookContent>();
        for (int i = 0 ; i < searchResponse.getHits().getHits().length ; i++){
            BookContent searchResult = new BookContent();
            Map<String, Object> sourceAsMap = searchResponse.getHits().getHits()[i].getSourceAsMap();

            searchResult.setTitle(sourceAsMap.get("title").toString());
            ArrayList<String> authorList = (ArrayList<String>)sourceAsMap.get("authors");
            for (int j = 0 ; j < authorList.size() ; j++ ){
                searchResult.add_authors(authorList.get(j));
            }
            searchResult.setRanking(Float.parseFloat(sourceAsMap.get("ranking").toString()));
            searchResult.setRankingCount(Float.parseFloat(sourceAsMap.get("rankingCount").toString()));
            searchResult.setAbstractForBook(sourceAsMap.get("abstractForBook").toString());
            searchResult.setPartOfSeries(Boolean.parseBoolean(sourceAsMap.get("partOfSeries").toString()));
            ArrayList<String> genreList = (ArrayList<String>)sourceAsMap.get("genreList");
            for (int j = 0 ; j < genreList.size() ; j++){
                searchResult.add_genreList(genreList.get(j));
            }

            bookContentArrayList.add(searchResult);
        }
        return bookContentArrayList;
    }

    public ArrayList<BookContent> getRecommendationList (ArrayList<BookContent> listRead) throws IOException {
        ArrayList<BookContent> recommendationList = new ArrayList<BookContent>();
        for (int i = 0; i < listRead.size(); i++) {
            BookContent bookRead = listRead.get(i);
            for (int j = 0; j < bookRead.getAuthors().size(); j++) {
                ArrayList<BookContent> resultAuthorList = searchBookAuthor(bookRead.getAuthors().get(j));
                for (int k = 0; k < resultAuthorList.size(); k++) {
                    if (!recommendationList.contains(resultAuthorList.get(k))){
                        recommendationList.add(resultAuthorList.get(k));
                    }
                }
            }
            for (int j = 0; j < bookRead.getGenreList().size(); j++) {
                ArrayList<BookContent> resultGenreList = searchBookGenre(bookRead.getGenreList().get(j));
                for (int k = 0; k < resultGenreList.size(); k++) {
                    if (!recommendationList.contains(resultGenreList.get(k))){
                        recommendationList.add(resultGenreList.get(k));
                    }
                }
            }
        }
        return  recommendationList;
    }

    public ArrayList<BookContent> getAllBookList () throws IOException {
        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<BookContent> bookContentArrayList = new ArrayList<BookContent>();
        for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
            BookContent searchResult = new BookContent();
            Map<String, Object> sourceAsMap = searchResponse.getHits().getHits()[i].getSourceAsMap();

            searchResult.setTitle(sourceAsMap.get("title").toString());
            ArrayList<String> authorList = (ArrayList<String>) sourceAsMap.get("authors");
            for (int j = 0; j < authorList.size(); j++) {
                searchResult.add_authors(authorList.get(j));
            }
            searchResult.setRanking(Float.parseFloat(sourceAsMap.get("ranking").toString()));
            searchResult.setRankingCount(Float.parseFloat(sourceAsMap.get("rankingCount").toString()));
            searchResult.setAbstractForBook(sourceAsMap.get("abstractForBook").toString());
            searchResult.setPartOfSeries(Boolean.parseBoolean(sourceAsMap.get("partOfSeries").toString()));
            ArrayList<String> genreList = (ArrayList<String>) sourceAsMap.get("genreList");
            for (int j = 0; j < genreList.size(); j++) {
                searchResult.add_genreList(genreList.get(j));
            }

            bookContentArrayList.add(searchResult);
        }
        return bookContentArrayList;
    }
}
