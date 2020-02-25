#/bin/sh
RUNTIME_FOLDER=/root/alcumus-service
cd $RUNTIME_FOLDER
#nohup java -jar $JAR_FILE_NAME --spring.profiles.active=$SPRING_ACTIVE_PROFILE &
JAR_FILE_NAME=build/libs/alcumus-service-1.0.0.jar
nohup java -jar $JAR_FILE_NAME --spring.profiles.active=test &
echo $! >> ./service.pid
sleep 1
#java -jar $JAR_FILE_NAME --spring.profiles.active=$SPRING_ACTIVE_PROFILE