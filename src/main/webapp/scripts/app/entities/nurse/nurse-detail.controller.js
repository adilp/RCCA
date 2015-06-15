'use strict';

angular.module('rccaApp')
    .controller('NurseDetailController', function ($scope, $stateParams, Nurse, NurseSchedule) {
        $scope.nurse = {};
        $scope.load = function (id) {
            Nurse.get({id: id}, function(result) {
              $scope.nurse = result;
            });
        };
        $scope.load($stateParams.id);
    });
