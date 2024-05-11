

async function fetchMyBids() {
    const username = localStorage.getItem("username");
    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/myBids?username=${username}`);

        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }
        const bids = await response.json();
        console.log('Fetched bids:', bids); // Log fetched data to the console
        displayBids(bids);
    } catch (error) {
        console.error('Error fetching bids:', error);
    }
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    const formattedDateTime = date.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
    return formattedDateTime.replace(',', ''); // Remove the comma between date and time
}

function displayBids(bids) {
    const tableBody = document.querySelector('#bidsTable tbody');
    tableBody.innerHTML = ''; // Clear existing rows
    const currentTime = new Date();

    bids.forEach(bid => {
        const row = document.createElement('tr');
        const isClose = new Date(bid.auctionClosingTime) <= currentTime;
        row.innerHTML = `
            <td>${bid.auctionId}</td> 
            <td><a href="bid.html?id=${bid.auctionId}&isclose=${isClose}">${bid.auctionName}</a></td>
            <td>${formatDateTime(bid.placedAt)}</td>
            <td>${bid.comment}</td>
            <td>${bid.amount}</td>
            <td>${bid.auctionCurrentHighestBidAmount}</td>
            <td>${formatDateTime(bid.auctionClosingTime)}</td>
            <td>${bid.amount === bid.auctionCurrentHighestBidAmount ? `<button class="btn btn-primary make-payment-btn" onclick="showCar('${bid.auctionName}', '${bid.amount}','${bid.auctionId}','${bid.auctionClosingTime}')">Make Payment</button>` : ''}</td>
        `;

        const auctionIdCell = document.createElement('td'); // Create a new <td> element for auctionId
        auctionIdCell.textContent = bid.auctionId; // Set the text content of the new <td> element
        auctionIdCell.style.display = 'none'; // Hide the cell using inline style
        row.appendChild(auctionIdCell); // Append the new <td> element to the row

        tableBody.appendChild(row);
    });
}

function showCar(auctionName, amount,auctionId,auctionClosingTime) {

    const currentDateTimestamp = new Date().getTime(); // Get current date timestamp in milliseconds
    const closingDateTimestamp = new Date(auctionClosingTime).getTime(); // Get closing date timestamp in milliseconds

    console.log('Current Date Timestamp:', currentDateTimestamp);
    console.log('Closing Date Timestamp:', closingDateTimestamp);

    if (currentDateTimestamp < closingDateTimestamp) {
        //alert("AUCTION IS NOT CLOSED.PLEACE WAIT UNTILE AUCTION IS CLOSED TO PAYMENT");
        showAlert('AUCTION IS NOT CLOSED.PLEACE WAIT UNTILE AUCTION IS CLOSED TO PAYMENT.', 'danger');
    } else{
        localStorage.setItem('auctionName', auctionName);
    localStorage.setItem('amount', amount);
    localStorage.setItem('auctionId', auctionId);

    window.location.href = 'payment.html';
    // window.location.href = 'http://localhost:63342/Online-Auction-Application/BidZoneFrontEnd/client/payment1.html';
}

}

document.addEventListener('DOMContentLoaded', fetchMyBids);

function showAlert(message, type) {
    var alertElement = document.createElement('div');
    alertElement.classList.add('alert', 'alert-' + type);
    alertElement.setAttribute('role', 'alert');
    alertElement.innerText = message;

    var container = document.querySelector('.container'); // Assuming there's a container element in your HTML
    container.insertBefore(alertElement, container.firstChild);

    setTimeout(function () {
        alertElement.remove();
    }, 5000);
}



