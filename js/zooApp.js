(function () {

  'use strict'

	angular
      .module("zoo", ['ui.bootstrap', 'ui.grid', 'ui.scroll'])
      .controller('ZooCtrl', ZooCtrl)

	function ZooCtrl ($timeout, $q) {
    var self = this;

    self.properties = [
			{name: 'bipartite', dbName: 'is_bipartite', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'Cayley', dbName: 'is_cayley', type: 'Numeric', selected: false, conditionB: true, edit: false},
			{name: 'diameter', dbName: 'diameter', type: 'Numeric', selected: false, conditionB: true, edit: false},
			{name: 'girth', dbName: 'girth', type: 'Numeric', selected: false, conditionN: '', edit: false},
			{name: 'Moebius ladder', dbName: 'is_moebius_ladder', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'odd girth', dbName: 'odd_girth', type: 'Numeric', selected: false, conditionB: true, edit: false},
			{name: 'order', dbName: 'order', type: 'Numeric', selected: false, conditionB: true, edit: false},
			{name: 'prism', dbName: 'is_prism', type: 'Boolean', selected: false, conditionB: true, edit: false},
			{name: 'size', dbName: 'size', type: 'Numeric', selected: false, conditionN: '', edit: false}, // #edges
			{name: 'SPX', dbName: 'is_spx', type: 'Boolean', selected: false, conditionB: true, edit: false}
		]
		self.numericPropertyValidator = /^(=|==|<=|>=|<|>)\s*(\d+\.?\d*)$|^([\[\(])\s*(\d+\.?\d*),?\s*(\d+\.?\d*)\s*([\]\)])$|^((\d+\.?\d*,?\s*)+)$/

		self.propertyEditSwitch = function(property) {
			var index = self.properties.indexOf(property);
			if (index > -1) {
				property.edit = !property.edit
				self.properties[index] = property
			}
		}

		self.propertySelectSwitch = function(property) {
			var index = self.properties.indexOf(property);
			if (index > -1) {
				property.selected = !property.selected
				self.properties[index] = property
			}
		}

		self.submitSearch = function() {
			var selectedProperties = self.properties.filter(function (property) { return property.selected });
			console.log(selectedProperties)
		}
  }

})();
