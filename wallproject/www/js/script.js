var mArea;
var wallname;
var message, wall, wallcontent; // defined only after the document is loaded
var msgNum;
var delWall;
var wallRefresh;
var wallctr;

function loadAndStart() {
  delWall = document.getElementById("idDeleteWall");
  mArea = document.getElementById("wallcontent");
  message = document.getElementById("usermsg");
  msgNum = document.getElementById("msgid");
  wall = document.getElementById("idwall");
  wallcontent = document.getElementById("wallcontent");
  wallctr = document.getElementById("wallcounter");
  setTimeout(updateWall, 1000);
}

function updateWall() {
  
  var request = new XMLHttpRequest();
  mArea.style.color = "black";
  wallRefresh = wall.value;
  console.log(wallRefresh);
  request.onload = function() {
    mArea.innerHTML = this.responseText;
    mArea.scrollTop = mArea.scrollHeight; // scroll the textarea to make last lines visible
    setTimeout(updateWall, 1000);
  };

  request.onerror = function() {
    nextMsg = 0;
    mArea.value = "Server not responding.";
    mArea.style.color = "red";
    setTimeout(updateWall, 1000);
  };

  request.ontimeout = function() {
    nextMsg = 0;
    mArea.value = "Server not responding.";
    mArea.style.color = "red";
    setTimeout(updateWall, 1000);
  };

  request.open("GET", "/walls/" + wall.value, true);
  request.timeout = 10000;
  // Message 0 is a server's greeting, it should always exist
  // no timeout, for following messages, the server responds only when the requested
  // message number exists
  if (wall.value !== "") {
    request.send();
  }else{
      mArea.value = "";
  }


}

function sendMessageToWall() {
  var request = new XMLHttpRequest();
  request.open("POST", "/walls/" + wallRefresh+ "/" + message.value, true);
  if (message.value !== "") {
    request.send();
    updateWall();
  }
  message.value = "";
}

function deleteMessageFromWall() {
  var request = new XMLHttpRequest();
  request.open(
    "DELETE",
    "/walls/" + wall.value + "/" + message.value + "/" + msgNum.value,
    true
  );
  if (msgNum.value !== "") {
    request.send();
    updateWall();
  }
  msgNum.value = "";
}

function deleteWall() {
  var request = new XMLHttpRequest();
  wall.value="";
  request.open("DELETE", "/walls/" + delWall.value, true);
  if (delWall.value !== "") {
    request.send();
    updateWall();
  }
  delWall.value = "";
}

function wallCounter() {
	var request = new XMLHttpRequest();
	request.open("GET", "/wallscounter/");
	request.onload = function() {
		wallctr.innerHTML = this.responseText;
        setTimeout(wallCounter, 1000);
	}
	request.send();
	
}
	