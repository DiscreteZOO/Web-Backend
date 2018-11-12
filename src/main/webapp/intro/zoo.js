function startIntro() {
    var options = {
        'showStepNumbers': 'false',
        'showBullets': 'false',
        'nextLabel': "&rarr;",
        'prevLabel': "&larr;"
    }
    var intro = introJs().setOptions(options);
    intro.setOptions({
        steps: [
            {
                element: '#intro-control',
                intro: "Hide or show the introduction",
                position: 'bottom'
            },
            {
                element: '#select-type',
                intro: "Choose your favourite objects",
                position: 'right'
            },
            {
                element: '.type-info',
                intro: "More information",
                position: 'right'
            }
        ]
    });
    intro.start();
}