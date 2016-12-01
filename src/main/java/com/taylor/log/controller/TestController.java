package com.taylor.log.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taylor.log.entity.Test;
import com.taylor.log.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

	@Autowired
	private TestService testService;

	@ResponseBody
	@RequestMapping("/query")
	public List<Test> queryTest(Test test, HttpServletRequest request, HttpServletResponse response) {
		return testService.queryTest(test);
	}

}