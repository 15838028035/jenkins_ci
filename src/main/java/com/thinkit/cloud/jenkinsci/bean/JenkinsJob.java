package com.thinkit.cloud.jenkinsci.bean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
*jenkins任务信息
*/
@ApiModel(value = "jenkins任务信息")
public class JenkinsJob extends MyBaseEntity{
	
	/**
	 * ID  id
	 */
	@ApiModelProperty(value = "ID")
	private java.lang.Long id;
	
	/**
	 * job名称  job_name
	 */
	@ApiModelProperty(value = "job名称")
	private String jobName = "";
	
	/**
	 * git url地址  GIT_URL
	 */
	@ApiModelProperty(value = "git url地址")
	private String gitUrl = "";
	
	/**
	 * 分支名称  GIT_BRANCH
	 */
	@ApiModelProperty(value = "分支名称")
	private String gitBranch = "";
	
	/**
	 * jenkins凭据参数  GIT_CREDIT
	 */
	@ApiModelProperty(value = "jenkins凭据参数")
	private String gitCredit = "";
	
	/**
	 * 是否使用git tag  IS_USE_GIT_TAG
	 */
	@ApiModelProperty(value = "是否使用git tag")
	private java.lang.Integer isUseGitTag;
	
	/**
	 * 是否执行sonar扫描  IS_RUN_SONAR
	 */
	@ApiModelProperty(value = "是否执行sonar扫描")
	private java.lang.Integer isRunSonar;
	
	/**
	 * 是否执行sonar html扫描  IS_RUN_SONAR_HTML
	 */
	@ApiModelProperty(value = "是否执行sonar html扫描")
	private java.lang.Integer isRunSonarHtml;
	
	/**
	 * 是否执行docker镜像  IS_GEN_DOCKER_IMG
	 */
	@ApiModelProperty(value = "是否执行docker镜像")
	private java.lang.Integer isGenDockerImg;
	
	/**
	 * 是否发布到nexus中  IS_DEPLOY_NEXUS
	 */
	@ApiModelProperty(value = "是否发布到nexus中")
	private java.lang.Integer isDeployNexus;
	
	/**
	 * 镜像模块名称  MODEL_NAMES
	 */
	@ApiModelProperty(value = "镜像模块名称")
	private String modelNames = "";
	
	/**
	 * 是否发送邮件通知  IS_SEND_EMAIL
	 */
	@ApiModelProperty(value = "是否发送邮件通知")
	private java.lang.Integer isSendEmail;
	
	/**
	 * 是否启用maven调试，默认值false  IS_MAVEN_DEBUG
	 */
	@ApiModelProperty(value = "是否启用maven调试，默认值false")
	private java.lang.Integer isMavenDebug;
	
	/**
	 * 创建人  create_user_id
	 */
	@ApiModelProperty(value = "创建人")
	private java.lang.Long createUserId;
	
	/**
	 * 创建人姓名  create_user_name
	 */
	@ApiModelProperty(value = "创建人姓名")
	private String createUserName = "";
	
	/**
	 * 创建时间  create_time
	 */
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	
	 /**
	 * 创建时间Begin
	 */
	private String  createTimeBegin;
	/**
	 * 创建时间End
	 */
	private String createTimeEnd;
	/**
	 * 更新人  update_user_id
	 */
	@ApiModelProperty(value = "更新人")
	private java.lang.Long updateUserId;
	
	/**
	 * 更新人姓名  update_user_name
	 */
	@ApiModelProperty(value = "更新人姓名")
	private String updateUserName = "";
	
	/**
	 * 更新时间  update_time
	 */
	@ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	
	 /**
	 * 更新时间Begin
	 */
	private String  updateTimeBegin;
	/**
	 * 更新时间End
	 */
	private String updateTimeEnd;


	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	
	public void setJobName(String value) {
		this.jobName = value;
	}
	
	public String getJobName() {
		return this.jobName;
	}
	
	public void setGitUrl(String value) {
		this.gitUrl = value;
	}
	
	public String getGitUrl() {
		return this.gitUrl;
	}
	
	public void setGitBranch(String value) {
		this.gitBranch = value;
	}
	
	public String getGitBranch() {
		return this.gitBranch;
	}
	
	public void setGitCredit(String value) {
		this.gitCredit = value;
	}
	
	public String getGitCredit() {
		return this.gitCredit;
	}
	
	public void setIsUseGitTag(java.lang.Integer value) {
		this.isUseGitTag = value;
	}
	
	public java.lang.Integer getIsUseGitTag() {
		return this.isUseGitTag;
	}
	
	public Boolean  getIsUseGitTagBoolean() {
		return this.isUseGitTag == 1;
	}
	
	public void setIsRunSonar(java.lang.Integer value) {
		this.isRunSonar = value;
	}
	
	public java.lang.Integer getIsRunSonar() {
		return this.isRunSonar;
	}
	
	public java.lang.Boolean getIsRunSonarBoolean() {
		return this.isRunSonar == 1;
	}
	
	public void setIsRunSonarHtml(java.lang.Integer value) {
		this.isRunSonarHtml = value;
	}
	
	public java.lang.Integer getIsRunSonarHtml() {
		return this.isRunSonarHtml;
	}
	
	public java.lang.Boolean getIsRunSonarHtmlBoolean() {
		return this.isRunSonarHtml == 1;
	}
	
	public void setIsGenDockerImg(java.lang.Integer value) {
		this.isGenDockerImg = value;
	}
	
	public java.lang.Integer getIsGenDockerImg() {
		return this.isGenDockerImg;
	}
	
	public java.lang.Boolean getIsGenDockerImgBoolean() {
		return this.isGenDockerImg == 1;
	}
	
	public void setIsDeployNexus(java.lang.Integer value) {
		this.isDeployNexus = value;
	}
	
	public java.lang.Integer getIsDeployNexus() {
		return this.isDeployNexus;
	}
	

	public java.lang.Boolean getIsDeployNexusBoolean() {
		return this.isDeployNexus == 1;
	}
	
	public void setModelNames(String value) {
		this.modelNames = value;
	}
	
	public String getModelNames() {
		return this.modelNames;
	}
	
	public void setIsSendEmail(java.lang.Integer value) {
		this.isSendEmail = value;
	}
	
	public java.lang.Integer getIsSendEmail() {
		return this.isSendEmail;
	}
	
	public java.lang.Boolean getIsSendEmailBoolean() {
		return this.isSendEmail==1;
	}
	
	public void setIsMavenDebug(java.lang.Integer value) {
		this.isMavenDebug = value;
	}
	
	public java.lang.Integer getIsMavenDebug() {
		return this.isMavenDebug;
	}
	
	public java.lang.Boolean getIsMavenDebugBoolean() {
		return this.isMavenDebug == 1;
	}
	
	public void setCreateUserId(java.lang.Long value) {
		this.createUserId = value;
	}
	
	public java.lang.Long getCreateUserId() {
		return this.createUserId;
	}
	
	public void setCreateUserName(String value) {
		this.createUserName = value;
	}
	
	public String getCreateUserName() {
		return this.createUserName;
	}
	
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
    	public void setCreateTimeBegin(String value) {
            this.createTimeBegin = value;
        }
        
        public String  getCreateTimeBegin() {
            return this.createTimeBegin;
        }
        
        public void setCreateTimeEnd(String value) {
            this.createTimeEnd = value;
        }
        
        public String  getCreateTimeEnd() {
            return this.createTimeEnd;
        }
	public void setUpdateUserId(java.lang.Long value) {
		this.updateUserId = value;
	}
	
	public java.lang.Long getUpdateUserId() {
		return this.updateUserId;
	}
	
	public void setUpdateUserName(String value) {
		this.updateUserName = value;
	}
	
	public String getUpdateUserName() {
		return this.updateUserName;
	}
	
	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}
	
    	public void setUpdateTimeBegin(String value) {
            this.updateTimeBegin = value;
        }
        
        public String  getUpdateTimeBegin() {
            return this.updateTimeBegin;
        }
        
        public void setUpdateTimeEnd(String value) {
            this.updateTimeEnd = value;
        }
        
        public String  getUpdateTimeEnd() {
            return this.updateTimeEnd;
        }

	
}

