# NieCeneo

1. Clone the repository

```
git clone --recursive https://github.com/rfmajor/io-backend.git
```

2. Install `java 16` and `mvn 3.8.7`

```
curl https://download.java.net/java/GA/jdk16/7863447f0ab643c585b9bdebf67c69db/36/GPL/openjdk-16_linux-x64_bin.tar.gz
wget https://dlcdn.apache.org/maven/maven-3/3.8.7/binaries/apache-maven-3.8.7-bin.tar.gz -P /tmp
```

https://java.tutorials24x7.com/blog/how-to-install-openjdk-16-on-ubuntu-20-04-lts \
https://phoenixnap.com/kb/install-maven-on-ubuntu 

3. Build backend jar

```
cd io-backend/
mvn clean package
```

4. Install docker

```
sudo apt install docker.io docker-compose -y
```

5. Happy hacking!

```
sudo docker-compose up -d
```
