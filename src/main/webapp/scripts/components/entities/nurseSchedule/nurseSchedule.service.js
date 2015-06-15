'use strict';

angular.module('rccaApp')
    .factory('NurseSchedule', function ($resource, DateUtils) {
        return $resource('api/nurseSchedules/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.scheduleDate = DateUtils.convertLocaleDateFromServer(data.scheduleDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.scheduleDate = DateUtils.convertLocaleDateToServer(data.scheduleDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.scheduleDate = DateUtils.convertLocaleDateToServer(data.scheduleDate);
                    return angular.toJson(data);
                }
            }
        });
    });
