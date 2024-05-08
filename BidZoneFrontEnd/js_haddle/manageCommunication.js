document.addEventListener("DOMContentLoaded", function() {
    showSection('users');
});

//navigate to the sections
function showSection(sectionId) {
    const sections = document.querySelectorAll('.content-section');

    sections.forEach(section => {
        section.style.display = 'none';
    });

    const activeSection = document.getElementById(sectionId);
    activeSection.style.display = 'block';

    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        if(button.onclick.toString().includes(sectionId)) {
            button.classList.add('active');
        } else {
            button.classList.remove('active');
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    fetchUsers();
});

function fetchUsers() {
    fetch('http://localhost:8080/auctionappBidZone/getAllUsers')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('userTableBody');
            tableBody.innerHTML = '';

            console.log(data)
            data.forEach(user => {
                const row = `<tr>
                    <td>${user.id}</td>
                    <td>${user.profile.firstName}</td>
                    <td>${user.profile.lastName}</td>
                    <td>${user.profile.description || 'No description provided'}</td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => {
            console.error('Error fetching users:', error);
        });
}




function sendMessage() {
    const message = document.querySelector('#sendMessage textarea').value;
    const user = document.querySelector('#sendMessage select').value;
    console.log(`Message to send: ${message} to user ${user}`);
    alert('Message sent!');
}
// Simulated received messages data (replace this with your actual data)
const receivedMessages = [
    { sender: 'John', timestamp: '2024-05-08 10:00', message: 'Hello, how are you?' },
    { sender: 'Jane', timestamp: '2024-05-08 10:05', message: 'I\'m doing well, thank you!' },
    // Add more messages as needed
];

function displayReceivedMessages() {
    const receivedMessagesDiv = document.querySelector('.received-messages');
    receivedMessagesDiv.innerHTML = ''; // Clear previous messages

    receivedMessages.forEach(message => {
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message');

        const senderInfo = document.createElement('span');
        senderInfo.classList.add('sender-info');
        senderInfo.textContent = `${message.sender} (${message.timestamp}):`;

        const messageContent = document.createElement('span');
        messageContent.classList.add('message-content');
        messageContent.textContent = message.message;

        messageDiv.appendChild(senderInfo);
        messageDiv.appendChild(messageContent);

        receivedMessagesDiv.appendChild(messageDiv);
    });
}

// Call the function to display received messages when the page loads
document.addEventListener('DOMContentLoaded', displayReceivedMessages);

// Function to handle sending a reply (similar to the previous example)
function sendReply() {
    const reply = document.querySelector('#receiveMessage textarea').value;
    console.log(`Reply sent: ${reply}`);
    // Here you would typically handle sending the reply to the appropriate recipient
    alert('Reply sent!'); // Placeholder feedback
    // Optionally, you can clear the textarea after sending the reply
    document.querySelector('#receiveMessage textarea').value = '';
}
// Simulated message history data (replace this with your actual data)
const messageHistoryData = {
    user1: [
        { timestamp: '2024-05-08 10:00', message: 'Hello!' },
        { timestamp: '2024-05-08 10:05', message: 'How are you?' },
        // Add more messages for user1 as needed
    ],
    user2: [
        { timestamp: '2024-05-08 11:00', message: 'Hi there!' },
        { timestamp: '2024-05-08 11:05', message: 'I\'m doing fine, thanks!' },
        // Add more messages for user2 as needed
    ],
    // Add message history for more users as needed
};

function showMessageHistory() {
    const userSelect = document.getElementById('userSelect');
    const selectedUser = userSelect.value;
    const messageHistoryDiv = document.getElementById('messageHistory');

    messageHistoryDiv.innerHTML = '';

    if (selectedUser && messageHistoryData[selectedUser]) {
        const messages = messageHistoryData[selectedUser];
        messages.forEach(message => {
            const messageDiv = document.createElement('div');
            messageDiv.classList.add('message');
            messageDiv.textContent = `${message.timestamp}: ${message.message}`;
            messageHistoryDiv.appendChild(messageDiv);
        });
    } else {
        const messageDiv = document.createElement('div');
        messageDiv.textContent = 'No message history available for this user.';
        messageHistoryDiv.appendChild(messageDiv);
    }
}
