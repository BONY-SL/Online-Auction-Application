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

            const selectElement2 = document.getElementById('userSelect');
            selectElement2.innerHTML = '<option selected>Select User</option>';



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

                const option2 = document.createElement('option');
                option2.value = user.id;
                option2.textContent = user.id;
                selectElement2.appendChild(option2);
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
    document.getElementById('clease').value="";
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
    alert('Reply sent!');

    document.querySelector('#receiveMessage textarea').value = '';
}

async function showMessageHistory() {

    const currentUsername = localStorage.getItem("username");
    console.log(currentUsername)
    const userSelect = document.getElementById('userSelect');
    const selectedValue = userSelect.options[userSelect.selectedIndex].value;
    console.log(selectedValue);
    if (selectedValue === "Select User") {
        alert("Please Select  User")
        return
    }
    if (!selectedValue || !currentUsername) {
        alert("Please Select  User")
        return
    }

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/getUsersMessageHistory?userId=${selectedValue}&username=${currentUsername}`);
        if (!response.ok) {
            const errorDetails = await response.text();
            console.error('Error fetching messages:', errorDetails);
            return;
        }


        const data = await response.json();


        if (data && data.length > 0) {


            const table = document.createElement('table');
            table.className = 'table';
            const thead = document.createElement('thead');
            const tbody = document.createElement('tbody');

            const headers = ['Message', 'Sent At', 'Sent By', 'Sent To'];
            const headerRow = document.createElement('tr');
            headers.forEach(headerText => {
                const header = document.createElement('th');
                header.textContent = headerText;
                headerRow.appendChild(header);
            });
            thead.appendChild(headerRow);

            data.forEach(message => {
                const row = document.createElement('tr');
                const contentCell = document.createElement('td');
                const sentAtCell = document.createElement('td');
                const sendby = document.createElement('td');
                const sendto = document.createElement('td');
                contentCell.textContent = message.content;
                sentAtCell.textContent = new Date(message.sentAt).toLocaleString();
                sendby.textContent=message.sentBy.username;
                sendto.textContent=message.sentTo.username;
                row.appendChild(contentCell);
                row.appendChild(sentAtCell);
                row.appendChild(sendby);
                row.appendChild(sendto);
                tbody.appendChild(row);
            });
            table.appendChild(thead);
            table.appendChild(tbody);
            const messageHistory = document.getElementById('messageHistory');
            messageHistory.innerHTML = '';
            messageHistory.appendChild(table);
        } else {
            console.log('No messages found.');
        }
    } catch (error) {
        console.error('Error fetching messages:', error);
    }


}
