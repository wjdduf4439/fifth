FROM  ubuntu:18.04

# root 권한으로 실행되는 것을 보장
USER root

#실질적으로 mysql-client를 설치하는 부분
#RUN apt-get update && apt-get install -y mysql-client
#RUN yum -y update && yum -y install mysql

##
## mysql-client : mysql 연결 프로그램
## iputils-ping : container에서 host로의 연결이 정상적으로 이루어지는지 확인하기

RUN apt-get update && \
	apt-get install -y openjdk-17-jdk mysql-client iputils-ping && \
	apt-get clean

# 환경 변수 설정
# 환경 변수 식별자, 이 변수가 제대로 출력되면 해당 docker 컨테이너가 정상적으로 실행된 것임
ENV ENVPOINT=DOCKER_IN
#db 비밀번호
ENV DBPASSWORD=dl!wjd2duf
#스토리지 경로
ENV STORAGEPATH=/home/fifth_storage/
	
# 작업 디렉토리 생성 및 권한 설정
# 예: 기본적으로 사용할 디렉토리를 미리 생성하고 권한을 설정
RUN mkdir -p $STORAGEPATH && \
    chmod -R 777 $STORAGEPATH
    
# Add a volume pointing to /tmp
VOLUME /tmp

# 외부에서 컨테이너의 8080 포트를 사용할 수 있도록 설정
EXPOSE 8080 

# The application's jar file
# 실제 파일을 빌드하기로 하는 위치
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-jar","/app.jar"]