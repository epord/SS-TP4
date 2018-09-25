
var file;
var time_checkpoint;
var frames;
var sun_positions = [];
var earth_positions = [];
var jupiter_positions = [];
var saturn_positions = [];
var voyager_positions = [];
var canvas_size = 800;
var world_size = 30;


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
var frame_skipping = 0; // amount of frames to skip
function draw() {
    background(24);

    stroke('rgba(255, 255, 255, 0.05)');
    noFill();
    ellipse(world2canvas(0), world2canvas(0), 2.0/world_size * canvas_size);
    ellipse(world2canvas(0), world2canvas(0), 2 * 5.228/world_size * canvas_size);
    ellipse(world2canvas(0), world2canvas(0), 2 * 9.553/world_size * canvas_size);

    noStroke();
    drawPlanet(sun_positions[current_frame], {r: 232, g: 63, b: 25}, 10);
    drawPlanet(earth_positions[current_frame], {r: 50, g: 150, b: 230}, 10);
    drawPlanet(jupiter_positions[current_frame], {r: 219, g: 113, b: 52}, 10);
    drawPlanet(saturn_positions[current_frame], {r: 209, g: 149, b: 29}, 10);
    drawPlanet(voyager_positions[current_frame], {r: 210, g: 210, b: 210}, 5);

    // drawPlanet(sun_positions[0], {r: 232, g: 63, b: 25}, 10);
    // drawPlanet(earth_positions[0], {r: 50, g: 150, b: 230}, 10);
    // drawPlanet(jupiter_positions[0], {r: 219, g: 113, b: 52}, 10);
    // drawPlanet(saturn_positions[0], {r: 209, g: 149, b: 29}, 10);
    // drawPlanet(voyager_positions[0], {r: 210, g: 210, b: 210}, 5);

    // stroke(255);
    // au2m = 149597870700.0;
    // drawPlanet({x: -1.3583948591909668E11/au2m, y: -8.066860152758499E10/au2m}, {r: 50, g: 150, b: 230}, 10);
    // drawPlanet({x: -1.3583948591909668E11/au2m, y: -8.066860152758499E10/au2m}, {r: 210, g: 210, b: 210}, 5);
    // drawPlanet({x:4.5907282659488403E11/au2m, y:-5.98892036361665E11/au2m}, {r: 219, g: 113, b: 52}, 10);
    // drawPlanet({x:1.3670071918156335E12/au2m, y:2.959295091467507E11/au2m}, {r: 209, g: 149, b: 29}, 10);

    current_frame = (current_frame + (1 + frame_skipping)) % frames;
}

function drawPlanet(position, planet_color, radius) {
    var c = color(planet_color.r, planet_color.g, planet_color.b);
    fill(c);
    ellipse(world2canvas(position.x), world2canvas(-position.y), radius);
}

function world2canvas(value) {
    return (value / world_size) * canvas_size + canvas_size / 2.0;
}