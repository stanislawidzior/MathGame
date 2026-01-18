let jwtToken = localStorage.getItem("jwtToken") || null;
let stompClient = null;

// ---------- Helpers ----------
function showError(msg) {
    document.getElementById("error").textContent = msg;
}

function clearError() {
    document.getElementById("error").textContent = "";
}

function authFetch(url, options = {}) {
    options.headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        ...(jwtToken ? { 'Authorization': `Bearer ${jwtToken}` } : {})
    };
    return fetch(url, options);
}

// ---------- UI Generators ----------
function renderAuthUI() {
    const container = document.getElementById("authContainer");
    container.innerHTML = "";

    if (jwtToken) {
        // Logged in → show logout
        const logoutBtn = document.createElement("button");
        logoutBtn.textContent = "Logout";
        logoutBtn.onclick = () => {
            jwtToken = null;
            localStorage.removeItem("jwtToken");
            renderAuthUI();
            renderAppUI(); // hide app
        };
        container.appendChild(logoutBtn);
    } else {
        // Not logged in → show login + register
        const loginDiv = document.createElement("div");
        loginDiv.innerHTML = `
            <h2>Login</h2>
            <label>Username: <input type="text" id="loginUsername"/></label>
            <label>Password: <input type="password" id="loginPassword"/></label>
            <button id="loginBtn">Login</button>
        `;
        container.appendChild(loginDiv);

        const registerDiv = document.createElement("div");
        registerDiv.innerHTML = `
            <h2>Register</h2>
            <label>Username: <input type="text" id="regUsername"/></label>
            <label>Email: <input type="email" id="regEmail"/></label>
            <label>Password: <input type="password" id="regPassword"/></label>
            <label>Confirm Password: <input type="password" id="regConfirmPassword"/></label>
            <button id="registerBtn">Register</button>
        `;
        container.appendChild(registerDiv);

        // Attach events
        document.getElementById("loginBtn").addEventListener("click", login);
        document.getElementById("registerBtn").addEventListener("click", register);
    }
}

function renderAppUI() {
    const container = document.getElementById("appContainer");
    container.innerHTML = "";

    if (!jwtToken) return; // hide if not logged in

    // Connect WS button
    const connectBtn = document.createElement("button");
    connectBtn.textContent = "Connect to WS";
    connectBtn.onclick = connectWS;
    container.appendChild(connectBtn);

    // Rooms list
    const roomsHeader = document.createElement("h2");
    roomsHeader.textContent = "Available Rooms";
    container.appendChild(roomsHeader);

    const roomsList = document.createElement("ul");
    roomsList.id = "rooms";
    container.appendChild(roomsList);

    // Create room form
    const createDiv = document.createElement("div");
    createDiv.innerHTML = `
        <h2>Create Room</h2>
        <label>Question amount:
            <input type="number" id="questionAmount" min="1" value="10"/>
        </label>
        <label>Allowed operations:
            <select id="operations" multiple size="4">
                <option value="ADDITION">Addition</option>
                <option value="SUBTRACTION">Subtraction</option>
                <option value="MULTIPLICATION">Multiplication</option>
                <option value="DIVISION">Division</option>
            </select>
        </label>
        <button id="createRoomBtn">Create Room</button>
    `;
    container.appendChild(createDiv);

    const logPre = document.createElement("pre");
    logPre.id = "log";
    container.appendChild(logPre);

    // Attach events
    document.getElementById("createRoomBtn").addEventListener("click", createRoom);
}

// ---------- Auth ----------
async function login() {
    clearError();
    const username = document.getElementById("loginUsername").value;
    const password = document.getElementById("loginPassword").value;

    try {
        const res = await fetch("/api/auth/signin", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (res.ok && data?.data?.token) {
            jwtToken = data.data.token;
            localStorage.setItem("jwtToken", jwtToken);
            renderAuthUI();
            renderAppUI();
        } else {
            showError(data?.message || "Login failed");
        }
    } catch (e) {
        showError(e.message);
    }
}

async function register() {
    clearError();
    const username = document.getElementById("regUsername").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;
    const confirmPassword = document.getElementById("regConfirmPassword").value;

    try {
        const res = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, email, password, confirmPassword })
        });
        const data = await res.json();
        if (res.ok) {
            showError("Registration successful! Please login.");
        } else {
            showError(data?.message || "Registration failed");
        }
    } catch (e) {
        showError(e.message);
    }
}

// ---------- WebSocket ----------
function connectWS() {
        if (!jwtToken) {
            showError("Please login first!");
            return;
        }

        // ZMIANA TUTAJ: Dodajemy token jako parametr query
        const socket = new SockJS('/ws?token=' + jwtToken);

        stompClient = Stomp.over(socket);

        // Authorization header tutaj nadal jest przydatny dla samego protokołu STOMP,
        // ale to "token" w URL załatwia sprawę handshake'u (403).
        stompClient.connect({ 'Authorization': `Bearer ${jwtToken}` }, () => {
            log("Connected to WebSocket");
            stompClient.subscribe('/topic/rooms', (msg) => {
                log("Rooms update: " + msg.body);
            });
            // Subscribe to user-specific messages (SendToUser)
            stompClient.subscribe('/user/queue/game', (msg) => {
                const roomMsg = JSON.parse(msg.body);
                log("User message: " + roomMsg.message);
            });

            // Subscribe to exceptions
            stompClient.subscribe('/user/game/exceptions', (msg) => {
                showError("Error: " + msg.body);
            });
        }, (err) => {
            showError("WS error: " + err);
        });

}

// ---------- Rooms ----------
async function createRoom() {
    if (!jwtToken) {
        showError("Please login first!");
        return;
    }
    const questionAmount = document.getElementById("questionAmount").value;
    const operations = Array.from(document.getElementById("operations").selectedOptions)
        .map(o => o.value);

    try {
        const res = await authFetch("/api/v2/rooms", {
            method: "POST",
            body: JSON.stringify({ questionAmount, operations })
        });
        const data = await res.json();
        if (res.ok) {
            log("Room created: " + JSON.stringify(data));
        } else {
            showError(data?.message || "Failed to create room");
        }
    } catch (e) {
        showError(e.message);
    }
}

// ---------- Logging ----------
function log(msg) {
    const logEl = document.getElementById("log");
    if (logEl) logEl.textContent += msg + "\n";
}

// ---------- Init ----------
renderAuthUI();
renderAppUI();
