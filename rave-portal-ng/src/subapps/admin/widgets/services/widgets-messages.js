/*
 * widgetsMessages
 * A service to show messages on the widgets page. Because other
 * states can cause messages to display (namely the category state),
 * we need a service that's shared between the two.
 *
 */

define(function() {
  return function() {
    var html = '';
    var className = '';

    return {
      createMessage: function(username) {
        html = 'Created widget "<b>' + username + '</b>"';
        className = 'alert-success';
      },

      updateMessage: function(username) {
        html = 'Updated widget "<b>' + username + '</b>"';
        className = 'alert-success';
      },

      deleteMessage: function(username) {
        html = 'Deleted widget "<b>' + username + '</b>"';
        className = 'alert-success';
      },

      clearMessage: function() {
        html = null;
        className = '';
      },

      showMessage: function() {
        return html ? true : false;
      },

      messageHtml: function() {
        return html;
      },

      messageClassName: function() {
        return className;
      }
    };
  };
});
