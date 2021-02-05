/*
*  通用jenkins自动构建pipline文件
* 参数说明:
* GIT_URL:  仓库地址
* GIT_BRANCH: 分支名称
* IS_RUN_SONNAR: 是否执行ssonar扫描
*
*
*/

// 生命全局工具变量
def GLOBAL_TOOL_JDK_ID   = 'JAVA_HOME'
def GLOBAL_TOOL_MAVEN_ID = 'MAVEN_HOME'

node {
    
    try{
    // 全局获取最新代码
    stage("获取最新代码") {
        // 限制15分钟内完成。
        timeout(time: 15, unit: 'MINUTES') {
             coCode()
        }
    }

    // 组件编译测试
    stage("SCA编译&&测试") {
        execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "clean compile -Dmaven.test.skip=true")
    }
    
    // sonar
   stage("sonar代码扫描") {
         timeout(time: 15, unit: 'MINUTES') {
	   if(params.IS_RUN_SONNAR) {
		execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=http://192.168.96.89:9000 -X")
	    }
         }
    }

    stage("清理工程") {
        // 暂时不删除目录
        deleteDir()
        // 查看当前目录
        // sh 'ls -lah'
    }
    }  finally {
       /*stage('Send email') {
                 emailext body : '${FILE,path="${JENKINS_HOME}/email-templates/email.html"}',
                 mimeType: 'text/html',
                 to:'${MAIL_TO_USER}',
                subject: '构建通知：$PROJECT_NAME - Build # $BUILD_NUMBER - Success!'
        }*/
        
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