package com.thinkit.cloud.jenkinsci.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.offbytwo.jenkins.JenkinsServer;
import com.thinkit.cloud.jenkinsci.bean.JenkinsJob;
import com.thinkit.cloud.jenkinsci.service.JenkinsJobService;
import com.thinkit.cloud.jenkinsci.service.JenkinsService;
import com.zhongkexinli.micro.serv.common.bean.RestApiResult2;
import com.zhongkexinli.micro.serv.common.msg.LayUiTableResultResponse;
import com.zhongkexinli.micro.serv.common.pagination.Query;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *jenkins任务信息管理
 */
@Api(value = "jenkins任务信息服务", tags = "jenkins任务信息服务接口")
@RestController()
public class JenkinsJobController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JenkinsJobService jenkinsJobService;
	

	@Autowired
	 JenkinsServer jenkinsServer;
	 
    @Autowired
    JenkinsService jenkinsService;
    
	
	@ApiOperation(value = "分页列表")
	@GetMapping(value = "/api/JenkinsJob")
	public LayUiTableResultResponse page(@RequestParam(defaultValue = "10") int limit,
	      @RequestParam(defaultValue = "1") int offset,@RequestParam Map<String, Object> params) {
			Query query= new Query(params);
			return  jenkinsJobService.selectByQuery(query);
	}
	 
		@ApiOperation(value = "新增")
		@PostMapping(value = "/api/JenkinsJob")
		public RestApiResult2 create(@ModelAttribute JenkinsJob jenkinsJob,HttpServletRequest request)  {
			
			try {
					Long createBy = getLoginId(request);
					jenkinsJob.setCreateUserId(createBy);
					jenkinsJob.setCreateUserName(getUserName(request));
					jenkinsJob.setCreateTime(new Date());
					jenkinsJobService.insertSelective(jenkinsJob);
					
				}catch(Exception e) {
					logger.error("[jenkins任务信息]-->新增失败" ,e);
					return new RestApiResult2().respCode(0).respMsg("新增失败 {}" ,e.getMessage());
				}
				
				return new RestApiResult2();
	}
	 
		@ApiOperation(value = "更新")
		@PutMapping(value="/api/JenkinsJob/{id}")
		public RestApiResult2 update(@PathVariable("id") java.lang.Long id ,@ModelAttribute JenkinsJob jenkinsJob,HttpServletRequest request)  {
			try {
					
					Long createBy = getLoginId(request);
					jenkinsJob.setUpdateUserId(createBy);
					jenkinsJob.setUpdateUserName(getUserName(request));
					jenkinsJob.setUpdateTime(new Date());
					jenkinsJobService.updateByPrimaryKeySelective(jenkinsJob);
					
				}catch(Exception e) {
					logger.error("[jenkins任务信息]-->更新失败" ,e);
					return new RestApiResult2().respCode(0).respMsg("更新失败 {}" ,e.getMessage());
				}
				
				return new RestApiResult2();
	}
		
	/** 显示 */
	@ApiOperation(value = "查看")
	@GetMapping(value="/api/JenkinsJob/{id}")
	public JenkinsJob show(@PathVariable("id") java.lang.Long id )  {
		JenkinsJob jenkinsJob =jenkinsJobService.selectByPrimaryKey(id);
		if(jenkinsJob== null) {
			jenkinsJob = new JenkinsJob();
		}
		return jenkinsJob;
	}
		
	/** 物理删除 */
	@ApiOperation(value = "物理删除")
	@DeleteMapping(value="/api/JenkinsJob/{id}")
	public RestApiResult2 delete(@PathVariable("id") java.lang.Long id ) {
		JenkinsJob jenkinsJob =jenkinsJobService.selectByPrimaryKey(id);
		if(jenkinsJob== null) {
			jenkinsJob = new JenkinsJob();
		}
		
		jenkinsJobService.deleteByPrimaryKey(id);
		
	  try {
          jenkinsService.deleteJob(jenkinsJob.getJobName());
        } catch (Exception e) {
            e.printStackTrace();
        }
		  
		return new RestApiResult2();
	}

	/** 显示 */
	@ApiOperation(value = "显示")
	@GetMapping(value="/api/JenkinsJob/showInfo/{id}")
	public  Map<String,Object> showInfo(@PathVariable("id") java.lang.Long id ){
		Map<String,Object> retMap =new HashMap<>();
		JenkinsJob jenkinsJob =jenkinsJobService.selectByPrimaryKey(id);
		if(jenkinsJob== null) {
			jenkinsJob = new JenkinsJob();
		}
		
		retMap.put("jenkinsJob", jenkinsJob);
		
		return retMap;
	}
	
	@ApiOperation(value = "列表")
	@GetMapping(value = "/api/JenkinsJob/queryList")
	public RestApiResult2 queryList(@RequestParam Map<String, Object> params) {
			Query query= new Query(params);
			List<JenkinsJob> list = jenkinsJobService.selectByExample(query);
			return new RestApiResult2().respData(list);
	}
}

