'use strict';

angular.module('rccaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nurse', {
                parent: 'entity',
                url: '/nurse',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'rccaApp.nurse.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nurse/nurses.html',
                        controller: 'NurseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nurse');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nurseDetail', {
                parent: 'entity',
                url: '/nurse/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'rccaApp.nurse.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nurse/nurse-detail.html',
                        controller: 'NurseDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nurse');
                        return $translate.refresh();
                    }]
                }
            });
    });
