#!/bin/bash

javac app/DBS.java
javac peer/Peer.java
java peer/Peer 1 3 luis 224.0.0.1 5000 224.0.0.2 5000 224.0.0.3 5000