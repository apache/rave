/*
 * console
 * Console shims. They log if your your browser supports console;
 * otherwise, nothing happens.
 *
 */

(function() {
  var console = window.console;
  var supportsConsole = !!console;
  var supportsLog = supportsConsole && !!console.log;
  var supportsWarn = supportsConsole && !!console.warn;

  var consoleShim = {

    // JSHint & IE-safe version of console.log
    log: function() {
      if (!supportsLog) { return; }
      console.log.apply(console.log, arguments);
    },

    warn: function() {
      if (!supportsWarn) { return; }
      console.warn.apply(console.log, arguments);
    }
  };

  window.util = window.util || {};
  window.util.console = consoleShim;
})();
