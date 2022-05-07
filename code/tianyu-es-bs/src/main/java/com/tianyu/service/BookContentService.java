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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.awt.print.Book;
import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

        for (int f = 0 ; f <= 160 ; f++ ){
            String filename = readJsonFile("/Users/filipkana/Documents/Skolarbete/DD2477/irProject/Data/dataset" + String.valueOf(f) + ".json");
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
                if (!bookContentArrayList.contains(bookContent)){
                    bookContentArrayList.add(bookContent);
                }
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

        if(searchResponse.getHits().getHits().length == 0){
            return null;
        }
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

    /*
     * Does a weighted query to elasticsearch where authors that the user has read prior books from
     * are given a higher score
     */
    public HashMap<BookContent, Float> searchBookAuthor (ArrayList<BookContent> readBooks) throws IOException {

        // Create a hashmap from author to occurrences
        HashMap<String, Integer> authorOccurences = new HashMap<>();
        for(BookContent book : readBooks){
            for(String author : book.getAuthors()){
                if(!authorOccurences.containsKey(author)){
                    authorOccurences.put(author, 1);
                }
                else{
                    authorOccurences.put(author, authorOccurences.get(author) + 1);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        float booksInTotal = readBooks.size();
        for(Map.Entry<String, Integer> author : authorOccurences.entrySet()){
            sb.append("\"");
            sb.append(author.getKey());
            sb.append("\"");
            sb.append("^");
            sb.append(author.getValue() / booksInTotal);
            sb.append(" ");
        }
        String keyword = sb.toString();

        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("authors",keyword);
        sourceBuilder.size(1000);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // Create map for scores
        HashMap<BookContent, Float> authorScores = new HashMap<>();
        SearchHit[] results = searchResponse.getHits().getHits();
        Float highestScore = 0.0F;
        for (int i = 0 ; i < results.length; i++){
            BookContent foundBook = new BookContent(results[i]);
            Float score = results[i].getScore();
            if(!readBooks.contains(foundBook)){
                if(score > highestScore){
                    highestScore = score;
                }
                authorScores.put(foundBook,score);
                System.out.println(foundBook);
                System.out.println(score);
            }
        }
        // Normalize score by dividing with the highest
        authorScores = normalizeScores(authorScores, highestScore);

        return authorScores;
    }
    /*
     * Normalizes a hashmap with bookcontent and scores, by dividing each score by the parameter score.
     */
    private HashMap<BookContent, Float> normalizeScores(HashMap<BookContent, Float> booksWithScores, Float score){
        for(BookContent book : booksWithScores.keySet()){
            float normVal = booksWithScores.get(book) / score;
            booksWithScores.put(book, normVal);
        }
        return booksWithScores;
    }

    public HashMap<BookContent, Float> searchBookGenre (ArrayList<BookContent> readBooks, HashMap<String, Float> genreWeights) throws IOException {

        StringBuilder sb = new StringBuilder();
        for(String genre : genreWeights.keySet()){
            sb.append(genre);
            sb.append(" ");
        }
        String keyword = sb.toString();
        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("genreList",keyword);
        sourceBuilder.size(1000);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] results = searchResponse.getHits().getHits();

        HashMap<BookContent, Float> booksWithScores = new HashMap<>();
        float highestScore = 0.0F;
        for (int i = 0 ; i < results.length ; i++) {
            BookContent searchResult = new BookContent(results[i]);

            ArrayList<String> genreList = searchResult.getGenreList();
            float score = 0;
            for (int j = 0; j < genreList.size(); j++) {

                if (genreWeights.containsKey(genreList.get(j))) {
                    score += genreWeights.get(genreList.get(j)) * 1 / (float) (j + 1);
                }
            }
            if(!readBooks.contains(searchResult)){
                if(score > highestScore){
                    highestScore = score;
                }
                booksWithScores.put(searchResult, score);
            }

        }
        booksWithScores = normalizeScores(booksWithScores, highestScore);
        return booksWithScores;
    }

    public ArrayList<BookContent> getRecommendationList (ArrayList<BookContent> listRead, String abstractCentroid, HashMap<String,Float> genreWeights) throws IOException {
        System.out.println(listRead.size()+ " Books read in total");
        HashMap<BookContent, Float> genreScores =  searchBookGenre(listRead, genreWeights);
        HashMap<BookContent, Float> authorScores = searchBookAuthor(listRead);
        HashMap<BookContent, Float> abstractScores = getAbstractRecommendations(listRead, abstractCentroid);
        ArrayList<HashMap<BookContent, Float>> scores = new ArrayList<>();
        scores.add(genreScores);
        scores.add(authorScores);
        scores.add(abstractScores);

        HashMap<BookContent, Float> unionBooks = union(scores);
        return  findTopThree(unionBooks);
    }
    /*
     * Retrieves the three books with the highest scores
     */
    private ArrayList<BookContent> findTopThree(HashMap<BookContent, Float> scores){
        ArrayList<BookContent> books = new ArrayList<>();
        float nr1 = 0.0F;
        float nr2 = 0.0F;
        float nr3 = 0.0F;

        for(Map.Entry<BookContent, Float> entry : scores.entrySet()){
            if(entry.getValue() > nr1){
                nr3 = nr2;
                nr2 = nr1;
                nr1 = entry.getValue();
                books.add(0, entry.getKey());
            }
            else if (entry.getValue() > nr2){
                nr3 = nr2;
                nr2 = entry.getValue();
                books.add(1, entry.getKey());
            } else if (entry.getValue() > nr3) {
                nr3 = entry.getValue();
                books.add(2, entry.getKey());
            }
            if(books.size() > 3){
                books.remove(3);
            }
        }
        return books;

    }

    /*
     * Queries elasticsearch using weighted query string "abstractCentroid" which has been calculated
     * from the centroid of the current users read books. Returns an arraylist of books ranked by elasticsearch
     * based on how relevant they are in relation to the weighted query string
     */
    private HashMap<BookContent, Float> getAbstractRecommendations(ArrayList<BookContent> readBooks, String abstractCentroid) throws IOException{
        SearchRequest searchRequest = new SearchRequest("book_list");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("abstractForBook", abstractCentroid);
        sourceBuilder.size(1000);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        HashMap<BookContent, Float> abstractScores = new HashMap<>();
        SearchHit[] results = searchResponse.getHits().getHits();
        float highestScore = 0.0F;
        for (int i = 0 ; i < results.length; i++){
            BookContent foundBook = new BookContent(results[i]);
            Float score = results[i].getScore();
            if(!readBooks.contains(foundBook)){
                if(score > highestScore){
                    highestScore = score;
                }
                abstractScores.put(foundBook,score);
            }
        }
        abstractScores = normalizeScores(abstractScores, highestScore);
        return abstractScores;

    }

    /*
     * Does a union of lists of bookcontent. combines the score of multiple entries.
     * Also gives scores based on book ranking and ranking count.
     */
    private HashMap<BookContent, Float> union(ArrayList<HashMap<BookContent, Float>> books){
        HashMap<BookContent, Float> unionScores = new HashMap<>();
        float mostRatings = 0.0F;
        for(HashMap<BookContent, Float> bookMap : books){
            for(Map.Entry<BookContent,Float> book : bookMap.entrySet()){
                if(book.getKey().getRankingCount() > mostRatings){
                    mostRatings = book.getKey().getRankingCount();
                }
                if(unionScores.containsKey(book.getKey())){
                    Float prevVal = unionScores.get(book.getKey());
                    Float newVal = prevVal + book.getValue();
                    unionScores.put(book.getKey(), newVal);
                }
                else{
                    unionScores.put(book.getKey(), book.getValue());
                }
                //System.out.println("book with score;");
                //System.out.println(book);
                //System.out.println(unionScores.get(book.getKey()));
            }
        }
        // We give a maximum of 2 extra points based on the amount of people that have rated the book and the rating it has received
        for(BookContent book : unionScores.keySet()){
            Float ratingScore = (book.getRanking() / (float) 5) + book.getRankingCount() / mostRatings;
            Float oldscore = unionScores.get(book);
            unionScores.put(book, ratingScore + oldscore);
            System.out.println("Book with title "+ book.getTitle() + " Received score "+ unionScores.get(book));
        }

        return unionScores;
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
