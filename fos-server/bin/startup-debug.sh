#!/bin/bash

on_die() {
	tput sgr0
	exit 0
}

trap 'on_die' TERM INT EXIT KILL


TARGET_FILE=$0

cd `dirname $TARGET_FILE`
TARGET_FILE=`basename $TARGET_FILE`

while [ -L "$TARGET_FILE" ]
do
    TARGET_FILE=`readlink $TARGET_FILE`
    cd `dirname $TARGET_FILE`
    TARGET_FILE=`basename $TARGET_FILE`
done

PHYS_DIR=`pwd -P`
RESULT=$PHYS_DIR/$TARGET_FILE
echo $RESULT

cd ${RESULT%/*/*}

JAVA="java"
CLASSPATH="."
MAIN_CLASS="com.feedzai.fos.server.Runner"
LOGGER_CONFIG_FILE="-Dlogback.configurationFile=conf/logger.xml"
JVM_OPTS="-Xms1024m -Xmx2048m"
JVM_DEBUG="-Dcom.sun.management.jmxremote -Xdebug -agentlib:jdwp=transport=dt_socket,suspend=y,server=y,address=9999,suspend=n"



for i in lib/*.jar
do
	CLASSPATH=$CLASSPATH:$i
done

$JAVA $JVM_OPTS $JVM_DEBUG -cp $CLASSPATH $LOGGER_CONFIG_FILE $MAIN_CLASS
