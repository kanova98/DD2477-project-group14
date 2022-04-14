package com.tianyu.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestIndexController {
    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }
}
