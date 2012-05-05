Meta:

Narrative:
As the user johnldap
I want to login to my account
So that I can view my pages

Scenario: John Doe logs into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in with username "johnldap" and password "johnldap"
Then I see the message "Hello John Ldap, welcome to Rave!" for the user "johnldap"
When I log out
Then I see the Rave login page
