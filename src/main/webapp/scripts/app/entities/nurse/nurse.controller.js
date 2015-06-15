'use strict';

angular.module('rccaApp')
    .controller('NurseController', function ($scope, Nurse, NurseSchedule) {
        $scope.nurses = [];
        $scope.nurseschedules = NurseSchedule.query();
        $scope.loadAll = function() {
            Nurse.query(function(result) {
               $scope.nurses = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Nurse.get({id: id}, function(result) {
                $scope.nurse = result;
                $('#saveNurseModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.nurse.id != null) {
                Nurse.update($scope.nurse,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Nurse.save($scope.nurse,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Nurse.get({id: id}, function(result) {
                $scope.nurse = result;
                $('#deleteNurseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Nurse.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNurseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveNurseModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.nurse = {name: null, isActive: null, nurseId: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
