'use strict';

angular.module('rccaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


