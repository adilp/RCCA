'use strict';

angular.module('rccaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nurseSchedule', {
                parent: 'entity',
                url: '/nurseSchedule',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'rccaApp.nurseSchedule.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nurseSchedule/nurseSchedules.html',
                        controller: 'NurseScheduleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nurseSchedule');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nurseScheduleDetail', {
                parent: 'entity',
                url: '/nurseSchedule/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'rccaApp.nurseSchedule.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nurseSchedule/nurseSchedule-detail.html',
                        controller: 'NurseScheduleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nurseSchedule');
                        return $translate.refresh();
                    }]
                }
            });
    });
