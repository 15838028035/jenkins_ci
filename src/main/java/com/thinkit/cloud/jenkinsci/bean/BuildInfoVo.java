package com.thinkit.cloud.jenkinsci.bean;

/**
 * 构建请求参数vo
 *
 */
public class BuildInfoVo {

	/**
	 * jobming名称
	 */
	private String JOB_NAME;
	
	private String GIT_URL;
	
	private String GIT_BRANCH;
	
	/**
	 * jenkins凭据参数
	 */
	private String GIT_CREDIT;
	
	/**
	 * 是否执行sonar扫描
	 */
	private Boolean IS_RUN_SONAR;
	
	/**
	 * 是否执行sonar html扫描
	 */
	private Boolean IS_RUN_SONAR_HTML;
	
	/**
	 * 是否执行docker镜像
	 */
	private Boolean IS_GEN_DOCKER_IMG;
	
	/**
	 *  是否发布到nexus中
	 */
	private Boolean IS_DEPLOY_NEXUS;
	/**
	 * 镜像模块名称
	 */
	private String MODEL_NAMES;
	
	
	/**
	 * 是否发送邮件通知
	 */
	private Boolean IS_SEND_EMAIL;
	
	/**
	 *  是否启用maven调试，默认值false
	 */
	private Boolean IS_MAVEN_DEBUG;


	public String getJOB_NAME() {
		return JOB_NAME;
	}

	public void setJOB_NAME(String jOB_NAME) {
		JOB_NAME = jOB_NAME;
	}

	public String getGIT_URL() {
		return GIT_URL;
	}

	public void setGIT_URL(String gIT_URL) {
		GIT_URL = gIT_URL;
	}

	public String getGIT_BRANCH() {
		return GIT_BRANCH;
	}

	public void setGIT_BRANCH(String gIT_BRANCH) {
		GIT_BRANCH = gIT_BRANCH;
	}

	public String getGIT_CREDIT() {
		return GIT_CREDIT;
	}

	public void setGIT_CREDIT(String gIT_CREDIT) {
		GIT_CREDIT = gIT_CREDIT;
	}

	public Boolean getIS_RUN_SONAR() {
		return IS_RUN_SONAR;
	}

	public void setIS_RUN_SONAR(Boolean iS_RUN_SONAR) {
		IS_RUN_SONAR = iS_RUN_SONAR;
	}

	public Boolean getIS_RUN_SONAR_HTML() {
		return IS_RUN_SONAR_HTML;
	}

	public void setIS_RUN_SONAR_HTML(Boolean iS_RUN_SONAR_HTML) {
		IS_RUN_SONAR_HTML = iS_RUN_SONAR_HTML;
	}

	public Boolean getIS_GEN_DOCKER_IMG() {
		return IS_GEN_DOCKER_IMG;
	}

	public void setIS_GEN_DOCKER_IMG(Boolean iS_GEN_DOCKER_IMG) {
		IS_GEN_DOCKER_IMG = iS_GEN_DOCKER_IMG;
	}

	public Boolean getIS_DEPLOY_NEXUS() {
		return IS_DEPLOY_NEXUS;
	}

	public void setIS_DEPLOY_NEXUS(Boolean iS_DEPLOY_NEXUS) {
		IS_DEPLOY_NEXUS = iS_DEPLOY_NEXUS;
	}

	public String getMODEL_NAMES() {
		return MODEL_NAMES;
	}

	public void setMODEL_NAMES(String mODEL_NAMES) {
		MODEL_NAMES = mODEL_NAMES;
	}

	public Boolean getIS_SEND_EMAIL() {
		return IS_SEND_EMAIL;
	}

	public void setIS_SEND_EMAIL(Boolean iS_SEND_EMAIL) {
		IS_SEND_EMAIL = iS_SEND_EMAIL;
	}

	public Boolean getIS_MAVEN_DEBUG() {
		return IS_MAVEN_DEBUG;
	}

	public void setIS_MAVEN_DEBUG(Boolean iS_MAVEN_DEBUG) {
		IS_MAVEN_DEBUG = iS_MAVEN_DEBUG;
	}
	
}
