package com.taylor.log.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="test")
public class Test {
	
	@Id
	private Integer id;
	
	private String name;

}
