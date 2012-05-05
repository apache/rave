Meta:

Narrative:
As the user of an OpenID account 
I want to login to my account using OpenID
So that I can view my pages

Scenario: User with OpenID logs into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I provide my OpenID identity "http://rave2011.myopenid.com/"
Then I see the OpenID authentication page
When I provide my OpenID password "rave2011"
Then I see the message "Hello http://rave2011.myopenid.com/, welcome to Rave!" for the user "http://rave2011.myopenid.com/"
When I log out
Then I see the Rave login page
