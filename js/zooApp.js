(function () {

  'use strict'

	angular
    .module("zoo", ['ui.bootstrap', 'ui.grid', 'ui.scroll'])
    .controller('ZooCtrl', ['$http', '$scope', ZooCtrl])

	function ZooCtrl($http) {
    var self = this;

    self.properties = [
			{name: 'arc transitive', dbName: 'is_arc_transitive', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'bipartite', dbName: 'is_bipartite', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'Cayley', dbName: 'is_cayley', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'clique number', dbName: 'clique_number', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'diameter', dbName: 'diameter', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'distance regular', dbName: 'is_distance_regular', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'distance transitive', dbName: 'is_distance_transitive', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'edge transitive', dbName: 'is_edge_transitive', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'Eulerian', dbName: 'is_eulerian', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'girth', dbName: 'girth', type: 'Numeric', selected: false, conditionN: '', edit: true},
			{name: 'Moebius ladder', dbName: 'is_moebius_ladder', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'odd girth', dbName: 'odd_girth', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'order', dbName: 'order', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'partial cube', dbName: 'is_partial_cube', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'prism', dbName: 'is_prism', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'split', dbName: 'is_split', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'strongly regular', dbName: 'is_strongly_regular', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'SPX', dbName: 'is_spx', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'triangles count', dbName: 'triangles_count', type: 'Numeric', selected: false, conditionB: true, edit: true}
		]
		self.numericPropertyValidator = /^(=|==|<=|>=|<|>|<>|!=)\s*(\d+\.?\d*)$|^([\[\(])\s*(\d+\.?\d*),?\s*(\d+\.?\d*)\s*([\]\)])$|^((\d+\.?\d*,?\s*)+)$/

		self.displayResults = false
		self.counter = 0
		self.zooGrid = {
			flatEntityAccess: true,
			data: []
		}

		$http.get('http://localhost:8080/count?par=').success(function(data) { self.counter = data })
		$http.get('http://localhost:8080/graphs?par=').success(function(data) { self.zooGrid.data = data; self.displayResults = true })

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
				self.properties[index] = property
			}
			updateCounter()
		}

		self.submitSearch = function() {
			$http.get('http://localhost:8080/graphs?par=' + constructParameterString()).success(function(data) {
				self.zooGrid.data = data
				self.displayResults = true
			})
		}

		function updateCounter() {
			$http.get('http://localhost:8080/count?par=' + constructParameterString()).success(function(data) { self.counter = data })
		}

		function constructParameterString() {
			var selectedProperties = self.properties.filter(function (property) { return property.selected && !property.edit })
			return selectedProperties.map(function(property) {
				if (property.type == "Boolean") return (property.conditionB == false ? "!" : "") + property.dbName
				else return property.dbName + ':' + (property.conditionN).replace("=", "*").replace(/\s+/, "")
			}).join(';')
		}
  }

})();
