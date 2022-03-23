docker run -it -d --rm --name maven -v $(pwd):/opt/app -p 8080:8080 maven:3.8-openjdk-11 sleep infinity

docker exec -it maven  /bin/bash

mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false

mvn compile exec:java -Dexec.mainClass="com.example.app.App"