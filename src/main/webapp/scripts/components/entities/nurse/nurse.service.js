'use strict';

angular.module('rccaApp')
    .factory('Nurse', function ($resource, DateUtils) {
        return $resource('api/nurses/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
