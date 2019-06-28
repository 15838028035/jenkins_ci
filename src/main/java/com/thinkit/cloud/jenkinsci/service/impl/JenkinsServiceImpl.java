package com.thinkit.cloud.jenkinsci.service.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.thinkit.cloud.jenkinsci.service.JenkinsService;
import com.thinkit.cloud.jenkinsci.util.FileHelper;
import com.thinkit.cloud.jenkinsci.util.ObjectToXmlUtil;
import com.thinkit.cloud.jenkinsci.util.ResultMessage;
import com.thinkit.cloud.jenkinsci.vo.BuildInformationVO;
import com.thinkit.cloud.jenkinsci.vo.BuildVO;
import com.thinkit.cloud.jenkinsci.vo.JobInformationVO;

@Service
public class JenkinsServiceImpl implements JenkinsService {
	
	@Autowired
    JenkinsServer jenkinsServer;

    @Override
    public ResultMessage createJob(String name, String description, String url, String jenkinsfilePath) throws IOException {

        if (jenkinsServer.getJob(name) != null) {
            return new ResultMessage(false, "已存在名为" + name + "的项目", null);
        }

        StringBuilder build = new StringBuilder();
        try {
        	File filePath = FileHelper.getFile("classpath:templates");
  	  	  
			Map<String,Object> map= new HashMap<>();
			map.put("GIT_URL", "github.com/aa.git");
			map.put("GIT_BRANCH", "master");
			map.put("GIT_CREDIT", "aaaaaaaaaaaaaa");
			map.put("IS_RUN_SONAR", true);
			map.put("IS_MAVEN_DEBUG", false);
			map.put("IS_RUN_SONAR_HTML", false);
			
			String result = ObjectToXmlUtil.buil(filePath.getAbsolutePath(),map,"config.xml");

            jenkinsServer.createJob(name,result, false);
            return new ResultMessage(true, "创建成功", getJob(name));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "创建失败", null);
        }

    }

    @Override
    public ResultMessage buildJob(String name) {

        try {
            Job job = jenkinsServer.getJob(name);
            job.build();
            return new ResultMessage(true, "构建成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(true, "构建失败", null);
        }

    }

    @Override
    public JobInformationVO getJob(String name) {
        JobWithDetails job = null;
        JobInformationVO jobInformationVO = null;
        List<BuildVO> buildVOS = new ArrayList<>();

        try {
            int health = 0;
            job = jenkinsServer.getJob(name);

        /*    List<Build> buildList = job.getAllBuilds();
            System.out.println();
            System.out.println();
            System.out.println(buildList.size());
            System.out.println();
            System.out.println();
            for (Build build : buildList) {
                BuildVO buildVO = new BuildVO(build.getNumber(), build.getUrl());
                buildVOS.add(buildVO);
            }
            if (buildVOS.size() <= 0) {
                health = 0;
            } else {
                for (int i = buildList.size() - 5; i < buildList.size(); i++) {
                    if (i >= 0 && buildList.get(i).details().getResult().toString().equals("SUCCESS")) {
                        health++;
                    }
                }
            }*/
            BuildVO lastBuild = new BuildVO(job.getLastBuild().getNumber(), job.getLastBuild().getUrl());
            BuildVO lastFailBuild = new BuildVO(job.getLastFailedBuild().getNumber(), job.getLastFailedBuild().getUrl());
            BuildVO lastSuccessfulBuild = new BuildVO(job.getLastSuccessfulBuild().getNumber(), job.getLastSuccessfulBuild().getUrl());
            BuildVO lastStablefulBuild = new BuildVO(job.getLastStableBuild().getNumber(), job.getLastStableBuild().getUrl());
            //jobInformationVO = new JobInformationVO(job.getName(), job.getUrl(), buildVOS, "构建稳定性:过去5次构建中" + (5 - health) + "次失败", lastBuild, lastFailBuild, lastSuccessfulBuild, lastStablefulBuild, job.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobInformationVO;
    }

    @Override
    public ResultMessage updateJob(String name, String description, String jenkinsfilePath) throws IOException {
        if (jenkinsServer.getJob(name) == null) {
            return new ResultMessage(false, "不存在名为" + name + "的项目", null);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        try {
            // 从XML文档中获取DOM文档实例
            DocumentBuilder db = dbf.newDocumentBuilder();
            // 获取Document对象
            Document xmldoc = db.parse("/var/lib/jenkins/jobs/" + name + "/config.xml");

            // 获取根节点
            Element root = xmldoc.getDocumentElement();
            // 定位id为001的节点

            // 将age节点的内容更改为28
            root.getElementsByTagName("description").item(0).setTextContent(description);

            //root.getElementsByTagName("name").item(0).setTextContent(branch);
            root.getElementsByTagName("scriptPath").item(0).setTextContent(jenkinsfilePath);

            // 保存
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer former = factory.newTransformer();
            former.transform(new DOMSource(xmldoc), new StreamResult(new File("/home/xujianghe/jenkins/config/config-" + name + "-.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder build = new StringBuilder();
        try {
            InputStream in = new FileInputStream("/home/xujianghe/jenkins/config/config-" + name + "-.xml");
            InputStreamReader read = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                build.append(lineTxt);
            }

            //System.out.println(build.toString());
            jenkinsServer.updateJob(name, build.toString());
            return new ResultMessage(true, "更新成功", getJob(name));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "更新失败", null);
        }

    }

    @Override
    public ResultMessage periodJob(String name, int period) throws IOException {
        Job job = jenkinsServer.getJob(name);
        if (job == null) {
            return new ResultMessage(false, "未找到名为" + name + "的项目", null);
        }
        if (period <= 0 || period >= 24) {
            return new ResultMessage(false, "时间间隔请保持在0-23小时", null);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        try {
            // 从XML文档中获取DOM文档实例
            DocumentBuilder db = dbf.newDocumentBuilder();
            // 获取Document对象
            Document xmldoc = db.parse("/var/lib/jenkins/jobs/" + name + "/config.xml");

            // 获取根节点
            Element root = xmldoc.getDocumentElement();
            // 定位id为001的节点

            // 将age节点的内容更改为28
            root.getElementsByTagName("interval").item(0).setTextContent(String.valueOf(3600000 * period));


            // 保存
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer former = factory.newTransformer();
            former.transform(new DOMSource(xmldoc), new StreamResult(new File("/home/xujianghe/jenkins/config/config-" + name + "-.xml")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        StringBuilder build = new StringBuilder();
        try {
            InputStream in = new FileInputStream("/home/xujianghe/jenkins/config/config-" + name + "-.xml");
            InputStreamReader read = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                build.append(lineTxt);
            }
            //String jobname = "oopsa";
            //System.out.println(build.toString());
            jenkinsServer.updateJob(name, build.toString());

            return new ResultMessage(true, "触发周期更新成功", period);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "触发周期更新失败", null);
        }
    }

    @Override
    public ResultMessage deleteJob(String name) throws IOException {
        Job job = jenkinsServer.getJob(name);
        if (job == null) {
            return new ResultMessage(false, "未找到名为" + name + "的项目", null);
        }
        try {
            jenkinsServer.deleteJob(name);
            return new ResultMessage(true, "已删除名为" + name + "的项目", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultMessage(false, "删除失败", null);
    }

    @Override
    public BuildInformationVO getBuild(String name, String number) throws IOException {
        BuildWithDetails build = jenkinsServer.getJob(name).getBuildByNumber(Integer.valueOf(number)).details();

        return new BuildInformationVO(build.isBuilding(), build.getDescription(), build.getDisplayName(), build.getDuration(), build.getEstimatedDuration(), build.getFullDisplayName(), build.getId(), build.getNumber(), build.getQueueId(), build.getResult().toString(), build.getTimestamp(), build.getUrl());
    }

    @Override
    public ResultMessage getJobPeriod(String name) throws IOException {
        Job job = jenkinsServer.getJob(name);
        if (job == null) {
            return new ResultMessage(false, "未找到名为" + name + "的项目", -1);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        // 从XML文档中获取DOM文档实例
        DocumentBuilder db = null;
        String result = "";
        try {
            db = dbf.newDocumentBuilder();
            Document xmldoc = db.parse("/var/lib/jenkins/jobs/" + name + "/config.xml");
            Element root = xmldoc.getDocumentElement();
            result = root.getElementsByTagName("interval").item(0).getTextContent();
            //System.out.println();
            //System.out.println();
            System.out.println(result);
            //System.out.println();
            //System.out.println();
            return new ResultMessage(true, "", Integer.parseInt(result) / 3600000);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return new ResultMessage(false, "获取周期失败", -1);
        } catch (SAXException e) {
            e.printStackTrace();
            return new ResultMessage(false, "获取周期失败", -1);
        }
    }


    @Override
    public ResultMessage createCredentials(String name, String SSHPrivateKey) throws URISyntaxException {
      /*  JenkinsHttpClient client = new JenkinsHttpClient(new URI(jenkinsURL.getJenkins()), jenkinsURL.getName(), jenkinsURL.getPassword());
        String json = "json={\"\":\"0\",\"credentials\":{\"scope\":\"GLOBAL\",\"id\":\"" + name + "\",\"username\":\"" + name + "\",\"password\":\"\",\"privateKeySource\":{\"stapler-class\":\"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey$DirectEntryPrivateKeySource\",\"privateKey\":\"" + SSHPrivateKey + "\",},\"description\":\"\",\"stapler-class\":\"com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey\"}}";

        String result = "";
        //client.post_text("credentials/store/system/domain/_/createCredentials",req, ContentType.APPLICATION_FORM_URLENCODED,true);
        try {
            result = client.post_text("credentials/store/system/domain/_/createCredentials", json, ContentType.APPLICATION_FORM_URLENCODED, false);
            return new ResultMessage(true, "创建认证成功！", result);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultMessage(false, "创建认证失败！", result);
        }*/
    	
    	return null;

    }


}