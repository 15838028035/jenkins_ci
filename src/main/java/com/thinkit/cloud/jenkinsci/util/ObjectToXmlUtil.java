package com.thinkit.cloud.jenkinsci.util;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.thinkit.cloud.jenkinsci.bean.BuildInfoVo;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;  
  
public class ObjectToXmlUtil  {
	
	/**
     * <一句话功能简述>
     * <功能详细描述>
     * @param map map
     * @param fileName fileName
     * @return 字符串流
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static String buil(String path,BuildInfoVo buildInfoVo, String fileName)
    {
        StringWriter out = new StringWriter();
        try
        {
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(path));
 
            Template template = configuration.getTemplate(fileName);
            template.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            template.setEncoding("UTF-8");
            template.setOutputEncoding("UTF-8");
            Environment env = template.createProcessingEnvironment(buildInfoVo, out);
 
            env.process();
            out.flush();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
 
        return out.toString();
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param map map
     * @param fileName fileName
     * @return 字符串流
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static String buil(String path,Map map, String fileName)
    {
        StringWriter out = new StringWriter();
        try
        {
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(path));
 
            Template template = configuration.getTemplate(fileName);
            template.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            template.setEncoding("UTF-8");
            template.setOutputEncoding("UTF-8");
            Environment env = template.createProcessingEnvironment(map, out);
 
            env.process();
            out.flush();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
 
        return out.toString();
    }

 
  public static void main(String args[]){
	  	File filePath = FileHelper.getFile("classpath:templates");
	  
		Map<String,Object> map= new HashMap<>();
		map.put("GIT_URL", "github.com/aa.git");
		map.put("GIT_BRANCH", "master");
		map.put("GIT_CREDIT", "aaa");
		map.put("IS_RUN_SONAR", true);
		map.put("IS_MAVEN_DEBUG", false);
		map.put("IS_RUN_SONAR_HTML", false);
		
		String result = buil(filePath.getAbsolutePath(),map,"config.xml");
		
		System.out.println(result);
 
  }
  
    
}
