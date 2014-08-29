define(function() {

  // Return our factory method
  return function() {
    var message = '';
    var className = '';

    return {
      showSuccess: function() {
        message = 'Your preferences have been updated.';
        className = 'alert-success';
      },

      showError: function(err) {
        message = err.data;
        className = 'alert-danger';
      },

      clearMessage: function() {
        message = null;
        className = '';
      },

      messageText: function() {
        return message;
      },

      messageClassName: function() {
        return className;
      },

      showMessage: function() {
        return message ? true : false;
      }
    };
  };
});
