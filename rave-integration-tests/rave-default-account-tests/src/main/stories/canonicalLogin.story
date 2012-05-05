Meta:

Narrative:
As the user canonical
I want to login to my account
So that I can view my pages

Scenario: User canonical logs into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in with username "canonical" and password "canonical"
Then I see the message "Hello Canonical User, welcome to Rave!" for the user "canonical"
When I log out
Then I see the Rave login page
