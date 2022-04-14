package com.tianyu.Controller;

import com.tianyu.service.TestContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class TestContentController {
    @Autowired
    private TestContentService testContentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keywords) throws IOException {
        return testContentService.parseContent(keywords);
    }

    // for running function search
    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable String keyword,
                                           @PathVariable int pageNo,
                                           @PathVariable int pageSize) throws IOException {
        return  testContentService.searchPage(keyword,pageNo,pageSize);
    }


}
