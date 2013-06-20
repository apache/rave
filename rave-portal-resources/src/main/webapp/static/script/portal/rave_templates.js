/**
 * Created with IntelliJ IDEA.
 * User: DGORNSTEIN
 * Date: 6/14/13
 * Time: 8:45 AM
 * To change this template use File | Settings | File Templates.
 */
//All set!!

define(["handlebars", "jquery"], function(Handlebars, $){
    var templates = {}

    $('[data-template-for]').each(function () {
        var key = $(this).data('template-for');
        var source = $(this).html();

        templates[key] = Handlebars.compile(source);
    });

    return{
        templates:templates
    }

})
