#!/bin/bash

BUILD_DIR=fos-server-$(git rev-parse HEAD | cut -b 1-7)

# build
# mvn clean install -DskipTests=true

# recreate build directory
rm -rf $BUILD_DIR $BUILD_DIR.tar.bz2
mkdir $BUILD_DIR
mkdir $BUILD_DIR/conf
mkdir $BUILD_DIR/lib
mkdir $BUILD_DIR/models

# copy files
cp fos-server/target/fos-server-*.jar $BUILD_DIR/lib
cp fos-server/src/main/resources/ClassificationServerParameters.properties $BUILD_DIR/conf/fos.properties
cp fos-impl-weka/src/test/resources/models/threadunsafe/test.header $BUILD_DIR/models
cp fos-impl-weka/src/test/resources/models/test.model $BUILD_DIR/models

# edit files
sed -i 's|src/test/resources/||g' $BUILD_DIR/conf/fos.properties 
sed -i 's|target/test-classes/||g' $BUILD_DIR/models/test.header

# create startup scripts
cat > $BUILD_DIR/run_scoring.sh << EOF
#!/bin/bash
java -jar lib/*.jar --config conf/fos.properties --embeddedRegistry -p 6969
EOF

cat > $BUILD_DIR/run_training.sh << EOF
#!/bin/bash
java -jar lib/*.jar --config conf/fos.properties --embeddedRegistry -p 5959
EOF

cat > $BUILD_DIR/run.sh << EOF
#!/bin/bash
type parallel >/dev/null 2>&1 || { echo >&2 "I require GNU parallel but it's not installed. Aborting."; exit 1; }
find . -name "run_*.sh" | parallel -u
EOF

chmod +x $BUILD_DIR/*.sh

# create package
tar cjf $BUILD_DIR.tar.bz2 $BUILD_DIR

# done
echo "[FOS Package]"
find $BUILD_DIR
