FROM ubuntu:jammy

RUN mkdir /opt/code &&\
    mkdir /opt/app
ADD ./ /opt/code/
ENV M2_HOME '/opt/apache-maven-3.9.6'
ENV PATH "$M2_HOME/bin:$PATH"
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"


RUN apt-get update && \
    apt-get install -y \
    openjdk-17-jdk \
    ffmpeg \
    curl \
    && apt-get clean
RUN curl https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -o apache-maven-3.9.6-bin.tar.gz &&\
    tar xzvf apache-maven-3.9.6-bin.tar.gz -C /opt/ &&\
    rm -rf apache-maven-3.9.6-bin.tar.gz &&\
    cd /opt/code/ &&\
    mvn clean package &&\
    mv /opt/code/target/app.jar /opt/app/ &&\
    rm -rfv /opt/code &&\
    chmod 777 -R /opt/app/

ENTRYPOINT ["java","-jar","/opt/app/app.jar"]


