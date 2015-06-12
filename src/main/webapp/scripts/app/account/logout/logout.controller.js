'use strict';

angular.module('rccaApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
