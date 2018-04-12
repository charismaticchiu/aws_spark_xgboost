#!/bin/sh
sudo yum update

sudo yum install gcc-c++

wget https://cmake.org/files/v3.10/cmake-3.10.0.tar.gz
tar xzf cmake-3.10.0.tar.gz
cd cmake-3.10.0
./bootstrap --prefix=/usr
make
sudo make install

wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven

sudo yum -y install git

cd ../
git clone --recursive https://github.com/dmlc/xgboost
cd xgboost
make -j4

export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64

cd jvm-packages
mvn package -DskipTests


curl https://bintray.com/sbt/rpm/rpm > bintray-sbt-rpm.repo
sudo mv bintray-sbt-rpm.repo /etc/yum.repos.d/
sudo yum install sbt


export PATH=/usr/lib/jvm/java-1.8.0-openjdk.x86_64/bin:$PATH

cd ../../

git clone --recursive https://github.com/charismaticchiu/aws_spark_xgboost.git
cd aws_spark_xgboost

