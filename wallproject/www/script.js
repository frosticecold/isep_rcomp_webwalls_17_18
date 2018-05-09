var mArea;
var wallname;
var message, wall, wallcontent; // defined only after the document is loaded
var msgNum;

function loadAndStart() {
  mArea = document.getElementById("wallcontent");
  message = document.getElementById("usermsg");
  msgNum = document.getElementById("msgid");
  wall = document.getElementById("idwall");
  wallcontent = document.getElementById("wallcontent");
  setTimeout(updateWall, 1000);
}

function updateWall() {
  var request = new XMLHttpRequest();
  mArea.style.color = "black";

  request.onload = function() {
    mArea.value = this.responseText;
    mArea.scrollTop = mArea.scrollHeight; // scroll the textarea to make last lines visible
    setTimeout(updateWall, 100);
  };

  request.onerror = function() {
    nextMsg = 0;
    mArea.value = "Server not responding.";
    mArea.style.color = "red";
    setTimeout(updateWall, 100);
  };

  request.ontimeout = function() {
    nextMsg = 0;
    mArea.value = "Server not responding.";
    mArea.style.color = "red";
    setTimeout(updateWall, 100);
  };

  request.open("GET", "/walls/" + wall.value, true);
  request.timeout = 100;
  // Message 0 is a server's greeting, it should always exist
  // no timeout, for following messages, the server responds only when the requested
  // message number exists
  request.send();
}

function sendMessageToWall() {
  var request = new XMLHttpRequest();
  request.open("POST", "/walls/" + wall.value + "/" + message.value, true);
  request.send();
  updateWall();
}

function deleteMessageFromWall() {
  var request = new XMLHttpRequest();
  request.open("DELETE","/walls/" + wall.value + "/" + message.value + "/" + msgNum.value,true);
  request.send();
  updateWall();
}
