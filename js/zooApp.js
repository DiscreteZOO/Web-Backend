var app = angular.module("zoo", ['ui.grid'])

app.controller('ZooCtrl', ['$scope', function ($scope) {

	$scope.objects = [
		{	"order": 10,
			"name": "Petersen",
			"p1": 2,
			"p2": "yes",
			"p3": 123 },

		{	"order": 8,
			"name": "cube",
			"p1": 25,
			"p2": "no",
			"p3": 734 }
		]

	}])
