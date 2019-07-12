package com.thinkit.cloud.jenkinsci.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import com.thinkit.cloud.jenkinsci.bean.JenkinsJob;
import com.thinkit.cloud.jenkinsci.service.JenkinsJobService;
import com.thinkit.cloud.jenkinsci.service.JenkinsService;
import com.thinkit.cloud.jenkinsci.util.FileHelper;
import com.thinkit.cloud.jenkinsci.util.ObjectToXmlUtil;
import com.thinkit.cloud.jenkinsci.util.ResultMessage;
import com.thinkit.cloud.jenkinsci.vo.JobInformationVO;
import com.zhongkexinli.micro.serv.common.pagination.Query;

@Controller
@RequestMapping("/")
public class JenkinsController {
    
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	 JenkinsServer jenkinsServer;
	 
    @Autowired
    JenkinsService jenkinsService;
    
	@Autowired
	private JenkinsJobService jenkinsJobService;
    

	/**
	 * 同步所有job
	 * @return
	 * @throws Exception
	 */
    @ResponseBody
    @PostMapping("/job/generateAllJob")
    public Map generateAllJob() throws Exception {
    	List<JenkinsJob>  jobs = jenkinsJobService.selectByExample(new Query());
    	
    	 Map<String,Object> retMap = new HashMap<String,Object>();
    	 
    	 retMap.put("respCode", "1");
    	 retMap.put("respMsg", "创建job成功");
         
         File filePath = FileHelper.getFile("templates");
         
         logger.info("模板文件路径:{}", filePath.getAbsolutePath());
         StringBuilder  exceptionMsg = new StringBuilder();
         for(JenkinsJob jenkinsJob: jobs) {
        	 try {
        	  	 
         		String jobName = jenkinsJob.getJobName();
       			String result = ObjectToXmlUtil.buil(filePath.getAbsolutePath(),jenkinsJob,"config.xml");
                 
       			logger.info("job模板内容:{}", result);
       			
                   if (jenkinsServer.getJob(jobName) != null) {
                  	 jenkinsServer.updateJob(jobName,result);
                  	logger.info("更新任务{} 成功", jobName);
                   }else {
                 	  jenkinsServer.createJob(jobName,result, false);
                 	  
                 	  logger.info("创建任务{} 成功", jobName);
                   }
                   
               } catch (Exception e) {
                   logger.error("创建job异常, job名称{}",jenkinsJob.getJobName(),e);
                   retMap.put("respCode", "0");
                   exceptionMsg.append("创建异常,job名称:" + jenkinsJob.getJobName() +"\r\n");
               }
         }
        
         
        return retMap;
    }
    
    /**
     *同步job
     */
    @ResponseBody
    @PostMapping("/job/generateJob/{id}" )
    public Map generateJob(@PathVariable("id") java.lang.Long id ) throws Exception {
    	Map<String,Object> retMap =new HashMap<>();
		JenkinsJob jenkinsJob =jenkinsJobService.selectByPrimaryKey(id);
		if(jenkinsJob== null) {
			jenkinsJob = new JenkinsJob();
		}
    	 
    	 retMap.put("respCode", "1");
    	 retMap.put("respMsg", "创建job成功");
         
         File filePath = FileHelper.getFile("templates");
         logger.info("模板文件路径:{}", filePath.getAbsolutePath());
         StringBuilder  exceptionMsg = new StringBuilder();
        	 try {
        	  	 
        		String jobName = jenkinsJob.getJobName();
      			String result = ObjectToXmlUtil.buil(filePath.getAbsolutePath(),jenkinsJob,"config.xml");
      			
      			logger.info("job模板内容:{}", result);
                
                  
                  if (jenkinsServer.getJob(jobName) != null) {
                 	 jenkinsServer.updateJob(jobName,result);
                 	logger.info("更新任务{} 成功", jobName);
                  }else {
                	  jenkinsServer.createJob(jobName,result, false);
                	  
                	  logger.info("创建任务{} 成功", jobName);
                  }
                  
              } catch (Exception e) {
                  logger.error("创建job异常, job名称{}",jenkinsJob.getJobName(),e);
                  retMap.put("respCode", "0");
                  exceptionMsg.append("创建异常,job名称:" + jenkinsJob.getJobName() +"\r\n");
              }
        
         
        return retMap;
    }
    
    /**
     *执行job
     */
    @ResponseBody
    @PostMapping("/job/buildJob/{id}" )
    public Map buildJob(@PathVariable("id") java.lang.Long id ) throws Exception {
    	Map<String,Object> retMap =new HashMap<>();
		JenkinsJob jenkinsJob =jenkinsJobService.selectByPrimaryKey(id);
		if(jenkinsJob== null) {
			jenkinsJob = new JenkinsJob();
		}
    	 
    	 retMap.put("respCode", "1");
    	 retMap.put("respMsg", "启动job成功");
         
	 try {
         	 Job job = jenkinsServer.getJob(jenkinsJob.getJobName());
             job.build();
      } catch (Exception e) {
          logger.error("构建job异常, job名称{}",jenkinsJob.getJobName(),e);
          retMap.put("respCode", "0");
      }
         
        return retMap;
    }
    

    @RequestMapping(value = "/job/{name}", method = RequestMethod.GET)
    @ResponseBody
    public JobInformationVO getJob(@PathVariable("name") String name) throws IOException {
        JobInformationVO jobInformationVO = jenkinsService.getJob(name);

        return jobInformationVO;
    }

    @RequestMapping(value = "/job/{name}", method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage createJob(@PathVariable("name") String name, String description, String url, String jenkinsFilePath) {
        try {
            return jenkinsService.createJob(name, description, url, jenkinsFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultMessage(false, "创建失败", null);
    }

    @RequestMapping(value = "/job/{name}/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage updateJob(@PathVariable("name") String name, String description, String jenkinsFilePath) {
        try {
            return jenkinsService.updateJob(name, description, jenkinsFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultMessage(false, "更新失败", null);
    }

    @RequestMapping(value = "/job/{name}/period", method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage periodJob(@PathVariable("name") String name, @RequestParam("period") String period) {
        System.out.println("period POST" + " period :" + period + "hours");
        try {
            return jenkinsService.periodJob(name, Integer.parseInt(period));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultMessage(false, "更新周期失败", null);
    }

    @RequestMapping(value = "/job/{name}/period", method = RequestMethod.GET)
    @ResponseBody
    public ResultMessage getJobPeriod(@PathVariable("name") String name) {
        try {
            return jenkinsService.getJobPeriod(name);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "获取周期失败", -1);
        }

    }

    @RequestMapping(value = "/job/{name}/doDelete", method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage deleteJob(@PathVariable("name") String name) {
        try {
            return jenkinsService.deleteJob(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultMessage(false, "删除失败", null);
    }


    @RequestMapping(value = "/createCredentials", method = RequestMethod.POST)
    @ResponseBody
    public void createCre(String id, String username, String privateKey) throws URISyntaxException, IOException {
       /* JenkinsHttpClient client = new JenkinsHttpClient(new URI(jenkinsURL.getJenkins()), jenkinsURL.getName(), jenkinsURL.getPassword());

        String json = "json={\"\":\"0\",\"credentials\":{\"scope\":\"GLOBAL\",\"id\":\"" + id + "\",\"username\":\"" + username + "\",\"password\":\"\",\"privateKeySource\":{\"stapler-class\":\"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey$DirectEntryPrivateKeySource\",\"privateKey\":\"" + privateKey + "\",},\"description\":\"\",\"stapler-class\":\"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey\"}}";
        String req = "json={\"\":\"0\",\"credentials\":{\"scope\":\"GLOBAL\",\"username\":\"" + username + "\",\"password\":\"\",\"id\":\"" + id + "\",\"description\":\"\",\"$class\":\"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\"}}";
        //client.post_text("credentials/store/system/domain/_/createCredentials",req, ContentType.APPLICATION_FORM_URLENCODED,true);
        client.post_text("credentials/store/system/domain/_/createCredentials", json, ContentType.APPLICATION_FORM_URLENCODED, false);*/
    }
    
    @RequestMapping(value = "/freemarkTest", method = RequestMethod.POST)
    @ResponseBody
    public String freemarkTest() throws URISyntaxException, IOException {
	    try {
	    	File filePath = FileHelper.getFile("classpath:templates");
	  	  
			Map<String,Object> map= new HashMap<>();
			map.put("GIT_URL", "github.com/aa.git");
			map.put("GIT_BRANCH", "master");
			map.put("GIT_CREDIT", "aaa");
			map.put("IS_RUN_SONAR", true);
			map.put("IS_MAVEN_DEBUG", false);
			map.put("IS_RUN_SONAR_HTML", false);
			
			String result = ObjectToXmlUtil.buil(filePath.getAbsolutePath(),map,"config.xml");
			
			return result;
	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    
	    return null;
    }

}