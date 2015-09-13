// UTC Virtual Athletic Trainer server for passing data 
// from mobile device to a database
// 9.12.15 --> WORKING, BUT NOT POSTING TO DB
// 09/02/83

var pg = require("pg")
var bodyparser = require("body-parser")
var fs = require('fs');
var http = require('http');
var count = 0;

var database = new pg.Client("postgres://postgres:postgres@localhost:5432/postgres");

var server = http.createServer(function (req, res) {
  if (req.method == 'POST') {
      //console.log("POST");
      var body = '';
      req.on('data', function (data) {
          body += data;
      });
      req.on('end', function () {
          //console.log(body);
          var dat = body.split(",");
          dat = dat.map(function (val) { return val; });
          //console.log(dat.pop() + "," + dat.pop());
          var ct = dat.pop();
          var cz = dat.pop();
          var cy = dat.pop();
          var cx = dat.pop();
          var gt = dat.pop();
          var gz = dat.pop();
          var gy = dat.pop();
          var gx = dat.pop();
          var at = dat.pop();
          var az = dat.pop();
          var ay = dat.pop();
          var ax = dat.pop();
          var no = dat.pop();
          console.log("Working, sorta, no db --> ax = " + ax + " and no = " + no);
      });
      res.statusCode = 200;
      res.end();
    }
    else {
        console.log("GET");        
    };
});


          /* TODO: GET THE NODE SERVER POSTING TO THE DB
          database.connect(function(error) {
            if(error) {
              console.log(error)
            } 
              try {
                database.query("INSERT INTO dat (no, ax, ay, az, at, gx, gy, gz, gt, cx, cy, cz, ct)"
                                 + " VALUES ( \'" + no + "\', \'" + ax + "\', \'" + ay + "\', \'" + az + "\', \'" + at + "\', \'" + 
                                             + gx + "\', \'" + gy + "\', \'" + gz + "\', \'" + gt + "\', \'" + cx + "\', \'" +
                                             + cy + "\', \'" + cz + "\', \'" + ct + "\' )");
              }
              catch (err) {
                console.log("adat error: " + err);
              }
          }); */


server.listen(8080, function() {
  console.log("listening on 8080")
})


