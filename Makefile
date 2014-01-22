.PHONY=all,compile
MAVEN_OPTS=-XX:MaxPermSize=128m -Xms1024m -Xmx2048m -XX:+UseConcMarkSweepGC

compile:
	mvn clean install -DskipTests=true

test:
	mvn test

all:
	mvn clean install -DskipTests=true

package:
	mvn assembly:assembly -DskipTests -f fos-server/pom.xml