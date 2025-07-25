package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

    // Match all routes except those containing a dot (like .js, .css, .png)
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forward() {
        return "forward:/index.html";
    }

    // Match nested routes (like /product/view or /user/profile)
    @RequestMapping(value = "/**/{path:^(?!api|auth|static|assets|.*\\..*).*$}")
    public String forwardNested() {
        return "forward:/index.html";
    }
}
