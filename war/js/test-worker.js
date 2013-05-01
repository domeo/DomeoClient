self.addEventListener('message', function(e) {
    var counter = 0;
	for(var i=0; i<100000000; i++) {
    	counter++;
    }
	self.postMessage(e.data);
}, false);