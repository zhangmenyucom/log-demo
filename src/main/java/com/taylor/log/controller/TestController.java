package com.taylor.log.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.taylor.log.cat.CatTransaction;
import com.taylor.log.entity.Test;
import com.taylor.log.service.TestService;

@RequestMapping("/test")
@Controller
@CatTransaction
public class TestController extends BaseAction {

	@Autowired
	private TestService testService;

	@ResponseBody
	@RequestMapping("/query")
	public List<Test> queryTest(Test test, HttpServletRequest request, HttpServletResponse response) {
		Transaction t = Cat.newTransaction("test", "hahahahahahahah");
		try {
			Cat.logEvent("test", "hello world~");
			Cat.logMetricForCount("PayCount");
			Cat.logMetricForSum("PayCount", 123);
			t.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			t.setStatus(e);
			e.printStackTrace();
		} finally {
			t.complete();
		}

		return testService.queryTest(test);
	}

}
