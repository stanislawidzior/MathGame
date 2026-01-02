let stompClient = null;
let userId = null;

const log = (msg) => {
    document.getElementById('log').textContent += msg + "\n";
};

function connectAndRegister() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        log('Connected');

        stompClient.subscribe('/user/game/rooms/register', (msg) => {
            userId = msg.body;
            log('Registered user id: ' + userId);

            stompClient.send('/app/game/rooms', {}, {});
        });

        stompClient.subscribe('/game/rooms', (msg) => {
            const rooms = JSON.parse(msg.body);
            renderRooms(rooms);
        });

        stompClient.subscribe('/user/game', (msg) => {
            log('Server: ' + msg.body);
        });

        stompClient.subscribe('/user/exceptions', (msg) => {
            log('Error: ' + msg.body);
        });

        stompClient.send('/app/game/register', {}, 'Guest');
    });
}

function renderRooms(rooms) {
    const ul = document.getElementById('rooms');
    ul.innerHTML = '';

    rooms.forEach(room => {
        const li = document.createElement('li');
        li.textContent = `Room ${room.id}`;

        const joinBtn = document.createElement('button');
        joinBtn.textContent = 'Join';
        joinBtn.onclick = () => joinRoom(room.id);

        li.appendChild(joinBtn);
        ul.appendChild(li);
    });
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
        gameSettingsDto: {
            settings: {
                question_amount: questionAmount,
                allowed_operations: operations
            }
        }
    };

    stompClient.send('/app/game/rooms/create', {}, JSON.stringify(payload));
}

function joinRoom(roomId) {
    const payload = {
        roomId: roomId,
        userName: 'Guest'
    };

    stompClient.send('/app/game/rooms/join', {}, JSON.stringify(payload));
}

document.getElementById('connectBtn').onclick = connectAndRegister;
document.getElementById('createRoomBtn').onclick = createRoom;
