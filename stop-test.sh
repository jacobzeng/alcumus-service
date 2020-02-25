#/bin/sh
RUNTIME_FOLDER=/root/alcumus-service
cd $RUNTIME_FOLDER
FILE=./service.pid
if [ -f "$FILE" ];
then
read pid < "$FILE"
kill "$pid"
rm "$FILE"
fi
sleep 5