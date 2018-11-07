package com.sella.cast.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sella.cast.util.DateUtil;
import com.sella.cast.util.SellaCastUtil;

public class SellaCastFeedDAO { 
	private static SellaCastFeedDAO instance;
	private SellaCastFeedDAO(){} 
	public static synchronized SellaCastFeedDAO getInstance(){
        if(instance == null){
            instance = new SellaCastFeedDAO();
        }
        return instance;
    }
	
 	static Logger logger = Logger.getLogger(SellaCastFeedDAO.class);

 	public void insertArchiveFeed(JSONObject jsonObject) throws UnknownHostException, ParseException {
		DBCollection archiveTable = SellaCastUtil.getMongodbTable("mongodb_feeder_collection_name");
		BasicDBObject newFeed = new BasicDBObject();
		newFeed.put("image", jsonObject.get("image"));
		newFeed.put("idfeed", jsonObject.get("idfeed"));
		newFeed.put("link", jsonObject.get("link"));
		newFeed.put("description", jsonObject.get("description"));
		newFeed.put("source", jsonObject.get("source"));
		newFeed.put("startdate", jsonObject.get("startdate"));
		newFeed.put("likecount", jsonObject.get("likecount"));
		newFeed.put("unlikecount", jsonObject.get("unlikecount"));
		newFeed.put("enddate", jsonObject.get("enddate"));
		newFeed.put("publishedDate", jsonObject.get("publishedDate"));
		newFeed.put("categories", jsonObject.get("categories"));
		newFeed.put("headline", jsonObject.get("headline"));
		newFeed.put("retentiontime", jsonObject.get("retentiontime"));
		newFeed.put("iscurated", jsonObject.get("iscurated"));
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray =(JSONArray) parser.parse((String) jsonObject.get("audience"));

		String[] audienceArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			audienceArray[i] = jsonArray.get(i).toString();
		}
		newFeed.put("audience", audienceArray);
		archiveTable.insert(newFeed);

	}

	public String getAliveFeeds(String feedType) throws UnknownHostException {
		DBCollection table = SellaCastUtil.getMongodbTable("mongodb_feeder_collection_name");
		JSONObject jsonObject = new JSONObject(); 
		try {
		String todayStr = DateUtil.formateDateToString(new Date(), "yyyyMMdd");
		BasicDBObject query = new BasicDBObject();
		query.put("startdate", new BasicDBObject("$lte", todayStr));
		query.put("enddate", new BasicDBObject("$gte", todayStr));
		if (feedType != null) {
			query.put("categories", feedType);
		}

		DBCursor dbCursor = table.find(query);
		List<DBObject> results = dbCursor.toArray();
		Collection<JSONObject> jsonColl = new ArrayList<JSONObject>();
		logger.debug("###################### MONGODB RECORD SIZE :" + results.size());
		for (int i = 0; i < results.size(); i++) {
			DBObject dbObject = (DBObject) results.get(i);
			JSONParser parser = new JSONParser();
			
			jsonObject = (JSONObject) parser.parse(dbObject.toString()); 
			jsonColl.add(jsonObject);
		}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return jsonObject.toString();
	}
	
}
