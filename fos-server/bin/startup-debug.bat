@echo off
setLocal EnableDelayedExpansion

IF NOT DEFINED JAVA_HOME goto :javaerror

rem Project properties.
set JAVA="%JAVA_HOME%\bin\java.exe"
set CLASSPATH=".;.\lib\*"
set MAIN_CLASS=com.feedzai.fos.server.Runner
set LOGGER_CONFIG_FILE=-Dlogback.configurationFile=conf\logger.xml
set JVM_OPTS=-Xms1024m -Xmx2048m
set JVM_DEBUG=-Dcom.sun.management.jmxremote -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,address=9999,suspend=n
set CONFIG=-c conf\fos.properties

set fos_home="%~dp0"

cd %fos_home%
cd ..

rem Start Fos.
!JAVA! !JVM_OPTS! !JVM_DEBUG! -cp !CLASSPATH! !LOGGER_CONFIG_FILE! !MAIN_CLASS! !CONFIG!

goto :eof

:javaerror
echo ============== ERROR ======================
echo =                                         =
echo = Environment variable JAVA_HOME not set. =
echo = Please define it and re run the script. =
echo =                                         =
echo ===========================================