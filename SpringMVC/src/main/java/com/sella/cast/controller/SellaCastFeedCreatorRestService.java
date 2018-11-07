package com.sella.cast.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sella.cast.beans.Country;
import com.sella.cast.dao.SellaCastFeedDAO;

@RestController
public class SellaCastFeedCreatorRestService {

	@RequestMapping(value = "/addFeed", method = RequestMethod.POST,headers="Accept=application/json" ) 
	
	public String insertFeeder(@RequestBody String request)
	{ 
//		SellaCastFeedDAO.getInstance().insertArchiveFeed(jsonObject);
		
		System.out.println("request :: "+request);
		
		return "success";
		
	}
	
}
