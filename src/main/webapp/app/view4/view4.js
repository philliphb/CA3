'use strict';

angular.module('myApp.view4', ['ngRoute'])

        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/view4', {
                    templateUrl: 'app/view4/view4.html',
                    controller: 'exchangeRateData'
                });
            }])
        .controller("exchangeRateData", ['$http', '$scope', function ($http, $scope) {

                $scope.data = {};
                $scope.getData = function () {
                    $http.get('api/currency/dailyrates')
                            .success(function (data, status, headers, config) {
                                console.log(data);
                                $scope.data = data;
                            })
                            .error(function (data, status, headers, config) {

                            });
                };
                $scope.getData();
                $scope.calculatedCurrency;
                $scope.amount;
                $scope.fromCurrency;
                $scope.toCurrency;
                $scope.calc = function () {
                    console.log($scope.amount);
                    console.log($scope.fromCurrency.code);
                    console.log($scope.toCurrency);
                    $http.get('api/currency/calculator/' + $scope.amount + '/' + $scope.fromCurrency.code + '/' + $scope.toCurrency.code)
                            .success(function (data, status, headers, config) {
                                console.log(data);

                                $scope.calculatedCurrency = data.value;
                            })
                            .error(function (data, status, headers, config) {
                                console.log("Error " + data);
                            });
                };
            }]);