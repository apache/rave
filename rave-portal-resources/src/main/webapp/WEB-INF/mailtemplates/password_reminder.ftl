<#-- @ftlvariable name="user" type="org.apache.rave.portal.model.User" -->
<#-- @ftlvariable name="reminderUrl" type="java.lang.String" -->
Dear ${user.getUsername()},

We've received a request to update the password associated with your Rave account.

To complete this process, open the following link in your browser:

${reminderUrl}

If you did not request this change, no further action is required.


