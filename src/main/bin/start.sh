#!/bin/bash
cd ../
nohup java -jar jenkinsci-0.0.1-SNAPSHOT.jar -cp config/generator.properties:templates/ >bin/nohup.out 2>&1 &
