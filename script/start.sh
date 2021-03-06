#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)

ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh



REPOSITORY=/opt/app/build/libs
PROJECT_NAME=demo

echo "> Build 파일 복사"
echo "> $REPOSITORY/*.jar $REPOSITORY"

cp $REPOSITORY/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

#nohup java -jar $REPOSITORY > /dev/null 2> /dev/null < /dev/null &
#nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
nohup java -jar \
    -Dspring.config.location=classpath:/application.yml,classpath:/application-$IDLE_PROFILE.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
#    -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &