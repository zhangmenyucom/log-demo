package com.taylor.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taylor.log.dao.TestDao;
import com.taylor.log.entity.Test;
import com.taylor.log.service.TestService;

@Service
public class TestServiceImpl implements TestService{
	@Autowired
	private TestDao testDao;
	
	@Override
	public List<Test> queryTest(Test test){
		return testDao.select(test);
	}

}
