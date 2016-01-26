(function () {

  'use strict'

	angular
    .module("zoo", ['ui.bootstrap', 'ui.grid', 'ui.scroll'])
    .controller('ZooCtrl', ['$http', '$scope', ZooCtrl])

	function ZooCtrl($http) {
    var self = this;

    self.properties = [
			{name: 'bipartite', dbName: 'is_bipartite', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'Cayley', dbName: 'is_cayley', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'diameter', dbName: 'diameter', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'girth', dbName: 'girth', type: 'Numeric', selected: false, conditionN: '', edit: true},
			{name: 'Moebius ladder', dbName: 'is_moebius_ladder', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'odd girth', dbName: 'odd_girth', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'order', dbName: 'order', type: 'Numeric', selected: false, conditionB: true, edit: true},
			{name: 'prism', dbName: 'is_prism', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'SPX', dbName: 'is_spx', type: 'Boolean', selected: false, conditionB: true, edit: false}
		]
		self.numericPropertyValidator = /^(=|==|<=|>=|<|>|<>|!=)\s*(\d+\.?\d*)$|^([\[\(])\s*(\d+\.?\d*),?\s*(\d+\.?\d*)\s*([\]\)])$|^((\d+\.?\d*,?\s*)+)$/

// - clique_number
// - is_arc_transitive
// - is_distance_regular
// - is_distance_transitive
// - is_edge_transitive
// - is_eulerian
// - is_overfull
// - is_split
// - is_strongly_regular
// - is_vertex_transitive
// - size (število povezav)
// - triangles_count

		self.displayResults = false
		self.counter = 0
		self.zooGrid = {
			flatEntityAccess: true,
			data: []
		}

		$http.get('http://localhost:8080/graphs?par=').success(function(data) {
			self.zooGrid.data = data
			self.counter = data.length
			self.displayResults = true
		})

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
