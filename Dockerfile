FROM ubuntu:latest
LABEL authors="dmitriydubov"

ENTRYPOINT ["top", "-b"]