Meta:

Narrative:
As an admin
I want to delete an account
So that I can clean up test accounts.

Scenario: Admin logs into the portal and deletes an account
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in as an administrator with username "canonical" and password "canonical"
Then I see the admin interface link
When I click the admin interface link
Then I see the admin interface
When I click the "Users" link
Then I get the user search form
When I search for username "newuser"
Then I see the matches for "newuser"
When I click the link for information on "newuser"
Then I see the information for user "newuser"
When I delete the the user "newuser"
Then I see "The user profile has been removed"
When I log out
Then I see the Rave login page
