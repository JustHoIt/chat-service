const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/stomp/chats'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/sub/chats', (chatMessage) => {
        showMessage(JSON.parse(chatMessage.body));
    });
    stompClient.publish({
        destination: "/pub/chats",
        body: JSON.stringify(
            {'message': "connected"})
    })
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#create").prop("disabled", connected);
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let chatroomId = $("chatroom-id").val();

    stompClient.publish({
        destination: "/pub/chats" + chatroomId,
        body: JSON.stringify(
            {'message': $("#message").val()})
    });
    $("#message").val("")
}

function showMessage(chatMessage) {
    $("#messages").append(
        "<tr><td>" + chatMessage.sender + " : " + chatMessage.message
        + "</td></tr>");
}

function createChatroom() {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/chats?title=' + $("chatroom-title").val(),
        success: function (data) {
            console.log('data', data);
            showChatrooms();
            enterChatrooms(data.id, true);
        },
        error: function (request, status, error) {
            console.log('request', request);
            console.log('error', error);
        }
    })
}

function showChatrooms() {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: '/chats',
        success: function (data) {
            console.log('data', data);
            renderChatrooms(data);
        },
        error: function (request, status, error) {
            console.log('request', request);
            console.log('error', error);
        }
    })
}

function lenderChatrooms(chatrooms) {
    $("#chatroom-list").html("");
    for (let i = 0; i < chatrooms.length; i++) {
        $("#chatroom-list").append(
            "<tr onclick='joinChatroom(" + chatrooms[i].id + ")'><td>"
            + chatrooms[i].id + "</td><td>" + chatrooms[i].title + "</td><td>"
            + chatrooms[i].memberCount + "</td><td>" + chatrooms[i].createdAt
            + "</td></tr>"
        );
    }
}

let subscription;

function enterChatrooms(chatroomId, newMember) {
    $("#chatroom-id").val(chatroomId);
    $("#conversation").show();
    $("#send").prop("disabled", false);
    $("#leave").prop("disabled", false);

    if (subscription != undefined) {
        subscription.unsubscribe();
    }

    subscription = stompClient.subscribe('/sub/chats/' + chatroomId,
        (chatMessage) => {
            showMessage(JSON.parse(chatMessage.body));
        });

    if (newMember) {
        stompClient.publish({
            destination: "/pub/chats/" + chatroomId,
            body: JSON.stringify(
                {'message': "님이 채팅방에 입장했습니다."})
        })
    }

}

function joinChatroom(chatroomId) {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/chats/' + chatroomId,
        success: function (data) {
            console.log('data', data);
            enterChatrooms(data.id, true);
        },
        error: function (request, status, error) {
            console.log('request', request);
            console.log('error', error);
        }
    })
}

function leaveChatroom() {
    let chatroomId = $("chatroom-id").val();
    $.ajax({
        type: 'DELETE',
        dataType: 'json',
        url: '/chats/' + chatroomId,
        success: function (data) {
            console.log('data', data);
            showChatrooms();
            exitChatroom(chatroomId);
        },
        error: function (request, status, error) {
            console.log('request', request);
            console.log('error', error);
        }
    })
}

function exitChatroom(chatroomId) {
    $("#chatroom-id").val("");
    $("#conversation").hide();
    $("#send").prop("disabled", true);
    $("#leave").prop("disabled", true);
}


$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
    $("#create").click(() => createChatroom());
    $("#leave").click(() => leaveChatroom());
});