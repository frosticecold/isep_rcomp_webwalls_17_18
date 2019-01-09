var font;
var vehicles = [];

function preload() {
  font = loadFont('js/Effects/AvenirNextLTPro-Demi.otf');
}

function setup() {
  var canvas  = createCanvas(400, 200);
    canvas.parent('sketch-holder');
  //background(51);
  // textFont(font);
  // textSize(192);
  // fill(255);
  // noStroke();
  // text('train', 100, 200);

  var points = font.textToPoints('Chat', 80, 130, 102, {
    sampleFactor: 0.15
  });

  for (var i = 0; i < points.length; i++) {
    var pt = points[i];
    var vehicle = new Vehicle(pt.x, pt.y);
    vehicles.push(vehicle);
    // stroke(255);
    // strokeWeight(8);
    // point(pt.x, pt.y);
  }
}

function draw() {
  background(255);
  for (var i = 0; i < vehicles.length; i++) {
    var v = vehicles[i];
    v.behaviors();
    v.update();
    v.show();
  }
}