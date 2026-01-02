let stompClient = null;
let userId = null;

const log = (msg) => {
    document.getElementById('log').textContent += msg + "\n";
};


async function connectAndRegister() {
    let token = localStorage.getItem("wsToken");

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    const headers = token ? { Authorization: "Bearer " + token } : {};

    stompClient.connect(
        headers,
        () => {
            log('Connected');

            // Subscribe to registration endpoint to receive JWT
            stompClient.subscribe('/user/game/rooms/register', (msg) => {
                userId = msg.body;

                // If this is the first connection, the server sends a JWT here
                if (!token) {
                    token = userId; // assume server sends JWT as body
                    localStorage.setItem("wsToken", token);
                    log('Received JWT token from server and saved to localStorage');
                }

                log('Registered user id: ' + userId);

                // Request rooms list, now authenticated
                stompClient.send('/app/game/rooms', { Authorization: "Bearer " + token }, {});
            });

            // Subscribe to rooms updates
            stompClient.subscribe('/game/rooms', (msg) => {
                const rooms = JSON.parse(msg.body);
                log(JSON.stringify(rooms.players));
                renderRooms(rooms);
            });

            // Subscribe to general messages
            stompClient.subscribe('/user/game', (msg) => {
                log('Server: ' + msg.body);
            });

            // Subscribe to exceptions
            stompClient.subscribe('/user/game/exceptions', (msg) => {
                log(msg.body);
                renderError(msg.body);
            });

            // Send registration message (server responds with JWT)
            stompClient.send('/app/game/register', headers, 'Guest');
        }
    );
}

function renderRooms(rooms) {
    const roomsContainer = document.getElementById('rooms');


    roomsContainer.innerHTML = '';

    rooms.forEach(room => {
        // Create a container for each room
        const roomDiv = document.createElement('div');
        roomDiv.classList.add('room'); // add class for styling
        roomDiv.style.border = '1px solid #ccc';
        roomDiv.style.padding = '10px';
        roomDiv.style.marginBottom = '10px';
        roomDiv.style.borderRadius = '5px';
        roomDiv.style.backgroundColor = '#f9f9f9';

        // Room ID
        const roomIdP = document.createElement('p');
        roomIdP.textContent = `Room ID: ${room.roomId}`;
        roomDiv.appendChild(roomIdP);

        // Game status
        const statusP = document.createElement('p');
        statusP.textContent = `Status: ${room.gameInProgress ? 'In Progress' : 'Waiting'}`;
        roomDiv.appendChild(statusP);

        // Players
        const playersP = document.createElement('p');
        if (room.players && room.players.length > 0) {
            const playerNames = room.players.map(p => p.name).join(', ');
            playersP.textContent = `Players: ${playerNames}`;
        } else {
            playersP.textContent = 'Players: none';
        }
        roomDiv.appendChild(playersP);

        // Join button
        const joinBtn = document.createElement('button');
        joinBtn.textContent = 'Join';
        joinBtn.disabled = room.gameInProgress; // disable if game started
        joinBtn.onclick = () => joinRoom(room.roomId);
        roomDiv.appendChild(joinBtn);

        // Append the room div to container
        roomsContainer.appendChild(roomDiv);
    });

}

function renderError(error){
    const errorContainer = document.getElementById("error");
    errorContainer.innerHTML = "";

    let errorMessage = document.createElement("h3");
    errorMessage.innerText = error

    errorContainer.appendChild(errorMessage);
}

function getSelectedOperations() {
    const select = document.getElementById('operations');
    return Array.from(select.selectedOptions).map(o => o.value);
}

function createRoom() {
    const questionAmount = parseInt(document.getElementById('questionAmount').value, 10);
    const operations = getSelectedOperations();

    if (operations.length === 0) {
        log('Select at least one operation');
        return;
    }

    const payload = {

            settings: {
                question_amount: questionAmount,
                allowed_operations: operations
            }

    };

    stompClient.send('/app/game/rooms/create', {}, JSON.stringify(payload));
}

function joinRoom(roomId) {
    const payload = {roomId:roomId}

    stompClient.send('/app/game/rooms/join', {}, JSON.stringify(payload));
}

document.getElementById('connectBtn').onclick = connectAndRegister;
document.getElementById('createRoomBtn').onclick = createRoom;
