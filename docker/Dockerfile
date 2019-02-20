FROM openjdk:8

ARG BRANCH=master

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping && \
  rm -rf /var/lib/apt/lists/*

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Installing Common
RUN \
  git clone https://github.com/fogbow/common.git && \
  (cd common && git checkout $BRANCH && mvn install -Dmaven.test.skip=true)

RUN \
  git clone https://github.com/fogbow/authentication-service.git && \
  (cd authentication-service && git checkout $BRANCH)

# Define working directory.
WORKDIR /root/authentication-service

# Generates the build number based on the commit checksum
RUN \
    (build_number=$(git rev-parse --short 'HEAD') && echo "build_number=$build_number-(buzios)" > build)

RUN \
  mvn dependency:sources
