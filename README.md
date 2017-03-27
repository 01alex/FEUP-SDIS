# SDIS - 2nd Project Specification

<p align="right">27 March - 2017</p>


##### T1G05

- Luís Costa  
- Guilherme Routar  
- Alexandre Ribeiro  


## Purpose of the Application

The group proposes to develop a distributed service web appplication, also available on mobile, which should behave similarly to a Dropbox, a service for file hosting.

The application should allow any (eventually authenticated) user to access it and perform actions such as **upload** and **delete files** and **create** and **delete folders** and also **download content**.


## Main Features

When users start the application they are presented with a *screen* containing the root of the remote directory. On that screen the user can upload new files or create a folder to subdivide the content. The user can also perform basic file management operations such as **UPLOAD** and **DELETE** files.

## Protocols to be considered
- TCP - synchronize user data and communicate with the host service
- TCP and UDP - communication via Dropbox LanSync Protocol
- SSL and FTP - File hosting common protocols

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

Considering the basis architecture and extra functionalities, the group considers 20 (twenty) to be a valid ceiling for the final grade.
