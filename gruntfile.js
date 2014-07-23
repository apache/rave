module.exports = function(grunt) {
  require('load-grunt-tasks')(grunt);

  grunt.initConfig({

    // Our directory variables. If you change
    // the app's directory just change the variables here
    rave: {
      bower: 'bower_components',
      src: 'rave-portal-ng/src',
      dev: 'rave-portal-ng/dev',
      prod: 'rave-portal-ng/prod'
    },

    // Completely destroys the built version of our app
    // dev is the development version. prod is the production version.
    clean: {
      dev: '<%= rave.dev %>',
      prod: '<%= rave.prod %>'
    },

    // Anything that doesn't get compiled needs to be copied
    // from the src directory to the one that we're serving
    copy: {
      devJs: {
        expand: true,
        cwd: '<%= rave.src %>',
        src: ['**/*.js'],
        dest: '<%= rave.dev %>'
      },
      devHtml: {
        expand: true,
        cwd: '<%= rave.src %>',
        src: ['**/*.html'],
        dest: '<%= rave.dev %>'
      },
      devImg: {
        expand: true,
        cwd: '<%= rave.src %>',
        src: ['images/**/*.{jpg,gif,png}'],
        dest: '<%= rave.dev %>'
      }
    },

    // Lints our source code & unit tests
    jshint: {
      dev: {
        options: {
          jshintrc: '.jshintrc-dev'
        },
        src: '<%= rave.src %>/**/*.js'
      },
      prod: {
        options: {
          jshintrc: '.jshintrc-prod'
        },
        src: '<%= rave.src %>/**/*.js'
      },
      tests: {

      }
    },

    // Compile our less
    less: {
      options: {
        paths:  ['<%= rave.bower %>', '<%= rave.src %>/less']
      },
      dev: {
        src: [
          '<%= rave.src %>/less/rave.less',
          '<%= rave.src %>/**/*.less',
          '!<%= rave.src %>/less/*/*.less',
          '!<%= rave.src %>/less/global-imports.less',
        ],
        dest: '<%= rave.dev %>/style.css'
      },
      prod: {
        src: ['<%= rave.src %>/rave.less', '<%= rave.src %>/**/*.less'],
        dest: '<%= rave.prod %>/style.css'
      }
    },

    // Serve up our client-side code (for development in lieu of a Java server)
    // Also lets you test the production code, although it is not a replacement
    // for a production server.
    connect: {
      dev: {
        options: {
          base: ['<%= rave.bower %>', '<%= rave.dev %>'],
          livereload: true,
          keepalive: true
        }
      },
      prod: {
        options: {
          base: '<%= rave.prod %>',
          keepalive: true
        }
      }
    },

    watch: {
      options: {
        livereload: true
      },
      grunt: {
        files: ['gruntfile.js']
      },
      less: {
        files: ['<%= rave.src %>/**/*.less'],
        tasks: ['less:dev']
      },
      html: {
        files: ['<%= rave.src %>/**/*.html'],
        tasks: ['copy:devHtml']
      },
      js: {
        files: ['<%= rave.src %>/**/*.js'],
        tasks: ['jshint:dev', 'copy:devJs']
      }
    },

    // Watch and connect are long-living processses,
    // so they need to be executed concurrently
    concurrent: {
      options: {
        logConcurrentOutput: true
      },
      dev: ['connect:dev', 'watch']
    },

    vagrantssh: {
      server: {
        commands: [
          'sudo mvn cargo:run -f /rave/rave-portal/pom.xml',
        ],
        callback: function(grunt, output) {
          grunt.log.writeln('The app is running at localhost:8080/portal.');
        }
      }
    }
  });

  grunt.registerTask('start-api', 'Start the API', ['vagrantssh']);

  grunt.registerTask('dev', 'Build the dev app & set up the app environment.', [
    'build-dev',
    'connect-dev'
  ]);

  grunt.registerTask('connect-dev', 'Start the mock server for the development build', [
    'concurrent:dev'
  ]);

  grunt.registerTask('connect-prod', 'Start the mock server for the production build', [
    'connect:prod'
  ]);

  grunt.registerTask('build-dev', 'Build the development version of Rave', [
    'jshint:dev',
    'clean:dev',
    'copy:devHtml',
    'copy:devJs',
    'copy:devImg',
    'less:dev'
  ]);

  grunt.registerTask('build-prod', 'Build a production version of Rave', [
    'jshint:prod',
    'clean:prod',
    'less:prod'
  ]);

  grunt.registerTask('test', 'Lints code and unit tests', [
    'jshint:prod'
  ]);

  grunt.registerTask('default', 'An alias for running tests', [
    'test'
  ]);
};
