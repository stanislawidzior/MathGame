
const wsUrl = "localhost:8080/ws";
const gameRoomsReceiveDestination = "/game/rooms";
const gameRoomsSendDestination = "/app/game/rooms";
let stompClient = null;
let nickname = null;
let selectedRoom = null;
const displayGameRooms = document.getElementById('displayGameRooms');
const availableGameRooms = document.getElementById('availableGameRooms');
const statusField = document.getElementById('status');
const roomsList = document.getElementById('roomsList');
const roomsListH = document.getElementById('roomsListH');
const createGameButton = document.getElementById("createGame");
const displayCreateGameFormButton = document.getElementById("displayCreateGame");
const createGameForm = document.getElementById("createGameForm");
function display_table_row(parsed, rownum){
    for(let key in  parsed){
        let row = roomsList.insertRow();
        if(Array.isArray(parsed[key])){
            let cell = row.insertCell();
            let text = document.createTextNode(rownum);
            let link = document.createElement('button');
            link.textContent = rownum;  
            link.classList.add('clickable');
            link.onclick = function() {
 
                joinGame(rownum);
            
            };
            cell.appendChild(link);
            cell = row.insertCell();
             text = document.createTextNode(parsed[key].length);
            cell.appendChild(text);
        }
    }
}

function display_table(message){
    roomsList.innerHTML = "";
   let parsed = JSON.parse(message.body);
   console.log("parsed "+parsed);
   if(parsed.hasOwnProperty("body")){
    statusField.innerText = parsed["body"];
   }else{
   let row = roomsListH.insertRow();
   let th = document.createElement("th");
   let text = document.createTextNode("room name");
   th.appendChild(text);
   row.appendChild(th);
    th = document.createElement("th");
    text = document.createTextNode("number of players");
   th.appendChild(text);
   row.appendChild(th);
   
   for(let key in  parsed){
    console.log("" + key + "" + parsed[key].length);
    display_table_row(parsed[key], key);
   }
}
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
function joinGame(gameId){
    sessionStorage.setItem("gameId",gameId);
    window.location.href = "../game.html";
}
function connect(){
    availableGameRooms.style.display = "block";
    connectWS();
    event.preventDefault();
}

function connectWS(){
    event.preventDefault();
    statusField.innerText = "connecting";
    try{
    stompClient = Stomp.over(new SockJS("http://" + wsUrl));
    }catch(er){
        error(er);
    }
    stompClient.connect( {},connect_callback, error_callback);
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

function createNewGame(){

}
function displayCreateGameForm(){
     event.preventDefault();
    document.getElementById("createGameForm").style.display = "block";
 }


displayGameRooms.addEventListener('submit', connect, true)
displayCreateGameFormButton.addEventListener('click', displayCreateGameForm, true);