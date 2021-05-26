package com.bukazoid.smarty.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller // <1>
public class HomeController {

	@RequestMapping(value = { "/", "/login", "/admin/**", "/admin", "/user/**", "/user" })
	public String index() {
		return "index";
	}

}
