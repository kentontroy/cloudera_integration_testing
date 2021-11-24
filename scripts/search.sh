#!/bin/bash

while getopts ":q:" opt; do
  case $opt in
    q) CLASSNAME=${OPTARG}
    ;;
    esac
done

echo "Searching for ${CLASSNAME}"

for f in `ls ${CLOUDERA_JAR_PATH}`
do
  m=`jar tvf ${CLOUDERA_JAR_PATH}/$f | grep ${CLASSNAME}`
  if [ -n "$m" ]
  then
    echo $f
  fi
done
