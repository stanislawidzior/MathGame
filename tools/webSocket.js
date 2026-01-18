
const wsUrlInput = document.getElementById("url");
const connectBtn = document.getElementById("connect");
const disconnectBtn = document.getElementById("disconnect");
const subscribeBtn = document.getElementById("subscribe");
const sendMessageBtn = document.getElementById("sendMessage");

const subscribeField = document.getElementById("channelName");
const messageField = document.getElementById("message");
const destinationField = document.getElementById("destination");

const receivedMessages = document.getElementById("receivedMessages");
const errorsField = document.getElementById("errors");
const statusField = document.getElementById("status");

let stompClient = null;
let subscription = null;

function setStatus(text) {
    statusField.innerText = text;
}

function showError(err) {
    const msg = typeof err === "string"
        ? err
        : err?.headers?.message || err?.message || "Unknown error";

    console.error(msg);
    errorsField.innerText = msg;
}

function onConnect() {
    setStatus("connected");
}

function onError(error) {
    showError(error);
}

function onMessage(message) {
    if (message?.body) {
        receivedMessages.innerText = message.body;
        console.log("Received:", message.body);
    } else {
        receivedMessages.innerText = "Received empty message";
    }
}


function connectWS(event) {
    event.preventDefault();

    if (!wsUrlInput.value) {
        showError("WebSocket URL is required");
        return;
    }

    setStatus("connecting");

    try {
        const socket = new SockJS("http://" + wsUrlInput.value);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnect, onError);
    } catch (err) {
        showError(err);
    }
}

function disconnectWS(event) {
    event.preventDefault();

    if (!stompClient) {
        showError("Not connected");
        return;
    }

    stompClient.disconnect(() => {
        setStatus("not connected");
        stompClient = null;
        subscription = null;
    });
}

function subscribeWS(event) {
    event.preventDefault();

    if (!stompClient?.connected) {
        showError("Connect first");
        return;
    }

    if (!subscribeField.value) {
        showError("Channel name is required");
        return;
    }

    subscription = stompClient.subscribe(
        subscribeField.value,
        onMessage
    );

    setStatus(`subscribed (${subscription.id})`);
}

function sendMessageWS(event) {
    event.preventDefault();

    if (!stompClient?.connected) {
        showError("Connect first");
        return;
    }

    if (!destinationField.value) {
        showError("Destination is required");
        return;
    }

    stompClient.send(
        destinationField.value,
        {},
        messageField.value || ""
    );
}

// ==== Event bindings ====
connectBtn.addEventListener("click", connectWS);
disconnectBtn.addEventListener("click", disconnectWS);
subscribeBtn.addEventListener("click", subscribeWS);
sendMessageBtn.addEventListener("click", sendMessageWS);
