#!/bin/bash

help() {
  printf "Usage: %s: [-d] [-p pidfile] [-l logfile] [-h]\n" $(basename $0) >&2
  printf "Options\n"  >&2
  printf "  -d\trun in daemon mode\n"  >&2
  printf "  -p\twrite pid to file\n"  >&2
  printf "  -l\tspecifies the log file. Default is /var/log/fos. Use only with -d option.\n"  >&2
  printf "  -h\tprint this help\n"  >&2
}

d=
p=
l=
lval=/var/log/fos
while getopts 'dp:l:h' OPTION
do
  case $OPTION in
  d)
    d=1
  ;;
  
  p)
    p=1
    pval="$OPTARG"
  ;;

  l)
    l=1
    lval="$OPTARG"
  ;;

  h)
    help
  ;;

  ?)
    help
    exit 2
  ;;
  esac
done
shift $(($OPTIND - 1))


on_die() {
  if [ ! "$d" ] &&  ps -p $PID > /dev/null
  then
    kill -TERM $PID
    wait $PID
  fi

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

cd ${RESULT%/*/*}

JAVA="java"
CLASSPATH="."
MAIN_CLASS="com.feedzai.fos.server.Runner"
LOGGER_CONFIG_FILE="-Dlogback.configurationFile=conf/logger.xml"
JVM_OPTS="-Xms1024m -Xmx2048m"

for i in lib/*.jar
do
  CLASSPATH=$CLASSPATH:$i
done

if [ "$d" ]
then
  $JAVA $JVM_OPTS -cp $CLASSPATH $LOGGER_CONFIG_FILE $MAIN_CLASS &> ${lval} &
else
  $JAVA $JVM_OPTS -cp $CLASSPATH $LOGGER_CONFIG_FILE $MAIN_CLASS &
fi

export PID=$!

if [ "$p" ]
then
        mkdir -p ${pval%/*} 
        echo $PID > $pval
fi

if [ "$d" ]
then
  exit 0
else
  wait $PID
fi
