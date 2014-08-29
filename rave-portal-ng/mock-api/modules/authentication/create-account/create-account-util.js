/*
 * createAccountUtil
 * Utility methods for the account creation endpoint
 *
 */

define(function(require) {
  var _ = require('underscore');
  var api = require('../../../core');

  var createAccountUtil = {

    // The necessary fields to create a new account
    requiredFields: [
      'username',
      'password',
      'confirmPassword',
      'email'
    ],

    // Returns true if data contains all of the requiredFields.
    // false otherwise.
    ensureFields: function(data) {
      var keys = _.keys(data);
      var intersect = _.intersection(keys, createAccountUtil.requiredFields);
      return intersect.length === createAccountUtil.requiredFields.length;
    },

    // Returns true if the email is valid; false otherwise
    validateEmail: function(email) {
      var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return emailRegex.test(email);
    },

    createAccount: function(data) {
      var newUserData = {
        username: data.username,
        password: data.password,
        email: data.email,
        openIdUrl: (data.hasOwnProperty('openIdUrl') ? data.openIdUrl : ''),
        firstName: (data.hasOwnProperty('firstName') ? data.firstName : ''),
        lastName: (data.hasOwnProperty('lastName') ? data.lastName : '' ),
        nameSeenByOthers: (data.hasOwnProperty('nameSeenByOthers') ? data.nameSeenByOthers : ''),
        relationshipStatus: (data.hasOwnProperty('relationshipStatus') ? data.relationshipStatus: ''),
        description: (data.hasOwnProperty('description') ? data.description: ''),
        locked: false,
        sessionToken: '',
        enabled: true,
        expired: false,
        authorities: ['ROLE_USER']
      };

      api.db.insert('users', newUserData);
      api.db.commit();
    }
  };

  return createAccountUtil;
});
