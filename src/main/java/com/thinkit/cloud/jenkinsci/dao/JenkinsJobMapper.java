package com.thinkit.cloud.jenkinsci.dao;

import java.util.List;
import java.util.Map;

import com.thinkit.cloud.jenkinsci.bean.JenkinsJob;


public interface JenkinsJobMapper  {

    public java.lang.Integer deleteByPrimaryKey(java.lang.Long id);

    public java.lang.Integer insert(JenkinsJob jenkinsJob);

    public java.lang.Integer insertSelective(JenkinsJob jenkinsJob);

    public JenkinsJob selectByPrimaryKey(java.lang.Long id);

    public java.lang.Integer updateByPrimaryKeySelective(JenkinsJob jenkinsJob);

    public java.lang.Integer updateByPrimaryKey(JenkinsJob jenkinsJob);

   /**
     * 根据条件查询列表
     *
     * @param example
     */
   public  List<JenkinsJob> selectByExample(Object mapAndObject);

   /**
     * 根据条件查询列表
     *
     * @param example
     */
    public List<Map<String,Object>> selectByPageExample(Object mapAndObject);
}
