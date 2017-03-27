# SDIS - 2nd Project Specification

<p align="right">27 March - 2017</p>


##### T1G05

- Luís Costa  
- Guilherme Routar  
- Alexandre Ribeiro  


## Purpose of the Application

The group proposes to develop a secret Dropbox allocated in FEUP. The idea is to have an hidden router somewhere at feup with and use it to create a server hosting our box.

The application will behave like the original Dropbox, a service for file hosting. 
The application will have two main **stack screens** (the same page layout for several screens/app interface): the folder screen where the contents are displayed and the *file menu* with several subprotocols as **UPLOAD**, **DELETE** file or **CREATE**, **DELETE** Folder. 


## Main Features

When users start the application they are presented with a *screen* containing the root of the remote directory. On that screen the user can upload new files or create a folder to subdivide the content. The user can also perform basic file management operations such as **UPLOAD** and **DELETE** files.

#### Folder View

A classic file explorer view with files displayed as list or icons. Unsorted files will be organized by **upload date**.


#### File View

A menu that pops when you click on the file. This file view contains the main operations that can be done with the file (delete, edit, copy, move) and the file info (size, created_at, type).


## Target Platforms

- Java standalone application for PC/Mac

## Additional Services and Improvements

After the basis architecture has been implemented, the group will propose to develop a few extra functionalities in order to achieve a more complex and realist application.

The characteristics defined below are expected to be functional in the final version.

### Security / Authentication

Access to the application should only be granted after the user authenticates by inputting his username and password.

### Failure Tolerance

The application should handle effectively faults such as
- Crash of a peer, including if that peer is the owner of the metadata of the service;
- Internet disconnection

### Scalability

The implemented architecture should be able to support an application hosting whether a restricted or wide number of users. 


## Proposed grade ceiling

Given the described features, and application architecture, the group considers 20 to be a valid ceiling for the final grade.
