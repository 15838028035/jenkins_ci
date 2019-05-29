// ����ȫ�ֹ��߱���
def GLOBAL_TOOL_JDK_ID   = 'JAVA_HOME'
def GLOBAL_TOOL_MAVEN_ID = 'MAVEN_HOME'

properties([parameters([string(name: 'GIT_URL', defaultValue: 'http://192.168.96.89:3000/Model_Training/Audio_Model.git', description: 'git�ֿ��ַ'),
                        string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'git��֧����'),
                        string(name: 'GIT_CREDIT', defaultValue: '47e7e050-aa0c-42cd-a1af-500a3f07d5c1', description: 'git�û�ƾ֤')

            ])])

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
    
    // sonar
   stage("sonar����ɨ��") {
         timeout(time: 15, unit: 'MINUTES') {
            execMavenCommand(GLOBAL_TOOL_MAVEN_ID, "", "sonar:sonar -Dsonar.host.url=http://192.168.96.89:9000 -X")
         }
    }

    stage("������") {
        // ��ʱ��ɾ��Ŀ¼
        deleteDir()
        // �鿴��ǰĿ¼
        // sh 'ls -lah'
    }
    }  finally {
       /*stage('Send email') {
                 emailext body : '${FILE,path="${JENKINS_HOME}/email-templates/email.html"}',
                 mimeType: 'text/html',
                 to:'${MAIL_TO_USER}',
                subject: '����֪ͨ��$PROJECT_NAME - Build # $BUILD_NUMBER - Success!'
        }*/
        
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