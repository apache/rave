module.exports = function(grunt) {
  require('load-grunt-tasks')(grunt);

  grunt.initConfig({
    vagrantssh: {
      server: {
        commands: [
          'sudo mvn cargo:run -f /rave/rave-portal/pom.xml',
        ],
        callback: function(grunt, output) {
          grunt.log.writeln('The app is running at rave.dev:8080/portal.');
        }
      }
    }
  });

  grunt.registerTask('dev', ['vagrantssh']);
};
