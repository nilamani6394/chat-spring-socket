'use strict';

const nameInput = $('#name');
const roomInput = $('#room-id');
const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');
const roomIdDisplay = document.querySelector('#room-id-display');

let stompClient = null;
let username = null;
let currentSubscription;
let roomId = null;
let topic = null;
let Cookies = null;

const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

/**starts new connection*/
function connect(event) {
    username = document.querySelector('#name').value.trim();
    Cookies.set('name', username);
    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/firstApp');
        stompClient = Stomp.over(socket);
        /**connect to the client*/
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

/**connect to the room*/
function onConnected() {
    /**First need to subscribe the public topic (define in side controller)*/
    // stompClient.subscribe('/topic/public', onMessageReceived);
    enterRoom(roomInput.val());
    /**Aware about the username to the server*/
    // stompClient.send("/app/chat.register",
    //     {},
    //     JSON.stringify({sender: username, type: 'JOIN'})
    // )
    //
    // connectingElement.classList.add('hidden');
    connectingElement.classList.add('hidden');
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

/**send message */
function sendMessage(event) {
    const messageContent = messageInput.value.trim();


    if (messageContent.startsWith('/join')) {
        let newRoomId = messageContent.substring('/join'.length);
        enterRoom(newRoomId);
        while (messageArea.firstChild) {
            messageArea.removeChild(messageArea.firstChild);
        }
    } else if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        /**now send the message to the client*/
        /**which contain message content*/
        // stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
        stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(chatMessage));
    }
    messageInput.value = '';
    event.preventDefault();
}
/**Leave the current room and enter in the new room*/
function enterRoom(newRoomId) {
    roomId = newRoomId;
    Cookies.set('roomId', roomId);
    roomIdDisplay.textContent = roomId;
    topic = `app/chat/${newRoomId}`;
    if (currentSubscription) {
        currentSubscription.unsubscribe();
    }
    currentSubscription = stompClient.subscribe(`/channel/${roomId}`, onMessageReceived());
    stompClient.send(`${topic}/addUser`, {},
        JSON.stringify({sender: username, type: 'JOIN'}));
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);

    const messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
        // messageElement.unsubscribe()
    } else {
        messageElement.classList.add('chat-message');

        const avatarElement = document.createElement('i');
        const avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        const usernameElement = document.createElement('span');
        const usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    const textElement = document.createElement('p');
    const messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    const index = Math.abs(hash % colors.length);
    return colors[index];
}

$(document).ready(function (){
    let saveName = Cookies.get('name');
    if (saveName){
        nameInput.val(saveName)
    }
    let  saveRoom = Cookies.get('roomId');
    if (saveRoom){
        roomInput.val(saveRoom);
    }
    usernamePage.classList.remove('hidden');
    usernameForm.addEventListener('submit',connect,true);
    messageForm.addEventListener('submit',sendMessage,true);
});