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

### Security
### Peers Failure Tolerance
### 



### Botnet

We would like to test our application using another distributed computing system, a botnet. Botnet is a number of inter-connected devices used by a botnet owner to perform various tasks. Botnets can steal data, send spam, allow the attacker access to the device and its connection. 
Botnets follow a **Client-Server** or a **Peer to Peer** architecture. 

### Scalability

The application should scale with ease: a similar performance should be achieved, whether 1 or 1000 users are using the app. The group considers this will come as a good consequence of the architecture improvement.

### Consistency

The application should be consistent, i.e. it should correctly manage concurrent events and file uploads. For example: if a User A modifies or deletes a file, the User B shouldn't be able to use it anymore.

### Authentication

Access to private **box's** should require authentication: users need to input the *username* and *password*.

### Fault tolerance

The application should tolerate faults with ease, such as temporary internet disconnections and resume a file upload whenever the connection comes back.

## Proposed grade ceiling

Given the described features, and application architecture, the group considers 20 to be a valid ceiling for the final grade.
