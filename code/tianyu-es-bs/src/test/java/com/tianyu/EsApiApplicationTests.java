package com.tianyu;

import com.alibaba.fastjson.JSON;
import com.tianyu.pojo.TestForElasticSearchContent;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//test for es7.6 API
@SpringBootTest
class EsApiApplicationTests {
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Test
    void testCreateIndex() throws IOException {
        // create index request
        CreateIndexRequest request = new CreateIndexRequest("tianyu_index");
        // do request
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);

    }

    // test for get index true for exists
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("book_list");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
    @Test
    void checkNumberOfDocs() throws IOException {
        CountRequest countRequest = new CountRequest("book_list");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        countRequest.source(searchSourceBuilder);

        CountResponse resp = client.count(countRequest, RequestOptions.DEFAULT);
        System.out.println(resp.getCount());
    }

    // test for delete index
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("tianyu_index");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //test for create doc
    @Test
    void testAddDocument() throws IOException {
        //create user
        TestForElasticSearchContent testForElasticSearchContent = new TestForElasticSearchContent("tianyu", 23);

        //create request
        IndexRequest request = new IndexRequest("tianyu_index");

        //set rules for "put /tianyu_index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        // insert data
        request.source(JSON.toJSONString(testForElasticSearchContent), XContentType.JSON);

        //client send request, receive feed back
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }

    //test for find doc
    @Test
    void testIsExists() throws IOException {
        GetRequest getRequest = new GetRequest("tianyu_index","1");
        //settings for test
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        boolean exists = client.exists(getRequest,RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //test for get doc message
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("tianyu_index","1");
        GetResponse getResponse = client.get(getRequest,RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse);
    }

    //test for update doc message
    @Test
    void testUpdateRequest() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("tianyu_index","1");
        updateRequest.timeout("1s");

        TestForElasticSearchContent testForElasticSearchContent = new TestForElasticSearchContent("TianyuFan",18);
        updateRequest.doc(JSON.toJSONString(testForElasticSearchContent),XContentType.JSON);

        UpdateResponse updateResponse = client.update(updateRequest,RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    //test for delete doc message
    @Test
    void testdeleteRequest() throws IOException {
        DeleteRequest request = new DeleteRequest("tianyu_index", "1");
        request.timeout("1s");

        DeleteResponse deleteResponse = client.delete(request,RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    //test for group option
    @Test
    void testBulkRequest() throws IOException{
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        ArrayList<Object> userList = new ArrayList<>();
        userList.add(new TestForElasticSearchContent("tianyu1",1));
        userList.add(new TestForElasticSearchContent("tianyu2",2));
        userList.add(new TestForElasticSearchContent("tianyu3",3));
        userList.add(new TestForElasticSearchContent("tianyu4",4));
        userList.add(new TestForElasticSearchContent("tianyu5",5));

        for (int i = 0 ; i < userList.size() ; i++){
            bulkRequest.add(new IndexRequest("tianyu_index")
                    .id(""+(i+1))
                    .source(JSON.toJSONString(userList.get(i)),XContentType.JSON));
        }
        BulkResponse bulkResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponses.hasFailures());
    }

    //test for search option
    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("tianyu_index");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //test for querybuilders
        //term for accurate search
        //all for all search
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "tianyu1");
        //MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(termQueryBuilder);
        //sourseBuilder.from();
        //sourceBuilder.size();
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("===================");
        for(SearchHit documentFields : searchResponse.getHits().getHits()){
            System.out.println(documentFields.getSourceAsMap());
        }
    }
}
