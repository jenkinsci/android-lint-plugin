# Android Lint Plugin for Jenkins

Parses output from the Android lint tool and displays the results for analysis.

https://wiki.jenkins-ci.org/display/JENKINS/Android+Lint+Plugin

## Testing
To build and test this plugin, use `mvn -DskipTests clean package hpi:run`.

This is required rather than just `mvn clean hpi:run` due to an issue with version 2.0 of maven-hpi-plugin;
see https://github.com/jenkinsci/maven-hpi-plugin/pull/63.