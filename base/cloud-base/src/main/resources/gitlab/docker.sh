pwd
SERVER_NAME=gitlab
cd blog/blog-server || exit
# 容器ID
CID=$(docker ps | grep "$SERVER_NAME" | awk '{print $1}')
if [ -n "$CID" ]; then
    echo "存在镜像在运行中，停止正在运行的镜像$CID"
    docker stop "$CID"
fi
IID=$(docker images | grep "$SERVER_NAME" | awk '{print $3}')
if [ -n "$IID" ]; then
    echo "存在$SERVER_NAME镜像，IID=$IID"
    echo "移除旧的镜像"
    docker rmi -f $SERVER_NAME
fi
echo "构建新的镜像$SERVER_NAME"
docker build -t $SERVER_NAME .
echo "所有的镜像"
docker images
echo "运行docker容器"
docker rm $SERVER_NAME
docker run --name $SERVER_NAME --net host -v $LOG_PATH:$LOG_PATH -d -p 10003:10003 $SERVER_NAME

running=$(docker inspect --format '{{.State.Running}}' $SERVER_NAME)
echo "$SERVER_NAME state $Running"
while [ "$running" == 'false' ]; do
    echo -e ".\c"
    sleep 1
    running=$(docker inspect --format '{{.State.Running}}' $SERVER_NAME)
done

echo "容器已启动!"
CID=$(docker ps | grep "$SERVER_NAME" | awk '{print $1}')
echo "容器ID: $CID"
