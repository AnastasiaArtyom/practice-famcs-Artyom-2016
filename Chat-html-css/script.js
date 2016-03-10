var userName = "";
var messages = [];

function Message(userNick, mesText, id, photoURL, my) {
    this.my = my;
    this.nick = userNick;
    this.mesText = mesText;
    this.changed = false;
    this.del = false;
    this.photoURl = photoURL;
    this.id = id;
}

function login() {
    userName = document.getElementById('loginTextArea').value;
    document.getElementById('loginTextArea').value = "";
    messages = document.getElementsByClassName("message");
    alert('Hello ' + userName);
    
}

function getData(){
	var date = new Date();

	var options = {
	
	  year: 'numeric',
	  month: 'long',
	  day: 'numeric',
	  timezone: 'UTC',
	  hour: 'numeric',
	  minute: 'numeric',
	  second: 'numeric'
	};
	return date.toLocaleString("en-US", options);
}


function genMyMessage(message){
    var prefix1 = '<div class="message"'
    var prefix1a = '><div class="me"><img src="src/me.jpg"><div class = "myButtons"><button class = "deleteButton" onclick = deleteMessage(this)></button><button class = "editButton" onClick = editMessage(this)></button></div><div class="name"><span class="author">';
    var prefix2 = '</span></div><div class="message-text"><p>';
    var suffix1 = '</p><span class="msg-time">';
    var suffix2 = '</span></div></div></div>';
    var date = new Date();

	var options = {
	
	  year: 'numeric',
	  month: 'long',
	  day: 'numeric',
	  timezone: 'UTC',
	  hour: 'numeric',
	  minute: 'numeric',
	  second: 'numeric'
	};

    return prefix1 + prefix1a + userName + prefix2 + message + suffix1 + date.toLocaleString("en-US", options) + suffix2;
}

function send(){
    if (userName != "") {
        var message = document.getElementById('input-message').value;
	
        document.getElementById('input-message').value = "";
	var chatList = document.getElementById('chat-list');
        var newDiv = document.createElement('div');
        newDiv.className = 'message';
	//alert(genMyMessage(message));
        newDiv.innerHTML = genMyMessage(message);
	
        chatList.appendChild(newDiv);
	chatList.scrollTop = chatList.scrollHeight;

	
	
    }else{
        alert("You must login!!");
        document.getElementById('input-message').value = "";
    }
}

function deleteMessage(item){
	var message = item.parentNode.parentNode.parentNode;
	//alert(item);
	if(message.getElementsByClassName("me").length > 0 && userName != ""){
		 var date = new Date();

		var options = {
	
		  year: 'numeric',
		  month: 'long',
		  day: 'numeric',
		  timezone: 'UTC',
		  hour: 'numeric',
		  minute: 'numeric',
		  second: 'numeric'
		};

		var div = document.createElement('div');
		div.innerHTML = '<div class="message" id = "mes_id"><div class = "meRemoved"><img src="src/me.jpg"><div class="name"><span class="author">' + userName + '</span></div><div class="message-text" id = "message-text"><p>Message was removed.</p><span class="msg-time">' + date.toLocaleString("en-US", options) + '</span></div></div></div>';
		var newMessage = div.childNodes[0];
		message.parentNode.replaceChild(newMessage, message);
	}
	else{
		alert("FORBIDDEN!!");
	}
	
	
}

function editMessage(item){
	var message = item.parentNode.parentNode.parentNode;
	//alert(item);
	if(message.getElementsByClassName("me").length > 0 && userName != ""){
		
		 var date = new Date();

		var options = {
	
		  year: 'numeric',
		  month: 'long',
		  day: 'numeric',
		  timezone: 'UTC',
		  hour: 'numeric',
		  minute: 'numeric',
		  second: 'numeric'
		};
		
		messageText = prompt('Input new message', 'Enter the message');
		var div = document.createElement('div');
		div.innerHTML = '<div class="message" id = "mes_id"><div class = "me"><img src="src/me.jpg"><div class="name"><span class="author">' + userName + '</span></div><div class="message-text" id = "message-text"><p>' + messageText + '</p><span class="msg-time">' + 
'edited ' + date.toLocaleString("en-US", options) + '</span></div></div></div>';
		var newMessage = div.childNodes[0];
		message.parentNode.replaceChild(newMessage, message);
	}
	else{
		alert("FORBIDDEN!!");
	}
}
