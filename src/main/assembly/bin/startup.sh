#!/bin/sh

usage() {
	echo "Usage:"
	echo "    startup.sh [-c config_folder] [-l log_folder] [-d debug mode] [-h]"
	echo "Description:"
	echo "    config_folder - config folder path, not must,if empty,default classpath"
	echo "    log_folder - hsb server's logs base folder, /home/work  log path: /home/work/logs"
	echo "    debug_mode - 1|0  1 means debug port is open, 0 close ,default 0"
	echo "    -h - show this help"
	exit -1
}
LOG_BASE_DIR=""
CONFIG_DIR=""
DEBUG_MODE="0";

while getopts "h:l:c:d:" arg
do
	case $arg in
	    l) LOG_BASE_DIR=$OPTARG;;
		c) CONFIG_DIR=$OPTARG;;
		d) DEBUG_MODE=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

# echo Baidu.com,Inc.
# echo 'Copyright (c) 2000-2013 All Rights Reserved.'
# echo Distributed
# echo https://RabbitConsumerthub.com/brucexx/heisenberg
# echo brucest0078@gmail.com

#check JAVA_HOME & java
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi
if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi
if $noJavaHome ; then
    echo
    echo "Error: JAVA_HOME environment variable is not set."
    echo
    exit 1
fi
#==============================================================================
#set JAVA_OPTS
#JAVA_OPTS="-server -Xmx3g -Xms3g -XX:MaxPermSize=128m "
#JAVA_OPTS="$JAVA_OPTS -XX:NewRatio=1" #  eden/old 的比例
#JAVA_OPTS="$JAVA_OPTS -XX:SurvivorRatio=8"#  s/e的比例
#JAVA_OPTS="$JAVA_OPTS -XX:+UseParallelGC"
#JAVA_OPTS="$JAVA_OPTS -XX:ParallelGCThreads=8"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseParallelOldGC"#  这个是JAVA 6出现的参数选项
#JAVA_OPTS="$JAVA_OPTS -XX:LargePageSizeInBytes=128m"# 内存页的大小， 不可设置过大， 会影响Perm的大小。
#JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"# 原始类型的快速优化
#JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"#  关闭System.gc()
#JAVA_OPTS="-server -Xms6g -Xmx6g -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxPermSize=128m "
#performance Options
#JAVA_OPTS="$JAVA_OPTS -Xss256k"
#JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
#JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
#JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
#JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
#GC Log Options
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
#debug Options
#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8805"
# ZK
#JAVA_OPTS="$JAVA_OPTS -Djava.security.auth.loRabbitConsumern.config=/etc/security/apps_client_jaas.conf"
#agent 目前每个实例需要指定不同端口
#JAVA_OPTS="$JAVA_OPTS -Drmi.agent.port=8233"

JAVA_OPTS="-server -Xmx4000M -Xms4000M -Xmn600M"
JAVA_OPTS="$JAVA_OPTS -XX:PermSize=500M -XX:MaxPermSize=500M -Xss256K"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC -XX:SurvivorRatio=1 -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSClassUnloadingEnabled -XX:LargePageSizeInBytes=128M"
JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=80 -XX:SoftRefLRUPolicyMSPerMB=0"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintClassHistogram -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintHeapAtGC -Xloggc:log/gc.log"
#LOG4J2 ASYNC
JAVA_OPTS="$JAVA_OPTS -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
#JMX REMOTE
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote=true"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=9988"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"

#==============================================================================

#set libjar
libjar="/root/apache-storm-1.0.4/lib/asm-5.0.3.jar:/root/apache-storm-1.0.4/lib/reflectasm-1.10.1.jar:/root/apache-storm-1.0.4/lib/log4j-api-2.8.jar:/root/apache-storm-1.0.4/lib/minlog-1.3.0.jar:/root/apache-storm-1.0.4/lib/servlet-api-2.5.jar:/root/apache-storm-1.0.4/lib/slf4j-api-1.7.21.jar:/root/apache-storm-1.0.4/lib/log4j-over-slf4j-1.6.6.jar:/root/apache-storm-1.0.4/lib/kryo-3.0.3.jar:/root/apache-storm-1.0.4/lib/log4j-core-2.8.jar:/root/apache-storm-1.0.4/lib/objenesis-2.1.jar:/root/apache-storm-1.0.4/lib/disruptor-3.3.2.jar:/root/apache-storm-1.0.4/lib/storm-rename-hack-1.0.4.jar:/root/apache-storm-1.0.4/lib/storm-core-1.0.4.jar:/root/apache-storm-1.0.4/lib/clojure-1.7.0.jar:/root/apache-storm-1.0.4/lib/log4j-slf4j-impl-2.8.jar:/home/dianqu/qrqmweb/lib/gson-2.8.0.jar:/home/dianqu/qrqmweb/lib/qrqmweb-1.0-SNAPSHOT.jar:/root/apache-storm-1.0.4/conf:/root/apache-storm-1.0.4/bin:/home/dianqu/qrqmweb/lib/storm-hdfs-1.0.4.jar:/home/dianqu/qrqmweb/lib/hadoop-hdfs-2.6.1.jar:/home/dianqu/qrqmweb/lib/hadoop-common-2.6.1.jar:/root/kafka_2.10-0.9.0.0/libs/kafka-clients-0.9.0.0.jar:/home/dianqu/qrqmweb/lib/guava-11.0.2.jar:/home/dianqu/qrqmweb/conf/log4j2.xml"

#set HOME
CURR_DIR=`pwd`
cd `dirname "$0"`/..
qrqmweb_HOME=`pwd`
cd $CURR_DIR
if [ -z "$qrqmweb_HOME" ] ; then
    echo
    echo "Error: qrqmweb_HOME environment variable is not defined correctly."
    echo
    exit 1
fi
#==============================================================================

#set CLASSPATH
qrqmweb_CLASSPATH="$qrqmweb_HOME/conf:$qrqmweb_HOME/lib:$CLASSPATH"
#==============================================================================

#startup Server
RUN_CMD="$JAVA_HOME/bin/java -client"
#RUN_CMD="$RUN_CMD -Ddaemon.name= -Dstorm.options="
RUN_CMD="$RUN_CMD -Dstorm.home=/root/apache-storm-1.0.4 -Dstorm.log.dir=/root/apache-storm-1.0.4/logs"
#RUN_CMD="$RUN_CMD -Dqrqmweb_HOME=$qrqmweb_HOME"
RUN_CMD="$RUN_CMD -Djava.library.path=/usr/local/lib:/opt/local/lib:/usr/lib"
RUN_CMD="$RUN_CMD -classpath $libjar"
#RUN_CMD="$RUN_CMD $JAVA_OPTS"
RUN_CMD="$RUN_CMD -Dstorm.jar=/home/dianqu/qrqmweb/lib/qrqmweb-1.0-SNAPSHOT.jar com.idea.main.qrqmweb_ToPoTest"
#RUN_CMD="$RUN_CMD >> \"$qrqmweb_HOME/logs/console.log\" 2>&1 &"
echo $RUN_CMD
eval $RUN_CMD
#==============================================================================