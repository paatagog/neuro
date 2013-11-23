@echo off
cd "C:\dev\neuro\sources\neuro-console\"

call mvn dependency:copy-dependencies -o
call java -Xmx512m -classpath "./client/*;./target/dependency/*;C:\dev\neuro\sources\neuro-console\src\main\java;C:\dev\neuro\sources\neuro-console\src\main\resources;C:\dev\neuro\sources\neuro-console\target\neuro-console-0.1.war\WEB-INF\classes;C:\dev\neuro\sources\neuro-console\target\generated-sources\gwt;" com.google.gwt.dev.DevMode -war C:\dev\neuro\sources\neuro-console\target\neuro-console-0.1 -gen C:\dev\neuro\sources\neuro-console\target\.generated -logLevel INFO -port 8888 -startupUrl gxt/Application.html ge.amigo.neuro.console.application
