/*
*  ͨ��jenkins�Զ�����pipline�ļ�
* ����˵��:
* GIT_URL:  �ֿ��ַ
* GIT_BRANCH: ��֧����
* GIT_CREDIT�� jenkinsƾ�ݲ���
* IS_RUN_SONNAR: �Ƿ�ִ��ssonarɨ��
* IS_GEN_DOCKER_IMG�� �Ƿ�ִ��docker����
* MODEL_NAMES ����ģ������
* IS_SEND_EMAIL: �Ƿ����ʼ�
*/

// ����ȫ�ֹ��߱���
def GLOBAL_TOOL_JDK_ID   = 'JAVA_HOME'
def GLOBAL_TOOL_MAVEN_ID = 'MAVEN_HOME'

// def modelNames = param.MODEL_NAMES.replaceAll('"','').split(',')

node {
    
    try{
    // ȫ�ֻ�ȡ���´���
    stage("��ȡ���´���") {
        // ����15��������ɡ�
        timeout(time: 15, unit: 'MINUTES') {
             coCode()
        }
    }

    // ����������
    stage("SCA����&&����") {
        execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "clean compile -Dmaven.test.skip=true")
    }
    
	stage("����sonar�����ļ�"){
            
		dir("${WORKSPACE}"){
			try{
				sh "rm sonar-project.properties"
			}catch(e){}
				sh "echo 'sonar.projectKey=param.MODEL_NAMES' >> sonar-project.properties"
				sh "echo 'sonar.projectName=param.MODEL_NAMES' >> sonar-project.properties"
				sh "echo 'sonar.sourceEncoding=UTF-8' >> sonar-project.properties"
				sh "echo 'sonar.modules=java-module,html-module' >> sonar-project.properties"
				
				sh "echo 'java-module.sonar.projectName=Java Module' >> sonar-project.properties"
				sh "echo 'java-module.sonar.language=java' >> sonar-project.properties"
				sh "echo 'java-module.sonar.sources=.' >> sonar-project.properties"
				sh "echo 'java-module.sonar.projectBaseDir=src/main/java' >> sonar-project.properties"
				sh "echo 'sonar.binaries=classes' >> sonar-project.properties"
								
				sh "echo 'html-module.sonar.projectName=Html Module ' >> sonar-project.properties"
				sh "echo 'html-module.sonar.language=web ' >> sonar-project.properties"
				sh "echo 'html-module.sonar.sources=.' >> sonar-project.properties"
				sh "echo 'html-module.sonar.projectBaseDir=src/main/resources/resources' >> sonar-project.properties"
				
				
		}
		           
	}

    // sonar
   stage("sonar����ɨ��") {
         timeout(time: 15, unit: 'MINUTES') {
	   if(params.IS_RUN_SONNAR) {
		 execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=${SONAR_HOST} -X")
	    }
         }
    }

	stage("SCA��������") {
		if(params.IS_GEN_DOCKER_IMG) {
			parallel '��������': {
					// �������
					execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "/", "clean package -Dmaven.test.skip=true  -X ")
					buildAndPushImage(param.MODEL_NAMES)
				 }
					
		}
    }
	
    stage("������") {
        // ��ʱ��ɾ��Ŀ¼
        deleteDir()
        // �鿴��ǰĿ¼
        // sh 'ls -lah'
    }
    }  finally {
		if(params.IS_SEND_EMAIL) {
		stage('Send email') {
                 emailext body : '${FILE,path="${JENKINS_HOME}/email-templates/email.html"}',
                 mimeType: 'text/html',
                 to:'${MAIL_TO_USER}',
                subject: '����֪ͨ��$PROJECT_NAME - Build # $BUILD_NUMBER - Success!'
        }
		
		}
        
        }

}

// ִ��maven�����Ҫ����·����maven�Ĺ���Ŀ¼��maven�Ĳ������
def execMavenCommand(mvnTools, path, params) {
    // ��ʼ��
    init(mvnTools);
    // ��ȡ��ǰ·��
   def curPath = pwd();
   // �޶�15���������
    timeout(time: 15, unit: 'MINUTES') {
        sh "cd ${curPath}${path} && mvn ${params} -V -B -Duser.timezone=GMT+08"
    }
}

// ��ʼ����Ϊ�˸��ü򻯸���node�Ļ�����ʼ��������ٴβ���ͳһ��ʽ���г�ʼ����
def init(mvnTools) {
    // ������
    // coCode();
    // ���maven�����������С�
    addMvnEnv(mvnTools);
}

// ���maven������������
def addMvnEnv(t) {
    // ����maven���
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
    // ��������Ŀ¼���������ļ���
    sh "mkdir -p ${buildPath} && cp ${workPath}/src/main/docker/Dockerfile ${buildPath}/ && cp ${workPath}/target/*.jar ${buildPath}/"

    // ����ƾ֤����ƾ֤����jenkins
    def docker_cer = 'docker-hub-10-1-245-53'

    // ��ȡ�汾����
    def buildVersion = versionOfProject(workPath)
    println("buildVersion:" + buildVersion)

    def artifactId = artifactIdProject(workPath)
    println("artifactId:" + artifactId)

    // ���ϱ����ʱ�����ͬʱȥ��-��_��
    // buildVersion = buildVersion + "." + currentBuild.timeInMillis
    // buildVersion = buildVersion.replace('-','')
    // buildVersion = buildVersion.replace('_','')

    def output = new Date().format('yyyyMMddHHmmss')

    buildVersion = buildVersion + "." + output

    docker.withServer(env.DOCKER_REGISTRY_SERVER_TCP) {
        def app = docker.build(artifactId + ":" + buildVersion, buildPath)

        docker.withRegistry(env.DOCKER_REGISTRY_SERVER_IP,'admin123') {
            // �������°汾
            app.push("latest")
            // ���͵�ǰ�汾
            app.push(buildVersion);
        }
    }
}

// ��pom�ļ��й����ȡ�汾��
def versionOfProject(path) {
    def matcher = readFile(path + '/pom.xml') =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}

// ��pom�ļ��л�ȡID
def artifactIdProject(path) {
    def matcher = readFile(path + '/pom.xml') =~ '<artifactId>(.+)</artifactId>'
    matcher ? matcher[0][1] : null
}