all: options build

options:
    export ANT_OPTS=-Xmx256M

build:
	ant
