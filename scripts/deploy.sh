#!/bin/bash
# /home/ubuntu/build/build/libs/ 디렉토리에서 .jar 확장자를 가진 파일들을 모두 나열하고 해당 리스트를 BUILD_JAR 변수에 저장합니다.
BUILD_JAR=$(ls /home/ubuntu/build/build/libs/*.jar)
# BUILD_JAR 변수에 저장된 파일 경로에서 파일명만 추출하여 JAR_NAME 변수에 저장합니다.
JAR_NAME=$(basename $BUILD_JAR)
# $JAR_NAME 변수에 저장된 파일명을 /home/ubuntu/deploy.log 파일에 기록합니다
echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

echo "> build 파일 복사" >> /home/ubuntu/deploy.log
DEPLOY_PATH=/home/ubuntu/build
# Jar 파일을 $DEPLOY_PATH에 복사
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행중인 프로세스가 있는지 검사하고 없으면 then, 있으면 else로 넘어감
if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/deploy.log
else
  # 실행 중인 프로세스가 있으면 프로세스를 종료시킴
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH/$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/deploy.log
# 서버를 백그라운드에서 실행하고, 정상적으로 동작하는 경우, deploy.log에 로깅하고 에러가 있는 경우 deploy_err.log에 로깅함.
nohup java -jar $DEPLOY_JAR >> /home/ubuntu/deploy.log 2>/home/ubuntu/deploy_err.log &