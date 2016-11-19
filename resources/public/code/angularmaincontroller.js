var myApp = angular.module('newsApp', ['ui.bootstrap']);

myApp.controller('MainCtrl', function ($scope, $http) {
    $scope.totalItems = 60;
    $scope.currentPage = 3;

    /*    $scope.displayPartial = function(page_number) {
     $http.get('pages/'+page_number).success(function(data) {
     $scope.partial = data;
     });
     };*/

    $scope.removeComment = function (commentId) {
        $http.post("localhost:3000/api/add-news", {"purge_everything": "blablbabab"})
            .success(function (data) {
                console.log("request was send: " + data)
            });
    }
});
