#!/bin/env groovy
def call(params) {
    if(!params) params = [:]
    def appVersion = params.get("version","")

    container('maven') {
        if (appVersion) {
            sh "mvn versions:set -DnewVersion=${appVersion}"
        } else {
            def pom = readMavenPom file: 'pom.xml'
            appVersion = pom.version.split("-")[0] + env.BUILD_NUMBER
            // set environment variables
        }
        env.VERSION = appVersion
        sh "mvn clean install"
        sh "skaffold version"
//        sh "jx step tag --version ${appVersion}"

        sh "skaffold build -f skaffold.yaml"
//        sh "jx step post build --image $DOCKER_REGISTRY/$ORG/$APP_NAME:${appVersion}"
    }
}