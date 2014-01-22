#!/bin/sh
#
# FOS Server
#
# chkconfig:   2345 99 1
# description: FOS Server
#              

### BEGIN INIT INFO
# Provides: 
# Required-Start: 
# Required-Stop: 
# Should-Start: 
# Should-Stop: 
# Default-Start: 
# Default-Stop: 
# Short-Description: 
# Description:      
### END INIT INFO

# Source function library.
. /etc/rc.d/init.d/functions

PID_FILE=/var/run/java/fos.pid
exec="/opt/fos/bin/startup.sh -d -p ${PID_FILE}"
prog="fos"

[ -e /etc/sysconfig/$prog ] && . /etc/sysconfig/$prog

lockfile=/var/lock/subsys/$prog

start() {
    [ -e ${PID_FILE} ] && (ps -p `cat ${PID_FILE}` &>/dev/null || exit 5)
    echo -n $"Starting $prog: "
    # if not running, start it up here, usually something like "daemon $exec"
    daemon $exec
    retval=$?
    [ $retval -eq 0 ] && touch $lockfile
    return $retval
}

stop() {
    echo -n $"Stopping $prog: "
    # stop it here, often "killproc $prog"
    kill `cat ${PID_FILE}`

    while [ 1 ]
    do
      ps -p `cat ${PID_FILE}` &> /dev/null
      if [ $? != 0 ]
      then
        break
      else
        sleep 2s
        printf "."
      fi
    done
    echo ""

#    retval=$?
    retval=0
    [ $retval -eq 0 ] && rm -f $lockfile
    return $retval
}


restart() {
    stop
    start
}

reload() {
    restart
}

force_reload() {
    restart
}

rh_status() {
    # run checks to determine if the service is running or use generic status
    ps -p `cat ${PID_FILE}`
}

rh_status_q() {
    rh_status >/dev/null 2>&1
}


case "$1" in
    start)
        rh_status_q && exit 0
        $1
        ;;
    stop)
        rh_status_q || exit 0
        $1
        ;;
    restart)
        $1
        ;;
    reload)
        rh_status_q || exit 7
        $1
        ;;
    force-reload)
        force_reload
        ;;
    status)
        rh_status
        ;;
    condrestart|try-restart)
        rh_status_q || exit 0
        restart
        ;;
    *)
        echo $"Usage: $0 {start|stop|status|restart|condrestart|try-restart|reload|force-reload}"
        exit 2
esac
exit $?
