#!/bin/bash
pwd
name=gateway.jar
cd gateway/target || exit
cp ./gateway.jar /data/gateway/gateway-new.jar

pid=$(ps -ef | grep gateway.jar | grep -v grep | awk '{print $2}')
echo "$pid"
if [ -n "$pid" ]
then
  kill -9 "$pid"
fi
cd /data/gateway || exit
if [ -f "$name" ];
then
  echo "bak old gateway.jar"
	mv gateway.jar gateway.jar.bak
fi
mv gateway-new.jar gateway.jar
nohup java -jar gateway.jar -Xmx256m -Xms256m & > /dev/null &
ehco "启动成功"