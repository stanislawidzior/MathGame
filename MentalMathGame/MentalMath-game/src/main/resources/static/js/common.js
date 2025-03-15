export const wsUrl = "localhost:8080/ws";
export function connectWS(){
    event.preventDefault();
    statusField.innerText = "connecting";
    try{
        stompClient = Stomp.over(new SockJS("http://" + wsUrl));
    }catch(er){
        error(er);
    }
    stompClient.connect( {},connect_callback, error_callback);
}
export function subscribeWs(name){
    event.preventDefault();
    if(!name){
        console.log("subscribe error");
        return;
    }
    stompClient.subscribe(name, subscribe_callback);
}
export function sendMessageWs(destination, message){
    event.preventDefault();
    if(!destination){
        console.log("destination error ");
    }
    stompClient.send(destination,{}, message);
}