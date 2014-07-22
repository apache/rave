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
    Vagrant v1.6.3+, Ansible 1.6.5+

  - Install the `vagrant-hostsupdater` plugin by performing `vagrant plugin install vagrant-hostsupdater`.

  - Add `192.168.13.37   localhost` to the top of your hosts file.

  - To build the virtual machine, simply run `vagrant up`.
    
  - To run Rave, ssh onto the Vagrantbox with the command `vagrant ssh`, then run the command:
    `sudo mvn cargo:run -f /rave/rave-portal/pom.xml`

  - Navigate to http://localhost:8080/ in a browser

  - press Ctrl-C from within the Vagrant box to stop the app
--------------------------------------------------------------------------------
