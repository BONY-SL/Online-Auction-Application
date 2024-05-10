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
            const selectElement = document.getElementById('userSelects');
            selectElement.innerHTML = '<option selected>Select user to message</option>';

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
                const option = document.createElement('option');
                option.value = user.id;
                option.textContent = user.id;
                selectElement.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error fetching users:', error);
        });
}




async function sendMessage() {
    const messagebody = document.querySelector('#sendMessage textarea').value;
    const userId = document.querySelector('#sendMessage select').value;
    const currentUserName = localStorage.getItem("username");

    if (!userId || !messagebody) {
        alert("Please select a user and enter a message.");
        return;
    }

    const message = {
        sentBy: { id: '', username: currentUserName },
        sentTo: { id: userId },
        sentAt: '',
        content: messagebody
    };

    try {
        const response = await fetch('http://localhost:8080/auctionappBidZone/messages/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(message)
        });

        if (!response.ok) {
            throw new Error('Failed to send message');
        }

        const data = await response.json();
        console.log('Message sent:', data);
    } catch (error) {
        console.error('Error sending message:', error);
    }
}
async function displayReceivedMessages() {
    const receivedMessagesDiv = document.querySelector('.received-messages');
    receivedMessagesDiv.innerHTML = '';
    const userIdgetMessage = document.querySelector('#userSelects').value;
    const username = localStorage.getItem("username");

    const id=parseInt(userIdgetMessage);

    if (!userIdgetMessage || userIdgetMessage === 'Select user to message') {
        receivedMessagesDiv.textContent = "Please select a user to view messages.";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/messages/response?userId=${id}&username=${username}`);
        if (!response.ok) {
            const errorDetails = await response.text();
            throw new Error(`Failed to fetch messages: ${response.status} ${response.statusText} - ${errorDetails}`);
        }
        const data = await response.json();
        if (data && data.content) {
            const messageContent = data.content;
            const messageElement = document.createElement('div');
            messageElement.textContent = messageContent;
            receivedMessagesDiv.appendChild(messageElement);
        } else {
            receivedMessagesDiv.textContent = "No messages found for the selected user.";
        }
    } catch (error) {
        console.error('Error fetching messages:', error);
        receivedMessagesDiv.textContent = `An error occurred while fetching messages: ${error.message}`;
    }
}




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
