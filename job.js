var schedule = require('node-schedule');
var cp = require('child_process') 

var j = schedule.scheduleJob(
    process.env.INTERVAL, 
    function(){
	console.log("Starting: ", process.env.COMMAND);
	var pid = cp.spawn(
	    process.env.COMMAND,
	    [],
	    {
		env:process.env
	    }
	)
	pid.on("close", function() {
	    console.log("Done.")
	})
    }
);

