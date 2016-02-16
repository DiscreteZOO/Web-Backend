(function () {

  'use strict'

	angular
    .module("zoo", ['ngTouch','ui.bootstrap', 'ui.grid', 'ui.grid.infiniteScroll', 'ui.scroll'])
    .controller('ZooCtrl', ['$http', '$scope', '$q', ZooCtrl])
		.filter('nullFilter', function () { return function (value) { return ( value == 0) ? '' : value } })
		.filter('truncationFilter', function () { return function (value) { return ( value == 0) ? 'false' : 'true' } })

	function ZooCtrl($http, $scope, $q) {

		var context = ""

		$scope.censuses = [
			{ name: 'VT index', dbName: 'vt_index', longName: 'vertex transitive graphs (up to 31 vertices)', selected: true,
				description: 'by Gordon Royle',
			 	link: 'Faculty of Mathematics, Natural Sciences and Information Technologies' },
			{ name: 'CVT index', dbName: 'cvt_index', longName: 'cubic vertex transitive graphs (up to 1280 vertices)', selected: true,
		 		description: 'by Primož Potočnik, Pablo Spiga and Gabriel Verret',
			 	link: 'http://www.matapp.unimib.it/~spiga/census.html' },
			{ name: 'symcubic index', dbName: 'symcubic_index', longName: 'cubic arc transitive graphs (up to 2048 vertices)', selected: true,
		 		description: 'by Marston Conder',
			 	link: 'https://www.math.auckland.ac.nz/~conder/symmcubic2048list.txt' }
		]

    $scope.properties = [
			{name: 'arc transitive', dbName: 'is_arc_transitive', type: 'Boolean', priority: 51, hasNull: false, selected: true, conditionB: true, edit: false},
			{name: 'bipartite', dbName: 'is_bipartite', type: 'Boolean', priority: 52, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'Cayley', dbName: 'is_cayley', type: 'Boolean', priority: 53, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'clique number', dbName: 'clique_number', type: 'Numeric', priority: 11, hasNull: false, selected: false, conditionN: '', edit: true},
			{name: 'degree', dbName: 'degree', type: 'Numeric', priority: 1000, hasNull: false, selected: false, conditionN: '', edit: true},
			{name: 'diameter', dbName: 'diameter', type: 'Numeric', priority: 41, hasNull: true, selected: false, conditionN: '', edit: true},
			{name: 'distance regular', dbName: 'is_distance_regular', type: 'Boolean', priority: 31, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'distance transitive', dbName: 'is_distance_transitive', type: 'Boolean', priority: 32, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'edge transitive', dbName: 'is_edge_transitive', type: 'Boolean', priority: 54, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'girth', dbName: 'girth', type: 'Numeric', priority: 42, hasNull: true, selected: false, conditionN: '', edit: true},
			{name: 'hamiltonian', dbName: 'is_hamiltonian', type: 'Boolean', priority: 33, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'Moebius ladder', dbName: 'is_moebius_ladder', type: 'Boolean', priority: 21, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'odd girth', dbName: 'odd_girth', type: 'Numeric', priority: 12, hasNull: true, selected: false, conditionN: '', edit: true},
			{name: 'order', dbName: 'order', type: 'Numeric', priority: 1001, hasNull: false, selected: true, conditionN: '<=100', edit: false},
			{name: 'overfull', dbName: 'is_overfull', type: 'Boolean', priority: 13, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'partial cube', dbName: 'is_partial_cube', type: 'Boolean', priority: 22, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'prism', dbName: 'is_prism', type: 'Boolean', priority: 23, hasNull: true, selected: false, conditionB: true, edit: false},
			{name: 'split', dbName: 'is_split', type: 'Boolean', priority: 14, hasNull: true, selected: false, conditionB: true, edit: false},
			{name: 'strongly regular', dbName: 'is_strongly_regular', type: 'Boolean', priority: 34, hasNull: false, selected: false, conditionB: true, edit: false},
			{name: 'SPX', dbName: 'is_spx', type: 'Boolean', priority: 15, hasNull: true, selected: false, conditionB: true, edit: false},
			{name: 'triangles count', dbName: 'triangles_count', type: 'Numeric', priority: 16, hasNull: false, selected: false, conditionN: '', edit: true},
			// {name: 'truncation', dbName: 'truncation', type: 'Boolean', priority: 24, hasNull: true, selected: false, conditionB: true, edit: true}
		]
		$scope.numericPropertyValidator = /^(=|==|<=|>=|<|>|<>|!=)\s*(\d+\.?\d*)$|^([\[\(])\s*(\d+\.?\d*),?\s*(\d+\.?\d*)\s*([\]\)])$|^((\d+\.?\d*,?\s*)+)$/

		$scope.displayResults = false
		$scope.counter = 0
		$scope.zooGrid = {
			flatEntityAccess: true,
			infiniteScrollRowsFromEnd: 100,
    	infiniteScrollUp: false,
    	infiniteScrollDown: true,
			columnDefs: propertiesToColumns(),
			data: [],
	    onRegisterApi: function(gridApi) {
	      gridApi.infiniteScroll.on.needLoadMoreData($scope, $scope.getDataDown);
	      gridApi.infiniteScroll.on.needLoadMoreDataTop($scope, $scope.getDataUp);
	      $scope.gridApi = gridApi;
	    }
		}

		$scope.propertyEditSwitch = function(property) {
			var index = $scope.properties.indexOf(property);
			if (index > -1) {
				property.edit = !property.edit
				$scope.properties[index] = property
			}
			updateCounter()
		}

		$scope.propertySelectSwitch = function(property) {
			var index = $scope.properties.indexOf(property);
			if (index > -1) {
				property.selected = !property.selected
				if (property.dbName == "odd_girth" && property.selected) {
					var bipartiteness = $scope.properties.filter(function (property) { return property.name == "bipartite" })
				}
				$scope.properties[index] = property
			}
			updateCounter()
		}

		$scope.submitSearch = function() {
			$http.get(context + '/graphs?par=' + constructParameterString()).success(function(data) {
				$scope.zooGrid.data = data
				$scope.zooGrid.columnDefs = propertiesToColumns()
				$scope.displayResults = true
			})
		}

		$scope.downloadURL = function(format) { return context + '/' + format + '?par=' + constructParameterString() }

		$scope.showColumn = function(column) {
			var index = $scope.zooGrid.columnDefs.indexOf(column);
			if (index > -1) { column.visible = true }
			$scope.gridApi.core.queueGridRefresh()
		}

		$scope.classDbSelected = function(census) {
			var index = $scope.censuses.indexOf(census);
			if (index > -1) {
				if (census.selected) return 'db-selected'
				else return 'db-deselected'
			}
		}

		$scope.switchDbSelected = function(census) {
			var i = $scope.censuses.indexOf(census);
			if (i > -1) { census.selected = !census.selected }
			updateCounter()
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
				var priorities = $scope.properties.map(function(property) { return modifiedPriority(property) }).sort(function(a, b) {return a-b}).slice(-5)
				return modifiedPriority(property) > priorities[0]
			}

			var propertyColumns = $scope.properties.map(function(property) {
				var column = { name: property.name, field: property.dbName, visible: visibility(property) }
				if (property.hasNull) column.cellFilter = 'nullFilter'
				if (property.name == 'truncation') column.cellFilter = 'truncationFilter'
				return column
			})
			var censusColumns = $scope.censuses.map(function(catalog) { return { name: catalog.name, field: catalog.dbName, visible: catalog.selected, cellFilter: 'nullFilter' } })

			return [{ name: 'name', field: 'name', visible: true }].concat(censusColumns.concat(propertyColumns))
		}

		function updateCounter() { $http.get(context + '/count?par=' + constructParameterString()).success(function(data) { $scope.counter = data }) }

		function constructParameterString() {
			var selectedProperties = $scope.properties.filter(function (property) { return property.selected && !property.edit })
			var selectedCensuses = $scope.censuses.filter(function (census) { return census.selected })
			var resultP = selectedProperties.map(function(property) {
				if (property.type == "Boolean") return (property.conditionB == false ? "!" : "") + property.dbName
				else return property.dbName + ':' + (property.conditionN).replace("=", "*").replace(/\s+/, "")
			}).join(';')
			var resultC = selectedCensuses.map(function(census) { return census.dbName }).join(';')
			return resultP + (resultP.length > 0 && resultC.length > 0 ? ';' : '') + resultC
		}

		$scope.getFirstData = function() {
	    var promise = $q.defer()
	    $http.get(context + '/graphs?par=').success(function(data) {
	      // var newData = $scope.getPage(data, $scope.lastPage)
	      // $scope.data = $scope.data.concat(newData)
	      // promise.resolve()
	    })
	    return promise.promise
	  };

	  $scope.getDataDown = function() {
	    var promise = $q.defer()
	    $http.get(context + '/graphs?par=')
			.success(function(data) {
	      $scope.lastPage++
	      var newData = $scope.getPage(data, $scope.lastPage)
	      $scope.gridApi.infiniteScroll.saveScrollPercentage()
	      $scope.data = $scope.data.concat(newData)
	      $scope.gridApi.infiniteScroll.dataLoaded($scope.firstPage > 0, $scope.lastPage < 4).then(function() {$scope.checkDataLength('up');}).then(function() { promise.resolve() })
	    })
			.error(function(error) {
	      $scope.gridApi.infiniteScroll.dataLoaded()
	      promise.reject()
	    })
	    return promise.promise
	  }

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

		// initialization
		updateCounter()
		$scope.submitSearch()

  } // end ZooCtrl

})()
