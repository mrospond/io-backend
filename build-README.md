# NieCeneo

1. install nodejs (we'll need to copy react boilerplate to the frontend folder)
```
sudo apt update
sudo apt install curl
curl -fsSl https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install nodejs -y
```

2. Clone

```
git clone --recursive https://github.com/rfmajor/io-backend.git
cd io-backend/io_project_front-end/
```

3. Add react files
```
sudo chown -R 1000:1000 "/home/${USER}/.npm/"
npm cache clean --force
npx create-react-app frontend
cp -r * frontend/
```

4. Install
mvn 3.8.7
java 16

```
cd io-backend/
mvn clean package
```

n-1. Run
//cd /io-backend/io_project_front-end/
//sudo docker build -t io-frontend .



n. Refs

https://phoenixnap.com/kb/install-maven-on-ubuntu
https://java.tutorials24x7.com/blog/how-to-install-openjdk-16-on-ubuntu-20-04-lts

To update submodule
```
cd io_project_front-end/
git pull origin main
```
