define(['./rave_opensocial', './rave_wookie'], function(opensocial, wookie){

    /*
    Overlay to add or change supported providers. Register provider names in LOWER CASE for consistency.
     */
    return {
        'opensocial': opensocial,
        'w3c': wookie
    }

})