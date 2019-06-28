#!/bin/bash
cd ../
nohup java -jar jenkinsci-0.0.1-SNAPSHOT.jar -cp config/generator.properties >bin/nohup.out 2>&1 &
