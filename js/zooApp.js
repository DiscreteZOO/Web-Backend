(function () {

  'use strict'

	angular
    .module("zoo", ['ngTouch','ui.bootstrap', 'ui.grid', 'ui.grid.infiniteScroll', 'ui.scroll'])
    .controller('ZooCtrl', ['$http', '$scope', ZooCtrl])

	function ZooCtrl($http) {
    var self = this
		var context = "http://localhost:8080"

    self.properties = [
			{name: 'arc transitive', dbName: 'is_arc_transitive', type: 'Boolean', priority: 51, selected: false, conditionB: true, edit: false},
			{name: 'bipartite', dbName: 'is_bipartite', type: 'Boolean', priority: 52, selected: false, conditionB: true, edit: false},
			{name: 'Cayley', dbName: 'is_cayley', type: 'Boolean', priority: 53, selected: false, conditionB: true, edit: false},
			{name: 'clique number', dbName: 'clique_number', type: 'Numeric', priority: 11, selected: false, conditionN: '', edit: true},
			{name: 'diameter', dbName: 'diameter', type: 'Numeric', priority: 41, selected: false, conditionN: '', edit: true},
			{name: 'distance regular', dbName: 'is_distance_regular', type: 'Boolean', priority: 31, selected: false, conditionB: true, edit: false},
			{name: 'distance transitive', dbName: 'is_distance_transitive', type: 'Boolean', priority: 32, selected: false, conditionB: true, edit: false},
			{name: 'edge transitive', dbName: 'is_edge_transitive', type: 'Boolean', priority: 54, selected: false, conditionB: true, edit: false},
			{name: 'girth', dbName: 'girth', type: 'Numeric', priority: 42, selected: false, conditionN: '', edit: true},
			{name: 'Moebius ladder', dbName: 'is_moebius_ladder', type: 'Boolean', priority: 21, selected: false, conditionB: true, edit: false},
			{name: 'odd girth', dbName: 'odd_girth', type: 'Numeric', priority: 12, selected: false, conditionN: '', edit: true},
			{name: 'order', dbName: 'order', type: 'Numeric', priority: 1000, selected: false, conditionN: '', edit: true},
			{name: 'partial cube', dbName: 'is_partial_cube', type: 'Boolean', priority: 22, selected: false, conditionB: true, edit: false},
			{name: 'prism', dbName: 'is_prism', type: 'Boolean', priority: 23, selected: false, conditionB: true, edit: false},
			{name: 'split', dbName: 'is_split', type: 'Boolean', priority: 13, selected: false, conditionB: true, edit: false},
			{name: 'strongly regular', dbName: 'is_strongly_regular', type: 'Boolean', priority: 33, selected: false, conditionB: true, edit: false},
			{name: 'SPX', dbName: 'is_spx', type: 'Boolean', priority: 14, selected: false, conditionB: true, edit: false},
			{name: 'triangles count', dbName: 'triangles_count', type: 'Numeric', priority: 15, selected: false, conditionN: '', edit: true}
		]
		self.numericPropertyValidator = /^(=|==|<=|>=|<|>|<>|!=)\s*(\d+\.?\d*)$|^([\[\(])\s*(\d+\.?\d*),?\s*(\d+\.?\d*)\s*([\]\)])$|^((\d+\.?\d*,?\s*)+)$/

		console.log(propertiesToColumns())

		self.displayResults = false
		self.counter = 0
		self.zooGrid = {
			flatEntityAccess: true,
			infiniteScrollRowsFromEnd: 100,
    	infiniteScrollUp: false,
    	infiniteScrollDown: true,
			columnDefs: propertiesToColumns(),
			data: [],
			,
	    onRegisterApi: function(gridApi) {
	      gridApi.infiniteScroll.on.needLoadMoreData(self, self.getDataDown);
	      gridApi.infiniteScroll.on.needLoadMoreDataTop(self, self.getDataUp);
	      self.gridApi = gridApi;
	    }
		}

		$http.get(context + '/count?par=').success(function(data) { self.counter = data })
		$http.get(context + '/graphs?par=').success(function(data) { self.zooGrid.data = data; self.displayResults = true })

		self.propertyEditSwitch = function(property) {
			var index = self.properties.indexOf(property);
			if (index > -1) {
				property.edit = !property.edit
				self.properties[index] = property
			}
			updateCounter()
		}

		self.propertySelectSwitch = function(property) {
			var index = self.properties.indexOf(property);
			if (index > -1) {
				property.selected = !property.selected
				if (property.dbName == "odd_girth" && property.selected) {
					var bipartiteness = self.properties.filter(function (property) { return property.name == "bipartite" })
				}
				self.properties[index] = property
			}
			updateCounter()
		}

		self.submitSearch = function() {
			$http.get(context + '/graphs?par=' + constructParameterString()).success(function(data) {
				self.zooGrid.data = data
				self.zooGrid.columnDefs = propertiesToColumns()
				self.displayResults = true
			})
		}

		self.downloadURL = function() {
			return context + '/downloadPackage?par=' + constructParameterString()
		}

		function propertiesToColumns() {
			function modifiedPriority(property) {
				var modifier = 1
				if (property.selected) {
					if (property.type == 'Numeric' && property.conditionN[0] != '=') modifier = 10
					else modifier = 0
				}
				return modifier * property.priority
			}
			function visibility(property) {
				var priorities = self.properties.map(function(property) { return modifiedPriority(property) }).sort(function(a, b) {return a-b}).slice(-5)
				return modifiedPriority(property) > priorities[0]
			}
			return self.properties.map(function(property) { return { name: property.name, field: property.dbName, visible: visibility(property) } })
		}

		function updateCounter() {
			$http.get(context + '/count?par=' + constructParameterString()).success(function(data) { self.counter = data })
		}

		function constructParameterString() {
			var selectedProperties = self.properties.filter(function (property) { return property.selected && !property.edit })
			return selectedProperties.map(function(property) {
				if (property.type == "Boolean") return (property.conditionB == false ? "!" : "") + property.dbName
				else return property.dbName + ':' + (property.conditionN).replace("=", "*").replace(/\s+/, "")
			}).join(';')
		}

		$scope.getFirstData = function() {
	    var promise = $q.defer();
	    $http.get('/data/10000_complex.json')
	    .success(function(data) {
	      var newData = $scope.getPage(data, $scope.lastPage);
	      $scope.data = $scope.data.concat(newData);
	      promise.resolve();
	    });
	    return promise.promise;
	  };

	  $scope.getDataDown = function() {
	    var promise = $q.defer();
	    $http.get('/data/10000_complex.json')
	    .success(function(data) {
	      $scope.lastPage++;
	      var newData = $scope.getPage(data, $scope.lastPage);
	      $scope.gridApi.infiniteScroll.saveScrollPercentage();
	      $scope.data = $scope.data.concat(newData);
	      $scope.gridApi.infiniteScroll.dataLoaded($scope.firstPage > 0, $scope.lastPage < 4).then(function() {$scope.checkDataLength('up');}).then(function() {
	        promise.resolve();
	      });
	    })
	    .error(function(error) {
	      $scope.gridApi.infiniteScroll.dataLoaded();
	      promise.reject();
	    });
	    return promise.promise;
	  };

		$scope.checkDataLength = function( discardDirection) {
	    // work out whether we need to discard a page, if so discard from the direction passed in
	    if( $scope.lastPage - $scope.firstPage > 3 ){
	      // we want to remove a page
	      $scope.gridApi.infiniteScroll.saveScrollPercentage();

	      if( discardDirection === 'up' ){
	        $scope.data = $scope.data.slice(100);
	        $scope.firstPage++;
	        $timeout(function() {
	          // wait for grid to ingest data changes
	          $scope.gridApi.infiniteScroll.dataRemovedTop($scope.firstPage > 0, $scope.lastPage < 4);
	        });
	      } else {
	        $scope.data = $scope.data.slice(0, 400);
	        $scope.lastPage--;
	        $timeout(function() {
	          // wait for grid to ingest data changes
	          $scope.gridApi.infiniteScroll.dataRemovedBottom($scope.firstPage > 0, $scope.lastPage < 4);
	        });
	      }
	    }
	  };

	  $scope.reset = function() {
	    $scope.firstPage = 2;
	    $scope.lastPage = 2;

	    // turn off the infinite scroll handling up and down - hopefully this won't be needed after @swalters scrolling changes
	    $scope.gridApi.infiniteScroll.setScrollDirections( false, false );
	    $scope.data = [];

	    $scope.getFirstData().then(function(){
	      $timeout(function() {
	        // timeout needed to allow digest cycle to complete,and grid to finish ingesting the data
	        $scope.gridApi.infiniteScroll.resetScroll( $scope.firstPage > 0, $scope.lastPage < 4 );
	      });
	    });
	  };

	  $scope.getFirstData().then(function(){
	    $timeout(function() {
	      // timeout needed to allow digest cycle to complete,and grid to finish ingesting the data
	      // you need to call resetData once you've loaded your data if you want to enable scroll up,
	      // it adjusts the scroll position down one pixel so that we can generate scroll up events
	      $scope.gridApi.infiniteScroll.resetScroll( $scope.firstPage > 0, $scope.lastPage < 4 );
	    });
	  });
  }

})();
