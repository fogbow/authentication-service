FROM openjdk:8

ARG BRANCH_COMMON
ARG BRANCH_AS

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
  (cd common && git checkout $BRANCH_COMMON && mvn install -Dmaven.test.skip=true)

# Downloading Authentication service
RUN \
  git clone https://github.com/fogbow/authentication-service.git && \
  (cd authentication-service && git checkout $BRANCH_AS)

# Define working directory.
WORKDIR /root/authentication-service

# Generates the build number based on the commit checksum
RUN \
    (build_number=$(git rev-parse --short 'HEAD') && \
    cd ../common && common_build_number=$(git rev-parse --short 'HEAD') && \
    echo "build_number=as-$build_number-common-$common_build_number" > build)

RUN \
  mvn dependency:sources

