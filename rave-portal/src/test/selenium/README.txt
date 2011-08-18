This directory contains Selenium IDE test scripts for the Firefox Web browser. 

REQUIRED DOWNLOADS: Before running the tests, you must do the following:

1. Download and install a stable version of Firefox (currently, version 5).  See http://www.mozilla.com/.

2. Download and install the (free) Selenium IDE Firefox plugin. See http://seleniumhq.org/projects/ide/.


RUNNING: To run the tests, do the following:

1. Download, install, and run Apache Rave. The tests assume the portal is available from http://localhost:8080.

2. Start Firefox.

3. From Firefox, start the Selenium IDE. See the Selenium IDE documentation. 

4. In the Selenium IDE, load the Rave test suite, Rave-Selenium-Tests, located in rave/rave-portal/rave-selenium-tests.

5. Run the tests, either individually or all together, through the Selenium IDE interface.


TIPS: 

1. If rerunning tests, change the new user name property in "Rave-New-Account-Test". 

2. Use Firefox's "private browsing" feature if you want to repeat the OpenID tests.  

3. If using Rave's default, in-memory database, then shutting down and restarting the browser will restore the default settings: new user accounts will be discarded, and built-in user account layouts will be restored.




