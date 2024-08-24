# Amazon Corretto with Java 21 / Debian 12
FROM maven:3.9-amazoncorretto-21-debian-bookworm
RUN apt update && apt -y install git && \
    unlink /etc/localtime && \
    ln -s /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
