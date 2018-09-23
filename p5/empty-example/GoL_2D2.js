
var output;
var cells;
var iterations;
var world_dimensions;
var cell_dimensions;
var time_checkpoint;
var current_iteration = 0;
var canvasX = 700;
var canvasY = 700;
var time_between_frames = 100;
var centersOfMass;
var radiuses;

function preload() {
    output = loadStrings('output.txt');
}

function setup() {

    iterations = output[0];

    world_dimensions = output[1].split(" ");
    world_dimensions = {x: parseInt(world_dimensions[0]), y: parseInt(world_dimensions[1]), z: parseInt(world_dimensions[2])};

    cell_dimensions = {x: canvasX/world_dimensions.x, y: canvasY/world_dimensions.y};
    var side = min(cell_dimensions.x, cell_dimensions.y);
    cell_dimensions = {x: side, y: side};

    createCanvas(side * world_dimensions.x, side * world_dimensions.y);

    cells = new Array(parseInt(iterations) + 1);
    centersOfMass = new Array(parseInt(iterations) + 1);
    radiuses = new Array(parseInt(iterations) + 1);

    for (var i = 0; i <= iterations; i++) {
        cells[i] = new Array(world_dimensions.z);
        centersOfMass[i] = {x: parseFloat(output[i * (world_dimensions.y+1) + 2].split(" ")[0]),
                            y: parseFloat(output[i * (world_dimensions.y+1) + 2].split(" ")[1]),
                            z: parseFloat(output[i * (world_dimensions.y+1) + 2].split(" ")[2])};
        radiuses[i] = parseFloat(output[i * (world_dimensions.y+1) + 2].split(" ")[3]);
        for (var z = 0; z < world_dimensions.z; z++) {
            cells[i][z] = new Array(world_dimensions.y);
            for (var y = 0; y < world_dimensions.y; y++) {
                cells[i][z][y] = new Array(world_dimensions.x);
                for (var x = 0; x < world_dimensions.x; x++) {
                    cells[i][z][y][x] = output[(z + i) * world_dimensions.y + y + i + 3].charAt(x)
                }
            }
        }
    }


    console.log(centersOfMass)
    console.log(radiuses)
    console.log(cells)

    time_checkpoint = millis();
}

function draw() {
    background(200);

    for (var y = 0; y < world_dimensions.y; y++) {
        for (var x = 0; x < world_dimensions.x; x++) {
            drawCell(x * cell_dimensions.x, y * cell_dimensions.y, cells[current_iteration][0][y][x]);
        }
    }
    drawRadius(centersOfMass[current_iteration].x, centersOfMass[current_iteration].y, radiuses[current_iteration]);

    if (millis() - time_checkpoint > time_between_frames) {
        current_iteration = (current_iteration + 1) % (parseInt(iterations) + 1);
        time_checkpoint = millis();
    }
}

function drawCell(x, y, state) {
    if (state == "O") {
        var c = color(255);
        fill(c);
        stroke(1);
        rect(x, y, cell_dimensions.x, cell_dimensions.y);
    }
}

function drawRadius(x, y, radius) {
    var c = color(0,0,0);
    fill(c);
    rect((x+0.5) * cell_dimensions.x - 2, (y+0.5) * cell_dimensions.x - 2, 4, 4)
    var c = color(255, 30, 30, 40);
    fill(c);
    noStroke();
    ellipse((x+0.5) * cell_dimensions.x, (y+0.5) * cell_dimensions.x, radius * 2 * cell_dimensions.x)
}