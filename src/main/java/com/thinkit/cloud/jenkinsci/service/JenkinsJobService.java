package com.thinkit.cloud.jenkinsci.service;

import java.util.List;

import com.thinkit.cloud.jenkinsci.bean.JenkinsJob;
import com.zhongkexinli.micro.serv.common.msg.LayUiTableResultResponse;
import com.zhongkexinli.micro.serv.common.pagination.Query;


public interface JenkinsJobService  {

    public java.lang.Integer deleteByPrimaryKey(java.lang.Long id);

    public java.lang.Integer insert(JenkinsJob jenkinsJob);

    public java.lang.Integer insertSelective(JenkinsJob jenkinsJob);

    public JenkinsJob selectByPrimaryKey(java.lang.Long id);

    public java.lang.Integer updateByPrimaryKeySelective(JenkinsJob jenkinsJob);

    public java.lang.Integer updateByPrimaryKey(JenkinsJob jenkinsJob);

    public LayUiTableResultResponse	 selectByQuery(Query query) ;

    public  List<JenkinsJob> selectByExample(Query query);
}
