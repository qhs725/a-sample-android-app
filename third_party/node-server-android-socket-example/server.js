//var pg = require("pg")
var app = require("express") ();
var bodyparser = require("body-parser");
//var server = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var num = 0;
//server.use(bodyparser.json())
//server.use(bodyparser.urlencoded())

app.get('/', function(req, res){
	res.sendFile(__dirname + '/index.html');
});



io.on('connection', function(socket){
  console.log('a user connected');
  socket.on('disconnect', function(){
    console.log('user disconnected');
  });
});


io.on('connection', function(socket){
  socket.on('chat message', function(msg){
    console.log('message: ' + msg);
  });
});

io.on('connection', function(socket){
  socket.on('chat message', function(msg){
    io.emit('chat message', msg);
  });
});

/*
server.get("/datapoint", function(request, response) {

	num++;
	console.log(num + " GET requests has been made")
	response.status(200).send("GET, OK :D")
})


//var database = new pg.Client("postgres://postgres:postgres@localhost:5433/tangodb")
var database = new pg.Client("postgres://gdjpiomy:NzXMwAkaX2sCAZXn2FbZR6pePqg-JItc@sweet-feijoa.db.elephantsql.com:5432/gdjpiomy")
database.connect(function(error) {
	if(error) {
		console.log(error)
	} else {
		server.use(bodyparser.json())
		server.use(bodyparser.urlencoded())
		server.post("/accelpoint", function(request, response) {
			response.status(200).send("OK!")
			var data = request.body
			//console.log(num + " POST requests has been made - ")
			num++;

			//Retrieving Values
			//Time and misc data
			var tstamp = data.timestamp;
			var uuid = data.uuid;
			var nanoTime = parseFloat(data.nanoTime)
			var session_info = mysql_real_escape_string(data.session_info);
			//Accelerometer data
			var accelx = parseFloat(data.accelx);
			var accely = parseFloat(data.accely);
			var accelz = parseFloat(data.accelz);


			if(isNaN(accelx) || isNaN(accely) || isNaN(accelz) ){
				console.log("ERROR! NaN");
			}else{
				//console.log("Accel-working");
				//Send request to database
				database.query("INSERT INTO accelpoint ( tstmp, uuid, nanot, session_info,  ax, ay, az)"
				+ " VALUES ( \'" + tstamp + "\' , \'" + uuid + '\' ,' + nanoTime + ', \' ' + session_info + '\', ' + accelx + ', ' + accely + ', ' + accelz + ')');

				console.log(tstamp + ":A: "+ session_info + ", " + accelx + ", " + accely + ", " + accelz);
			}
			})

		server.use(bodyparser.json())
		server.use(bodyparser.urlencoded())
		server.post("/gyropoint", function(request, response) {
			response.status(200).send("OK!")
			var data = request.body
			//console.log(num + " POST requests has been made - ")
			num++;

			//Retrieving Values
			//Time and misc data
			var tstamp = data.timestamp;
			var uuid = data.uuid;
			var nanoTime = parseFloat(data.nanoTime)
			var session_info = mysql_real_escape_string(data.session_info);
			//Gyroscopic data
			var gyroX = parseFloat(data.gyroX);
			var gyroY = parseFloat(data.gyroY);
			var gyroZ = parseFloat(data.gyroZ);
			//Uncalibrated Gyro data
			//var gyroXd = parseFloat(data.gyroXd);
			//var gyroYd = parseFloat(data.gyroYd);
			//var gyroZd = parseFloat(data.gyroZd);

			if(isNaN(gyroX) || isNaN(gyroY) || isNaN(gyroZ) ){
				console.log("ERROR! NaN");
			}else{
				//console.log("Gyro-working");
				//Send request to database
				database.query("INSERT INTO gyropoint ( tstmp, uuid, nanot, session_info,  gyrox, gyroy, gyroz)"
				+ " VALUES ( \'" + tstamp + "\' , \'" + uuid + '\' ,' + nanoTime + ', \' ' + session_info + '\', ' + gyroX + ', ' + gyroY + ', ' + gyroZ + ')');

				console.log(tstamp + ":G: "+ session_info + ", " + gyroX + ", " + gyroY + ", " + gyroZ)
			}})
		}

	//fucntion to escape certain characters (edited to remove apostrophes from output to minimize server crashes)
	function mysql_real_escape_string (str) {
		return str.replace(/[\0\x08\x09\x1a\n\r"'\\\%]/g, function (char) {
			switch (char) {
				case "\0":
					return "\\0";
				case "\x08":
					return "\\b";
				case "\x09":
					return "\\t";
				case "\x1a":
					return "\\z";
				case "\n":
					return "\\n";
				case "\r":
					return "\\r";
				case "\"":
				case "'":
					return ""; //removes apostrophe
				case "\\":
				case "%":
					return "\\"+char; // prepends a backslash to backslash, percent,
									// and double/single quotes
			}
		});
	}
})

*/
http.listen(80, function() {
	console.log("Listening on 80")
})
