const wsUrl = document.getElementById('url');
const connectBtn = document.getElementById('connect');
const disconnectBtn = document.getElementById('disconnect');
const subscribeBtn = document.getElementById('subscribe');
const sendMessageBtn = document.getElementById('sendMessage');
const subscribeField = document.getElementById('channelName');
const messageField = document.getElementById('message');
const destinationField = document.getElementById('destination');

const lastReceivedMessage = document.getElementById('receivedMessages');
const errors = document.getElementById('errors');
const statusField = document.getElementById('status');
var subscription = null;
var stompClient = null;

var connect_callback = function() {
   statusField.innerText = "connected";
  };
var error_callback = function(error) {
  alert(error.headers.message);
  errors.innerText = error.headers.message;
};
var subscribe_callback = function(message){
    if(message.body){
        lastReceivedMessage.innerText = message.body;
        statusField.innerText += subscription.Id;
        console.log("Received message from server: " + message.body)
    }else{
        lastReceivedMessage.innerText("received an empty message");
    }
}
function connectWS(){
    event.preventDefault();
    statusField.innerText = "connecting";
    try{
    stompClient = Stomp.over(new SockJS("http://" + wsUrl.value));
    }catch(er){
        error(er);
    }
    stompClient.connect( {},connect_callback, error_callback);
}

function sendMessageWs(){
    event.preventDefault();
    if(!destinationField.value){
        error("choose a destination");
    }
    stompClient.send(destinationField.value,{}, messageField.value);
}

function subscribeWs(){
    event.preventDefault();
    if(!subscribeField.value){
        error("Set a channel name");
        return;
    }
    subscription = stompClient.subscribe(subscribeField.value, subscribe_callback);
}
function disconnectWS(){
    event.preventDefault();
    stompClient.disconnect(function (){
        statusField.innerText = "not connected";
    })
}
function error(err){
    console.log(err);
    errors.innerText = err;
}

connectBtn.addEventListener("click", connectWS);
disconnectBtn.addEventListener("click", disconnectWS);
subscribeBtn.addEventListener("click", subscribeWs);
sendMessageBtn.addEventListener("click", sendMessageWs);

