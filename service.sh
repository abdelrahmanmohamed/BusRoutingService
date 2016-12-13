#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Keep the pwd in mind!
# Example: RUN="java -jar $DIR/target/magic.jar"
RUN="java -jar $DIR/target/busroutingservice-1.0-SNAPSHOT-allinone.jar"
NAME=busroutingservice

DATA_FILE=$2

PIDFILE=/tmp/$NAME.pid
LOGFILE=/tmp/$NAME.log

start() {
    if [ -f $PIDFILE ]; then
        if kill -0 $(cat $PIDFILE); then
            echo 'Service already running' >&2
            return 1
        else
            rm -f $PIDFILE
        fi
    fi
    local CMD="$RUN $DATA_FILE > \"$LOGFILE\" &"
    sh -c "$CMD"
    sh -c "(pgrep -f busroutingservice-1.0-SNAPSHOT-allinone.jar | head -1)" &> $PIDFILE
    echo "starting service"
    while true;do
    wget -qO- http://localhost:8088/heartbeat  &> /dev/null && break
    done
    echo started
}

stop() {
    if [ ! -f $PIDFILE ] || ! kill -0 $(cat $PIDFILE); then
        echo 'Service not running' >&2
        return 1
    fi
    wget -qO- http://localhost:8088/stop  &> /dev/null
    rm -f $PIDFILE  &> /dev/null
    echo "stopped"
}


case $1 in
    start)
        start
        ;;
    stop)
        stop
        ;;
    block)
        start
        sleep infinity
        ;;
    *)
        echo "Usage: $0 {start|stop|block} DATA_FILE"
esac