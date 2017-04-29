#!/bin/bash

make
STATUS="${?}"
if [ $STATUS != "0" ]; then
    exit 1
fi

shopt -s globstar

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

for var in "$@"
do
    echo "Begin testing on a$var"
    COUNT=0
    PASSED=0

    stdlib=""
    for e in $(find ./$var.0 -type f -name '*.java')
    do
        stdlib=$stdlib$e" "
    done

    for f in ./a$var/*
    do
    	COUNT=$((COUNT+1))
        echo $f
        if [[ -d $f ]]; then
            ARGUMENT=""
            for g in $(find $f -type f -name '*.java')
            do
                ARGUMENT=$ARGUMENT$g" "
            done
            ./joosc $stdlib$ARGUMENT;
        elif [[ -f $f ]]; then
            if [[ "$f" == *.java ]]
            then
    	        ./joosc $stdlib$f;
            fi
        fi

    	STATUS="${?}"
    
    	FILE_NAME=$(echo $f| cut -d'/' -f 3)
    	SHORT=${FILE_NAME:0:2}
    	if [ $SHORT == "Je" ]; then
    	    if [ $STATUS == "42" ]; then
    	    	PASSED=$((PASSED + 1))
    	    	echo "${GREEN}Passed${NC}"
    	    else
    	        #echo "$f"
    	    	echo "${RED}FAILED${NC}"
    	    fi
    
    	else
    	    if [ $STATUS == "0" ]; then
    	    	PASSED=$((PASSED + 1))
    	    	echo "${GREEN}Passed${NC}"
    	    else
    	        #echo "$f"
    	    	echo "${RED}FAILED${NC}"
    	    fi
    	fi
    done
    echo "a$var: $PASSED out of $COUNT passed."
done
