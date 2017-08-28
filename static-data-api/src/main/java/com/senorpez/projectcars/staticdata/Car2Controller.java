package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        value = "/cars",
        method = RequestMethod.GET
)
@RestController
public class Car2Controller {
    @RequestMapping
    Resources<Object> cars() {
        return null;
    }
}
