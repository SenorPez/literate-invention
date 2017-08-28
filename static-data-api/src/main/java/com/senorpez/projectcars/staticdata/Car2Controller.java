package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        value = "/cars",
        method = RequestMethod.GET,
        produces = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8"
)
@RestController
public class Car2Controller {
    @RequestMapping
    Resources<Object> cars() {
        return null;
    }
}
