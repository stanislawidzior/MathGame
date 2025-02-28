const gameStatus = document.getElementById("gameStatus");
gameStatus.innerText = "Waiting for players in room : " + sessionStorage.getItem("gameId");
var stompClient = null;
const wsUrl = "localhost:8080/ws";
connectWS();


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

var connect_callback = function() {
    statusField.innerText = "available games";
    subscribeWs(gameRoomsReceiveDestination);
    sendMessageWs(gameRoomsSendDestination);
   };
 var error_callback = function(error) {
   alert(error.headers.message);
   statusField.innerText = "something went wrong";
 };
 var subscribe_callback = function(message){
    if(message.body){
        if(message)
       // console.log("Received message from server: " + message.body);
        display_table(message);

    }else{
        console.log("received an empty message");
    }
}
function subscribeWs(name){
    event.preventDefault();
    if(!name){
        console.log("subscribe error");
        return;
    }
    subscription = stompClient.subscribe(name, subscribe_callback);
}
function sendMessageWs(destination, message){
    event.preventDefault();
    if(!destination){
        console.log("destination error ");
    }
    stompClient.send(destination,{}, message);
}