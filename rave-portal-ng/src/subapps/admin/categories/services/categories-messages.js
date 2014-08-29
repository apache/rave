/*
 * categoriesMessages
 * A service to show messages on the categories page. Because other
 * states can cause messages to display (namely the category state),
 * we need a service that's shared between the two.
 *
 */

define(function() {
  return function() {
    var html = '';
    var className = '';

    return {
      createMessage: function(createdText) {
        html = 'Created category "<b>' + createdText + '</b>"';
        className = 'alert-success';
      },

      updateMessage: function(updateData) {
        html = 'Renamed "<b>' + updateData.oldText + '</b>" to "<b>' + updateData.newText + '</b>"';
        className = 'alert-success';
      },

      deleteMessage: function(deletedText) {
        html = 'Deleted "<b>' + deletedText + '</b>"';
        className = 'alert-success';
      },

      errorMessage: function(error) {
        html = error;
        className = 'alert-danger';
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
