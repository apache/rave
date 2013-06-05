define([], function () {
    return function (msg) {
        if (console && console.log) {
            console.log(msg);
        }
    }
})