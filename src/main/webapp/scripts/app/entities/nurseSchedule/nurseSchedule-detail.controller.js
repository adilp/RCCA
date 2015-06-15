'use strict';

angular.module('rccaApp')
    .controller('NurseScheduleDetailController', function ($scope, $stateParams, NurseSchedule, Nurse) {
        $scope.nurseSchedule = {};
        $scope.load = function (id) {
            NurseSchedule.get({id: id}, function(result) {
              $scope.nurseSchedule = result;
            });
        };
        $scope.load($stateParams.id);
    });
