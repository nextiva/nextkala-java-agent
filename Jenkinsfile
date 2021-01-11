@Library('pipeline') _

jobTemplate {

    APP_NAME = 'scheduling'
    DEPLOY_ON_K8S = true
    publishDockerImage = true
    ANSIBLE_DEPLOYMENT = false
    publishBuildArtifact = true
    JDK_VERSION = 'Java 11'
    NODE_LABEL = 'slave_java'

    projectFlow = ['language': 'java',
                   'testCommands': 'mvn clean install package -U']

    kubernetesClusterMap = [dev       : "dev.nextiva.io",
                            qa        : "qa.nextiva.io",
                            production: "prod.nextiva.io"]

    healthCheckMap = [dev       : ["https://scheduling.dev.nextiva.io/health"],
                      qa        : ["https://scheduling.qa.nextiva.io/health"],
                      production: ["https://scheduling.prod.nextiva.io/health"]]

    branchPermissionsMap = [dev       : ["authenticated"],
                            qa        : ["authenticated"],
                            production: ["authenticated"]]
}
