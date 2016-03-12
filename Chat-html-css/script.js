var userName = "";
var messages = [];

function run() {
    var myEvent = document.getElementsByClassName('main')[0];

    myEvent.addEventListener('click', delegateEvent);
    myEvent.addEventListener("keydown", delegateEvent);
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

function login() {
    userName = document.getElementById('loginTextArea').value;
    document.getElementById('loginTextArea').value = "";
    messages = document.getElementsByClassName("message");
    window.alert('Hello ' + userName);


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
function createMessage(messageText, classType, editable, deleted) {
    var newMessageDiv = document.createElement('div');
    newMessageDiv.className = 'message';

    var authorDiv = document.createElement('div');
    authorDiv.className = classType;

    var authorImage = document.createElement('img');
    authorImage.src = 'src/me.jpg';

    if (deleted != 'true') {
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
    authorName.appendChild(document.createTextNode(userName));

    var messageTextDiv = document.createElement('div');
    messageTextDiv.className = 'message-text';

    var text = document.createElement('p');
    text.className = 'msg';
    text.appendChild(document.createTextNode(messageText));


    var time = document.createElement('span');
    time.className = 'msg-time';
    time.appendChild(document.createTextNode(editable + getCurrentData()));

    if (deleted != 'true') {
        buttonsDiv.appendChild(deleteButton);
        buttonsDiv.appendChild(editButton);
    }
    messageTextDiv.appendChild(text);
    messageTextDiv.appendChild(time);
    authorDiv.appendChild(authorImage);
    if (deleted != 'true') {
        authorDiv.appendChild(buttonsDiv);
    }
    authorDiv.appendChild(authorName);
    authorDiv.appendChild(messageTextDiv);
    newMessageDiv.appendChild(authorDiv);
    return newMessageDiv;
}

function send(myEvent) {
    if (userName != "") {
        var messageText = document.getElementById('input-message').value;

        document.getElementById('input-message').value = "";
        var chatList = document.getElementById('chat-list');
        if (messageText == "" || messageText == null)
            return;
        var newDiv = document.createElement('div');
        chatList.appendChild(createMessage(messageText + '\n', 'me', '', 'false'));
        chatList.scrollTop = chatList.scrollHeight;
        messageText = '';
    } else {
        alert("You must login!!");
        document.getElementById('input-message').value = "";
    }
}

function deleteMessage(myEvent) {
    var message = myEvent.target.parentNode.parentNode.parentNode;
    if (message.getElementsByClassName("me").length > 0 && userName != "") {
        var messageText = 'Mesaage was removed!';
        var div = createMessage(messageText, 'meRemoved\n', '', 'true');
        var newMessage = div.childNodes[0];
        message.parentNode.replaceChild(newMessage, message);
    }
    else {
        alert("FORBIDDEN!!");
    }
}

function editMessage(myEvent) {
    var message = myEvent.target.parentNode.parentNode.parentNode;

    if (message.getElementsByClassName("me").length > 0 && userName != "") {
        messageText = prompt('Input new message', 'Enter the message');
        if (messageText == null || messageText == '') {
            alert("try again!");
            return;
        }
        var div = createMessage(messageText , 'me', 'edited ', '');
        var newMessage = div.childNodes[0];
        message.parentNode.replaceChild(newMessage, message);
    }
    else {
        alert("FORBIDDEN!!");
    }
}

