'use strict';

angular.module('rccaApp')
    .controller('NurseScheduleController', function ($scope, NurseSchedule, Nurse) {
        $scope.nurseSchedules = [];
        $scope.nurses = Nurse.query();
        $scope.loadAll = function() {
            NurseSchedule.query(function(result) {
               $scope.nurseSchedules = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            NurseSchedule.get({id: id}, function(result) {
                $scope.nurseSchedule = result;
                $('#saveNurseScheduleModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.nurseSchedule.id != null) {
                NurseSchedule.update($scope.nurseSchedule,
                    function () {
                        $scope.refresh();
                    });
            } else {
                NurseSchedule.save($scope.nurseSchedule,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            NurseSchedule.get({id: id}, function(result) {
                $scope.nurseSchedule = result;
                $('#deleteNurseScheduleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            NurseSchedule.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNurseScheduleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveNurseScheduleModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.nurseSchedule = {scheduleDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
