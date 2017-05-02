#!/bin/bash

javac app/DBS.java
javac peer/Peer.java
java app/DBS 172.30.1.252/luis BACKUP DBS.class  1