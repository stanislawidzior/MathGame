let stompClient = null;
let playerId = null;

const status = document.getElementById("status");
const messages = document.getElementById("messages");
const roomsList = document.getElementById("rooms");

function log(msg) {
    messages.textContent += msg + "\n";
}

// === CONNECT ===
document.getElementById("connect").onclick = () => {
    const url = document.getElementById("wsUrl").value;

    const socket = new SockJS("http://" + url);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        status.textContent = "connected";
        log("Connected");

        // public rooms updates
        stompClient.subscribe("/game/rooms", msg => {
            renderRooms(JSON.parse(msg.body));
        });

        // user-specific messages
        stompClient.subscribe("/user/game", msg => {
            log("USER: " + msg.body);
        });

        stompClient.subscribe("/user/exceptions", msg => {
            log("ERROR: " + msg.body);
        });

    });
};

// === REGISTER ===
document.getElementById("register").onclick = () => {
    const name = document.getElementById("username").value;

    stompClient.send("/app/game/register", {}, name);

    stompClient.subscribe("/user/game/rooms/register", msg => {
        playerId = msg.body;
        document.getElementById("playerId").textContent = playerId;
        log("Registered with id: " + playerId);
    });
};

// === LOAD ROOMS ===
document.getElementById("loadRooms").onclick = () => {
    stompClient.send("/app/game/rooms", {}, "");
};

// === CREATE ROOM ===
document.getElementById("createRoom").onclick = () => {
    if(playerId == null){

    }
    const dto = {
        settings: {
            question_amount: 5,
            allowed_operations: ["addition"]
        }
    };

    stompClient.send("/app/game/rooms/create", {}, JSON.stringify(dto));
};

// === JOIN ROOM ===
document.getElementById("joinRoom").onclick = () => {
    const roomId = document.getElementById("roomId").value;
    const name = document.getElementById("username").value;

    const joinDto = {
        roomId: roomId,
        userName: name
    };

    stompClient.send("/app/game/rooms/join", {}, JSON.stringify(joinDto));
};

// === RENDER ROOMS ===
function renderRooms(rooms) {
    roomsList.innerHTML = "";
    rooms.forEach(room => {
        const li = document.createElement("li");
        li.textContent = `Room ${room.id}`;
        roomsList.appendChild(li);
    });
}
