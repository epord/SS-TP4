
var file;
var time_checkpoint;
var frames;
var sun_positions = [];
var earth_positions = [];
var jupiter_positions = [];
var saturn_positions = [];
var voyager_positions = [];
var canvas_size = 800;
var world_size = 20;


function preload() {
    file = loadStrings('output.txt');
}

function setup() {
    frames = file[0];
    console.log(frames)
    createCanvas(canvas_size, canvas_size);

    sun_positions = readPlanetFromFile(1, 2);
    earth_positions = readPlanetFromFile(3, 4);
    jupiter_positions = readPlanetFromFile(5, 6);
    saturn_positions = readPlanetFromFile(7, 8);
    voyager_positions = readPlanetFromFile(9, 10);

    time_checkpoint = millis();
}

function readPlanetFromFile(line_number_x, line_number_y) {
    var arr = [];
    console.log(line_number_x)
    var arrX = file[line_number_x].split(" ");
    var arrY = file[line_number_y].split(" ");
    for (var i = 0; i < arrX.length; i++) {
        arr.push({x: parseFloat(arrX[i]), y: parseFloat(arrY[i])})
    }
    return arr;
}

var current_frame = 0;
var frame_skipping = 5; // amount of frames to skip
function draw() {
    background(200);

    drawPlanet(sun_positions[current_frame], {r: 232, g: 63, b: 25})
    drawPlanet(earth_positions[current_frame], {r: 50, g: 150, b: 230})
    drawPlanet(jupiter_positions[current_frame], {r: 219, g: 113, b: 52})
    drawPlanet(saturn_positions[current_frame], {r: 209, g: 149, b: 29})
    drawPlanet(voyager_positions[current_frame], {r: 120, g: 120, b: 120})

    current_frame = (current_frame + (1 + frame_skipping)) % frames;
}

function drawPlanet(position, planet_color) {
    var c = color(planet_color.r, planet_color.g, planet_color.b);
    fill(c);
    noStroke();
    ellipse(world2canvas(position.x), world2canvas(position.y), 10);
}

function world2canvas(value) {
    return (value / world_size) * canvas_size + canvas_size / 2.0;
}