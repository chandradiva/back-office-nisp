'use strict';

angular.module('swipeLiDemo', [
  'swipeLi'
])
  .controller('MainCtrl', ['$scope', function ($scope) {

    $scope.list = [
    {
      name: 'Swipe Right to Transfer',
      doInvert: false,
    }];

    $scope.doInvert = false;

    $scope.done = function (item) {
      console.log('%s marked as accepted!', item);
      item.doInvert = true;
    };

    $scope.skip = function (item) {
      console.log('%s marked as rejected!', item);
      item.doInvert = true;
    };

  }]);