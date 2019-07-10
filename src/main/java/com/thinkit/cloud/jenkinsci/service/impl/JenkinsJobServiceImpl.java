package com.thinkit.cloud.jenkinsci.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.thinkit.cloud.jenkinsci.dao.JenkinsJobMapper;
import com.thinkit.cloud.jenkinsci.service.JenkinsJobService;
import com.thinkit.cloud.jenkinsci.bean.JenkinsJob;

import com.zhongkexinli.micro.serv.common.msg.LayUiTableResultResponse;
import com.zhongkexinli.micro.serv.common.pagination.Query;

@Service
public class JenkinsJobServiceImpl  implements JenkinsJobService{
	
	@Autowired
	private JenkinsJobMapper jenkinsJobMapper;
	
	@Override
	public java.lang.Integer deleteByPrimaryKey(java.lang.Long id) {
		return jenkinsJobMapper.deleteByPrimaryKey(id);
	}

	@Override
	public java.lang.Integer insert(JenkinsJob jenkinsJob){
		return jenkinsJobMapper.insert(jenkinsJob);
	}

	@Override
	public java.lang.Integer insertSelective(JenkinsJob jenkinsJob) {
		return jenkinsJobMapper.insertSelective(jenkinsJob);
	}

	@Override
	public JenkinsJob selectByPrimaryKey(java.lang.Long id) {
		return jenkinsJobMapper.selectByPrimaryKey(id);
	}

	@Override
	public java.lang.Integer updateByPrimaryKeySelective(JenkinsJob jenkinsJob) {
		return jenkinsJobMapper.updateByPrimaryKeySelective(jenkinsJob);
	}

	@Override
	public java.lang.Integer updateByPrimaryKey(JenkinsJob jenkinsJob) {
		return jenkinsJobMapper.updateByPrimaryKey(jenkinsJob);
	}

	@Override
	 public LayUiTableResultResponse selectByQuery(Query query) {
	        com.github.pagehelper.Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
	        List<Map<String,Object>> list  = jenkinsJobMapper.selectByPageExample(query);
	        return new LayUiTableResultResponse(result.getTotal(), list);
	}

	@Override
	public List<JenkinsJob> selectByExample(Query query) {
		return jenkinsJobMapper.selectByExample(query);
	}

}
