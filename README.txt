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
To setup the vagrant development environment:

  - Requirements:
    Vagrant v1.6.3+, Ansible 1.6.5+

  - Install the `vagrant-hostsupdater` plugin by performing `vagrant plugin install vagrant-hostsupdater`.

  - To build the virtual machine, simply run `vagrant up`.

To run the apache rave application:

  - SSH into the vagrant box
  - Run the following command to start the rave portal
      `sudo mvn cargo:run -f /rave/rave-portal/pom.xml`
  - open url http://rave.dev:8080/portal in a browser
  - Login as user `canonical` with the password `canonical`

When you're done, you can type `ctrl + C` in the console to stop the application.

Unbuilding the Application
==========================

Building the application creates a handful of directories called `target` in the repository. To remove them all at once run `mvn clean` from
the `/rave` directory on the Vagrantbox.

Note that this will require that you re-provision the box to run the app. You can do this from the root directory of the application when you're
*not on the box* by running `vagrant provision`.
`vagrant provision` from 
