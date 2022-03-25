# Morphia Quickstart
This repository is for the article Morphia Java ODM for MongoDB that can be found on MongoDB's website at https://www.mongodb.com/languages/morphia.

## Getting started
You should be able to load the code in an editor and run it from there. However, if you prefer, you can also use a container rather than installing the necessary runtimes.

### Using a container
Start the container
```
docker run -it -d --rm --name maven -v $(pwd):/opt/app -p 8080:8080 maven:3.8-openjdk-11 sleep infinity
```
Open a shell session to the container
```
docker exec -it maven  /bin/bash

cd /opt/app
```
