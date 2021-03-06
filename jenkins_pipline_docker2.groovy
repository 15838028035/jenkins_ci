/*
*  通用jenkins自动构建pipline文件
* 参数说明:
* IS_VIEW_HELP:是否查看使用帮助介绍
* GIT_URL:  仓库地址
* GIT_BRANCH: 分支名称
* GIT_CREDIT： jenkins凭据参数
* IS_RUN_SONAR: 是否执行sonar扫描
* IS_RUN_SONAR_HTML: 是否执行sonar html扫描
* IS_GEN_DOCKER_IMG： 是否执行docker镜像
* IS_DEPLOY_NEXUS 是否发布到nexus中
* MODEL_NAMES 镜像模块名称
* IS_SEND_EMAIL: 是否发送邮件通知
* IS_MAVEN_DEBUG: 是否启用maven调试，默认值false
*/

// 生命全局工具变量
def GLOBAL_TOOL_JDK_ID   = 'JAVA_HOME'
def GLOBAL_TOOL_MAVEN_ID = 'MAVEN_HOME'

// def modelNames = param.MODEL_NAMES.replaceAll('"','').split(',')

node {
    
    try{
	stage("使用帮助介绍") {
		 if(params.IS_VIEW_HELP) {
		   echo "=======================使用帮助开始================="
		   echo "请在jenkins上建立pipeline流水线，设置参数化请求参数"
		   echo "是否查看使用帮助介绍：IS_VIEW_HELP,boolean类型"
		   echo "仓库地址：GIT_URL,字符串类型"
		   echo "分支名称：GIT_BRANCH,字符串类型"
		   echo "jenkins凭据参数：GIT_CREDIT,字符串类型"
		   echo "是否执行sonar扫描：IS_RUN_SONAR,boolean类型"
		   echo "是否执行sonar html扫描：IS_RUN_SONAR_HTML,boolean类型"
		   echo "是否执行docker镜像：IS_GEN_DOCKER_IMG,boolean类型"
		   echo "是否发布到nexus中：IS_DEPLOY_NEXUS,boolean类型"
		   echo "镜像模块名称：MODEL_NAMES,字符串类型"
		   echo "是否发送邮件通知：IS_SEND_EMAIL,boolean类型"
		   echo "是否启用maven调试:IS_MAVEN_DEBUG,boolean类型"
		   echo "=======================使用帮助结束================="
	   }
	   
    }
	
	 stage("打印请求参数") {
	   echo "请求参数信息显示如下:"
       echo "仓库地址GIT_URL:" +params.GIT_URL
	   echo "分支名称GIT_BRANCH:" +params.GIT_BRANCH
	   echo "jenkins凭据参数GIT_CREDIT:" +params.GIT_CREDIT
	   echo "是否执行sonar扫描IS_RUN_SONAR:" +params.IS_RUN_SONAR
	   echo "是否执行sonar html扫描IS_RUN_SONAR_HTML:" +params.IS_RUN_SONAR_HTML
	   echo "是否执行docker镜像IS_GEN_DOCKER_IMG:" +params.IS_GEN_DOCKER_IMG
	   echo "是否发布到nexus中IS_DEPLOY_NEXUS:" +params.IS_DEPLOY_NEXUS
	   echo "镜像模块名称MODEL_NAMES:" +params.MODEL_NAMES
	   echo "是否发送邮件通知IS_SEND_EMAIL:" +params.IS_SEND_EMAIL
	   echo "是否启用maven调试，默认值false，IS_MAVEN_DEBUG:" +params.IS_MAVEN_DEBUG
	   
    }
	
    // 全局获取最新代码
    stage("获取最新代码") {
        // 限制15分钟内完成。
        timeout(time: 15, unit: 'MINUTES') {
             coCode()
        }
    }

    // 组件编译测试
    stage("SCA编译&&测试") {
	 if(params.IS_MAVEN_DEBUG) {
        execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "clean compile -Dmaven.test.skip=true -X")
	   }else {
	   execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "clean compile -Dmaven.test.skip=true")
	   }
    }
    
	stage("配置sonar配置文件"){
        if(params.IS_RUN_SONAR) {    
			dir("${WORKSPACE}"){
				try{
					sh "rm sonar-project.properties"
				}catch(e){}
					sh "echo 'sonar.projectKey=param.MODEL_NAMES' >> sonar-project.properties"
					sh "echo 'sonar.projectName=param.MODEL_NAMES' >> sonar-project.properties"
					sh "echo 'sonar.sourceEncoding=UTF-8' >> sonar-project.properties"
										
					if(params.IS_RUN_SONAR_HTML) {
						sh "echo 'sonar.modules=java-module,html-module' >> sonar-project.properties"
					}else {
						sh "echo 'sonar.modules=java-module' >> sonar-project.properties"
					}
					
					sh "echo 'java-module.sonar.projectName=Java Module' >> sonar-project.properties"
					sh "echo 'java-module.sonar.language=java' >> sonar-project.properties"
					sh "echo 'java-module.sonar.sources=.' >> sonar-project.properties"
					sh "echo 'java-module.sonar.projectBaseDir=src/main/java' >> sonar-project.properties"
					sh "echo 'sonar.binaries=classes' >> sonar-project.properties"
					
					if(params.IS_RUN_SONAR_HTML) {					
						sh "echo 'html-module.sonar.projectName=Html Module ' >> sonar-project.properties"
						sh "echo 'html-module.sonar.language=web ' >> sonar-project.properties"
						sh "echo 'html-module.sonar.sources=.' >> sonar-project.properties"
						sh "echo 'html-module.sonar.projectBaseDir=src/main/resources/static' >> sonar-project.properties"
					}
					
					
			}
		}
		           
	}

    // sonar
   stage("sonar代码扫描") {
         timeout(time: 15, unit: 'MINUTES') {
		   if(params.IS_RUN_SONAR) {
				if(params.IS_MAVEN_DEBUG) {
					if(params.IS_RUN_SONAR_HTML) {	
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=${SONAR_HOST} -Dsonar.sources=. -X")
					}else {
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=${SONAR_HOST} -X")
					}
				 }else {
					
					if(params.IS_RUN_SONAR_HTML) {	
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=${SONAR_HOST} -Dsonar.sources=. ")
					}else {
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=${SONAR_HOST}  ")
					}
				}
			 }
		 }
    }

	stage("docker镜像生成") {
		if(params.IS_GEN_DOCKER_IMG) {
			parallel '镜像生成': {
					// 打包代码
					if(params.IS_MAVEN_DEBUG) {
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "/", "clean package -Dmaven.test.skip=true -P profile2  -X ")
					}else {
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "/", "clean package -Dmaven.test.skip=true -P profile2  ")
					}
					buildAndPushImage(params.MODEL_NAMES)
				 }
					
		}
    }
    
      // 发布
    stage("发布到nexus中") {
        	if(params.IS_DEPLOY_NEXUS) {
                 timeout(time: 15, unit: 'MINUTES') {
					 if(params.IS_MAVEN_DEBUG) {
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "deploy -Dmaven.test.skip=true -Dgpg.passphrase=${gpg_passphrase} -P release -Duser.timezone=GMT+08 -X")
					 }else{
						execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "deploy -Dmaven.test.skip=true -Dgpg.passphrase=${gpg_passphrase} -P release -Duser.timezone=GMT+08 ")
					 }
				}
        	}
    }
	
    stage("清理工程") {
        // 暂时不删除目录
        deleteDir()
        // 查看当前目录
        // sh 'ls -lah'
			cleanWs();
    }
    }  finally {
		if(params.IS_SEND_EMAIL) {
		stage('Send email') {
                 emailext body : '${FILE,path="${JENKINS_HOME}/email-templates/email.html"}',
                 mimeType: 'text/html',
                 to:'${MAIL_TO_USER}',
                subject: '构建通知：：$PROJECT_NAME - Build # $BUILD_NUMBER - Success!'
        }
		
		}
        
        }

}

// 执行maven命令，需要配置路径，maven的工作目录，maven的参数命令。
def execMavenCommand(mvnTools, path, params) {
    // 初始化
    init(mvnTools);
    // 获取当前路径
   def curPath = pwd();
   // 限定15分钟内完成
    timeout(time: 15, unit: 'MINUTES') {
        sh "cd ${curPath}${path} && mvn ${params} -V -B -Duser.timezone=GMT+08"
    }
}

// 初始化，为了更好简化各个node的环境初始化，因此再次采用统一方式进行初始化。
def init(mvnTools) {
    // 检测代码
    // coCode();
    // 添加maven到环境变量中。
    addMvnEnv(mvnTools);
}

// 添加maven到环境变量中
def addMvnEnv(t) {
    // 定义maven组件
    def mvnHome = tool t
    env.PATH = "${mvnHome}/bin:${env.PATH}"
}

def coCode() {

            
   checkout([
       $class: 'GitSCM', 
       branches: [[name: 'refs/heads/'+params.GIT_BRANCH]],
       doGenerateSubmoduleConfigurations: false, 
       extensions: [], 
       submoduleCfg: [], 
       userRemoteConfigs: [[credentialsId: params.GIT_CREDIT, 
       url: params.GIT_URL
       ]
       ]]
       )
}

def buildAndPushImage(moduleHome) {

    def curPath = pwd()
    def workPath = curPath
    println "build docker workspace:" + workPath

    def buildPath = workPath + "/target/docker";
    // 创建构建目录，并拷贝文件。
    sh "mkdir -p ${buildPath} && mkdir -p ${buildPath}/config &&  mkdir -p ${buildPath}/lib  && cp ${workPath}/src/main/docker/Dockerfile ${buildPath}/ && cp ${workPath}/target/*.jar ${buildPath}   && cp ${workPath}/target/classes/application*.* ${buildPath}/config"

    // 声明凭证，该凭证来自jenkins
    def docker_cer = 'docker-hub-10-1-245-53'

    // 读取版本号码
    def buildVersion = versionOfProject(workPath)
    println("buildVersion:" + buildVersion)

    def artifactId = artifactIdProject(workPath)
    println("artifactId:" + artifactId)

    // 加上编译的时间戳，同时去掉-和_。
    // buildVersion = buildVersion + "." + currentBuild.timeInMillis
    // buildVersion = buildVersion.replace('-','')
    // buildVersion = buildVersion.replace('_','')

    def output = new Date().format('yyyyMMddHHmmss')

    buildVersion = buildVersion + "." + output

    docker.withServer(env.DOCKER_REGISTRY_SERVER_TCP) {
        def app = docker.build(artifactId + ":" + buildVersion, buildPath)

        docker.withRegistry(env.DOCKER_REGISTRY_SERVER_IP,'admin123') {
            // 推送最新版本
            app.push("latest")
            // 推送当前版本
            app.push(buildVersion);
        }
    }
}

// 从pom文件中国年获取版本号
def versionOfProject(path) {
    def matcher = readFile(path + '/pom.xml') =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}

// 从pom文件中获取ID
def artifactIdProject(path) {
    def matcher = readFile(path + '/pom.xml') =~ '<artifactId>(.+)</artifactId>'
    matcher ? matcher[0][1] : null
}