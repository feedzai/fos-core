#!/bin/sh
current=`pwd`
FOS_HOME=${FOS_HOME:-$current/fos-src}
export FOS_HOME

# checkout fos if it does not exist
echo $FOS_HOME
if [ ! -d $FOS_HOME ]
then
    mkdir -p $FOS_HOME
    echo "Cloning repo"
    git clone git@git.feedzai.com:open-source/ml-framework.git $FOS_HOME
fi

cd $FOS_HOME &&

# switch to main development tree
git checkout develop &&

# pull the most recent changes
git pull &&

# create a fresh fos release
mvn clean install -DskipTests=true &&
mvn assembly:assembly -DskipTests=true -f fos-server/pom.xml &&

cd .. &&
mkdir -p fos-runtime &&
tar xvzf $FOS_HOME/fos-server/target/fos-server-bin.tar.gz -C fos-runtime &&
mv fos-runtime/fos-server/* fos-runtime/ &&
rm -rf fos-runtime/fos-server &&
mkdir -p fos-training &&
tar xvzf $FOS_HOME/fos-server/target/fos-server-bin.tar.gz -C fos-training &&
mv fos-training/fos-server/* fos-training/ &&
rm -rf fos-training/fos-server &&
sed -i "s/fos.registryPort=5959/fos.registryPort=5959/g" fos-runtime/conf/fos.properties &&
sed -i "s/fos.scoringPort=5960/fos.scoringPort=5960/g" fos-runtime/conf/fos.properties &&
sed -i "s/fos.registryPort=5959/fos.registryPort=6959/g" fos-training/conf/fos.properties &&
sed -i "s/fos.scoringPort=5960/fos.scoringPort=6960/g" fos-training/conf/fos.properties &&
chmod +x fos-runtime/bin/*.sh &&
chmod +x fos-training/bin/*.sh
