Meta:

Narrative:
As a new user
I want to create an account
So that I can login into the portal

Scenario: User creates a new account and logs in into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I follow the new account link
Then I get the new account form
When I fill in the form with username "newuser" password "password" confirmpassword "password" email "newuser@example.com"
And I submit the new account form
Then I see the login page
And A message appears "Account successfully created"
When I fill in the login form with username "newuser" password "password"
Then I see my portal page with the add new widgets box
