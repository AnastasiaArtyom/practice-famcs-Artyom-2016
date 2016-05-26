var userName = "";
var messages = [];
var isConnected = void 0;
var token = 'TN11EN';
var URL = 'http://127.0.0.1:12345/chat';
var chatList = [
    newChatMember('me', 'src/me.jpg'),
    newChatMember('smb2', 'src/somebodyPhoto2.jpg'),
    newChatMember('smb1', 'src/somebodyPhoto1.jpg'),
    newChatMember('indefied', 'src/default.jpg')
];
var userPhoto;

function run() {
    //localStorage.clear();
    userPhoto = findUserPhoto(userName);
    userName = loadUserName() || '';
    var myEvent = document.getElementsByClassName('main')[0];

    myEvent.addEventListener('click', delegateEvent);
    myEvent.addEventListener("keydown", delegateEvent);
    messages = loadMessages() || [];

    document.getElementById('loginTextArea').value = userName;
    Connect();
}


function Connect() {
    saveUserName();
    if (isConnected) {
        return;
    }
    ajax('URL', mainUrl + '?token=' + token, null, function (responseText) {
        if (isConnected) {
            var new_msg = JSON.parse(responseText).messages;
            token = JSON.parse(responseText).token;
            updatePage(new_msg);
        }
    });
    function whileConnected() {
        isConnected = setTimeout(function () {
            ajax('GET', URL + '?token=' + token, null, function (responseText) {
                if (isConnected) {
                    var new_msg = JSON.parse(responseText).messages;
                    token = JSON.parse(responseText).token;
                    updatePage(new_msg);
                    whileConnected();
                }
            });
        }, seconds(1));
    }

    whileConnected();
}


function Message(userName, msg_text, id) {
    this.method;
    this.author = userName;
    this.text = msg_text;
    this.id = id;
    this.isDeleted = false;
    this.isEdited = false;
}

function updatePage(newMessagesArray) {
    for (var index = 0; index < newMessagesArray.length; ++index) {
        if (newMessagesArray[index].method === 'POST') {
            messages.push(newMes[i]);
        }
        else if (newMessagesArray[index].method === 'PUT') {
            for (var i = 0; i < messages.length; ++i) {
                if (messages[i].id === newMessagesArray[index].id) {
                    messages[i].method = 'PUT';
                    messages[i].text = newMessagesArray[i].text;
                    messages[index].time = newMessagesArray[index].time;
                    messages[index].author = newMessagesArray[index].author;
        	    messages[index].isDeleted = newMessagesArray[index].isDeleted;
       		    messages[index].isEdited = newMessagesArray[index].isEdited;
                }
            }
        }
        else {
            for (var j = 0; j < messages.length; ++j) {
                if (messages[j].id === newMessagesArray[index].id) {
                    messages[j].method = 'DELETE';
                    messages[j].text = '';
                }

            }
        }

    }
}


function loadMessages() {
    if (typeof(localStorage) == 'undefined') {
        alert('pechal\'ka');
        return;
    }

   return JSON.parse(localStorage.getItem('allMessages'));
;
}

function generateUUID() {
    var d = new Date().getTime();
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

function loadUserName() {
    if (typeof(localStorage) == 'undefined') {
        alert('pechal\'ka');
        return;
    }
    return localStorage.getItem("userName");
}

function findUserPhoto(userName) {
    for (var index = 0; index < chatList.length; ++index) {
        if (chatList[index].name == userName) {
            return chatList[index].photo;
        }

    }
    return chatList[chatList.length - 1].photo;
}

function renderMessage(message) {

    var newMessageDiv = document.createElement('div');
    newMessageDiv.className = 'message';
    newMessageDiv.id = message.id;

    var authorDiv = document.createElement('div');


    if (message.author == userName) {
        if (message.isDeleted == true) {
            authorDiv.className = 'meRemoved';
        } else {
            authorDiv.className = 'me';
        }

    } else {
        if (message.isDeleted == true) {
            authorDiv.className = 'removed';
        } else {
            authorDiv.className = 'others';
        }
    }
    var authorImage = document.createElement('img');
    authorImage.src = findUserPhoto(message.author);
    if (message.isDeleted != true) {

        var buttonsDiv = document.createElement('div');
        buttonsDiv.className = 'myButtons';

        var deleteButton = document.createElement('button');
        deleteButton.className = 'deleteButton';
        deleteButton.type = 'button';

        var editButton = document.createElement('button');
        editButton.className = 'editButton';
        editButton.type = 'button';
    }

    var authorName = document.createElement('span');
    authorName.className = 'author';
    authorName.appendChild(document.createTextNode(message.author));

    var messageTextDiv = document.createElement('div');
    messageTextDiv.className = 'message-text';

    var text = document.createElement('p');
    text.className = 'msg';
    text.appendChild(document.createTextNode(message.messageText));


    var time = document.createElement('span');
    time.className = 'msg-time';
    var currentTime = getCurrentData();
    var editable = '';
    if(message.isEdited == true){
        editable = 'edited ';
    }
    time.appendChild(document.createTextNode(editable + message.time));

    if(message.isDeleted != true) {
        buttonsDiv.appendChild(deleteButton);
        buttonsDiv.appendChild(editButton);
    }

    messageTextDiv.appendChild(text);
    messageTextDiv.appendChild(time);
    authorDiv.appendChild(authorImage);
    if(message.isDeleted != true) {
        authorDiv.appendChild(buttonsDiv);
    }

    authorDiv.appendChild(authorName);
    authorDiv.appendChild(messageTextDiv);
    newMessageDiv.appendChild(authorDiv);

    return newMessageDiv;
}


function newChatMember(name, photo) {
    return {
        name: name,
        photo: photo
    };
}

function render() {
    if (typeof(localStorage) == 'undefined') {
        alert('pechal\'ka');
        return;
    }
    var chatList = document.getElementById('chat-list');

    for (var index = 0; index < messages.length; ++index) {
        chatList.appendChild(renderMessage(messages[index]));

    }
    chatList.scrollTop = chatList.scrollHeight;
}

function saveMessages() {
    if (typeof(localStorage) == 'undefined') {
        alert('pechal\'ka');
        return;
    }

    localStorage.setItem('allMessages', JSON.stringify(messages));
}

function setUserName() {
    userName = document.getElementById('loginTextArea').value;
    saveUserName();
}

function saveUserName() {
    if (typeof(localStorage) == 'undefined') {
        alert('pechal\'ka');
        return;
    }

    localStorage.setItem('userName', userName);
}

function delegateEvent(myEvent) {
    if (myEvent.type === 'click') {
        if (myEvent.target.id == 'loginButton') {
            login(myEvent);
        }

        if (myEvent.target.classList.contains('deleteButton')) {
            deleteMessage(myEvent);
        }
        if (myEvent.target.classList.contains('editButton')) {
            editMessage(myEvent);
        }
        if (myEvent.target.id == 'sendButton') {
            send(myEvent);
        }
    } else if (myEvent.type == 'keydown') {
        if (myEvent.keyCode === 13 && myEvent.target.id == 'loginTextArea') {
            login(myEvent);
        }
        if (myEvent.shiftKey && myEvent.keyCode == 13 && myEvent.target.id == 'input-message') {
            send();

        }
    }
}

function redrawMessages(){
 var msg = document.getElementsByClassName('message');
    for(var index = 0; index < msg.length; ++index){
        if(msg[index].childNodes[0].childNodes[2].innerHTML == userName){
            if(msg[index].childNodes[0].className == 'meRemoved' ||  msg[index].childNodes[0].className == 'removed') {
                //msg[index].childNodes[0].className = 'me';
                msg[index].childNodes[0].className = 'meRemoved';
            }
            else {
                msg[index].childNodes[0].className = 'me';
            }
        }
        else{
            if(msg[index].childNodes[0].className == 'removed' ||  msg[index].childNodes[0].className == 'meRemoved') {
                //msg[index].childNodes[0].className = 'others';
                msg[index].childNodes[0].className = 'removed';
            }
            else {
                msg[index].childNodes[0].className = 'others';
            }
        }

    }
}

function login() {
    userName = document.getElementById('loginTextArea').value;
    document.getElementById('loginTextArea').innerHTML = "";
    window.alert('Hello ' + userName);
    userPhoto = findUserPhoto(userName);
    saveUserName();
    redrawMessages();
    //render();
}

function getCurrentData() {
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
function createMessage(messageText, id) {
    var newMessageDiv = document.createElement('div');
    newMessageDiv.className = 'message';
    newMessageDiv.id = id;

    var authorDiv = document.createElement('div');
    authorDiv.className = 'me';

    var authorImage = document.createElement('img');
    authorImage.src = userPhoto;


    var buttonsDiv = document.createElement('div');
    buttonsDiv.className = 'myButtons';

    var deleteButton = document.createElement('button');
    deleteButton.className = 'deleteButton';
    deleteButton.type = 'button';

    var editButton = document.createElement('button');
    editButton.className = 'editButton';
    editButton.type = 'button';

    var authorName = document.createElement('span');
    authorName.className = 'author';
    authorName.appendChild(document.createTextNode(userName));

    var messageTextDiv = document.createElement('div');
    messageTextDiv.className = 'message-text';

    var text = document.createElement('p');
    text.className = 'msg';
    text.appendChild(document.createTextNode(messageText));


    var time = document.createElement('span');
    time.className = 'msg-time';
    var currentTime = getCurrentData();
    time.appendChild(document.createTextNode(currentTime));


    buttonsDiv.appendChild(deleteButton);
    buttonsDiv.appendChild(editButton);

    messageTextDiv.appendChild(text);
    messageTextDiv.appendChild(time);
    authorDiv.appendChild(authorImage);

    authorDiv.appendChild(buttonsDiv);

    authorDiv.appendChild(authorName);
    authorDiv.appendChild(messageTextDiv);
    newMessageDiv.appendChild(authorDiv);

    var message = createJSONMessage(id, userName, currentTime, messageText, false, false);

    messages.push(message);



    return newMessageDiv;
}

function createJSONMessage(id, userName, time, messageText, isDeleted, isEdited) {
    return {
        'id': id,
        'author': userName,
        'time': time,
        'messageText': messageText,
        'isDeleted': isDeleted,
        'isEdited': isEdited
    };
}

function send(myEvent) {
    if (userName != "") {
        var messageText = document.getElementById('input-message').value;
        
        var chatList = document.getElementById('chat-list');
        if (messageText == "" || messageText == null)
            return;
        var id = generateUUID();
        var newDiv = document.createElement('div');
        var message = new Message(userName, messageText, id);
	 ajax('POST', URL, JSON.stringify(message), function () {
            document.getElementById('input-message').value = "";
        });
        chatList.appendChild(createMessage(messageText + '\n', id));
        chatList.scrollTop = chatList.scrollHeight;
        saveMessages();
    } else {
        alert("You must login!!");
        document.getElementById('input-message').value = "";
    }
}

function deleteMessage(myEvent) {
    var message = myEvent.target.parentNode.parentNode.parentNode;
    if (message.getElementsByClassName("me").length > 0 && userName != "") {
        var messageText = 'Mesaage was removed!';
        var id = message.id;
        
        changeJSON(id, messageText, true, false);
	ajax('DELETE', URL, '{"id":"' + id + '"}', function () {
	    message.childNodes[0].className = 'meRemoved';
            message.childNodes[0].childNodes[3].childNodes[0].innerHTML = messageText;
            message.childNodes[0].childNodes[3].childNodes[1].innerHTML = getCurrentData();
            message.childNodes[0].childNodes[1].style.display = 'none';
        
    })
        saveMessages();
    }
    else {
        alert("FORBIDDEN!!");
    }
}

function editMessage(myEvent) {
    var message = myEvent.target.parentNode.parentNode.parentNode;

    if (message.getElementsByClassName("me").length > 0 && userName != "") {
        messageText = prompt('Input new message', message.childNodes[0].childNodes[3].childNodes[0].innerHTML);
        if (messageText == null || messageText == '') {
            alert("try again!");
            return;
        }
        var id = message.id;
        ajax('PUT', URL, '{"id":"' + id + '", "text":"' + messageText + '"}', function () {
        message.childNodes[0].childNodes[3].childNodes[0].innerHTML = messageText;
        message.childNodes[0].childNodes[3].childNodes[1].innerHTML = 'edited ' + getCurrentData();
        changeJSON(id, messageText, false, true);
    });
        //saveMessages();
    }
    else {
        alert("FORBIDDEN!!");
    }

}

function changeJSON(id, messageText, isDeleted, isEdited) {
    for (var index = 0; index < messages.length; ++index) {
        if (messages[index].id == id) {
            messages[index].messageText = messageText;
            messages[index].isDeleted = isDeleted;
            messages[index].isEdited = isEdited;
            return;
        }
    }

}

function ajax(method, url, data, continueWith) {
    var xhr = new XMLHttpRequest();
    xhr.open(method || 'GET', url, true);
    xhr.onload = function () {
        if (xhr.readyState !== 4) {
            return;
        }

        if (xhr.status != 200) {
            return;
        }
        continueWith(xhr.responseText);
    };
    xhr.onerror = function () {
        //document.getElementsByClassName('error')[0].style.display = 'block';
    };
    xhr.send(data);
}
