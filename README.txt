Apache Rave - README.txt
Licensed under Apache License 2.0 - http://www.apache.org/licenses/LICENSE-2.0
--------------------------------------------------------------------------------

About
=====
Apache Rave is A new Web And Social Mashup Engine. It will provide an
out-of-the-box as well as an extendible lightweight Java platform to host, serve
and aggregate (Open)Social Gadgets and services through a highly customizable
and Web 2.0 friendly front-end. Rave is targeted as engine for internet and
intranet portals and as building block to provide context-aware personalization
and collaboration features for multi-site/multi-channel (mobile) oriented and
content driven websites and (social) network oriented services and platforms.
For the OpenSocial container and services the (Java) Apache Shindig will be
integrated. At a later stage further generalization is envisioned to also
transparently support W3C Widgets using Apache Wookie.

Release Notes
=============
 - Added new runtime capability to override shindig host.  Add -Dshindig.host=servername to $JAVA_OPTS to override the localhost setting


Getting Started
===============
Please visit the project website for the latest information:
    http://rave.apache.org/

Along with the developer mailing list archive:
    http://mail-archives.apache.org/mod_mbox/rave-dev/


System Requirements
===================
You need a platform that supports Java SE 6 or later.

Building and running
====================
To build from source code:

  - Requirements:
    Sources compilation require Java SE 6 or higher.
    The project is built with Apache Maven 3+ (suggested is 3.0.3).
    You need to download and install Maven 3 from: http://maven.apache.org/

  - The Rave project itself (this one) depends on the separate Rave Master project
    which defines general and global settings for the whole of the Rave project,
    independent of a specific release.
    As its rave-master-pom is already published to the Apache Snapshots repository,
    there is no need to check it out manually and build it locally yourself,
    unless changes are needed on general and global level.
    
    If so needed, the Rave Master project can be checked out from:
      http://svn.apache.org/repos/asf/rave/rave-master-pom/trunk rave-master-pom

    After check out, cd into rave-master-pom and invoke maven to install it using:
      $mvn install
    
  - To build the Rave project invoke maven in the root directory:
      $mvn install

To run a local Tomcat instance with rave-shindig and rave-portal deployed:

  - from the top-level rave directory, use the command
      $mvn cargo:run -f rave-portal/pom.xml
  - alternatively, navigate to the rave-portal subdirectory and invoke:
      $mvn cargo:run
  - open url http://localhost:8080/ in a browser
  - press Ctrl-C in the console to stop Tomcat again
--------------------------------------------------------------------------------
