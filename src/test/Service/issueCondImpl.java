package com.tastmanager.test.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tastmanager.test.requestBody.issueListCondition;
import com.tastmanager.test.serviceInterface.issueCondService;

import DBconnection.IssueEntity;

@Service
public class issueCondImpl implements issueCondService{
	@Resource
	private MongoTemplate mongotemplate; 
	@Override
	public List<IssueEntity> getPartialIssueList(issueListCondition issueconditions){
		Query query = new Query();
		List<Criteria> conditions = new ArrayList<Criteria>();    
	    if(issueconditions.getStatus().length>0) {
	    	for(String s:issueconditions.getStatus()) {
		    	conditions.add(Criteria.where("status").is(s));
	    	}
	    }
	    Criteria[] c = new Criteria[conditions.size()];
		if(conditions != null && conditions.size()>0) {
	        for(int i=0; i<conditions.size();i++) { 
	        	c[i]=conditions.get(i);
	        }
			Criteria criteria =new Criteria().andOperator(c);
			query.addCriteria(criteria);
			query.with(new Sort(Sort.Direction.ASC, "_id"));
			query.limit(50);
		    return mongotemplate.find(query, IssueEntity.class);
		}else {
			Query query1 = new Query();
			query1.with(new Sort(Sort.Direction.ASC, "_id"));
			query1.limit(50);
			return mongotemplate.find(query1, IssueEntity.class);
		}
	}

}
