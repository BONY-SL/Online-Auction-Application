async function fetchMyBids() {
    const username = localStorage.getItem("username");
    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/myBids?username=${username}`);
        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }
        const bids = await response.json();
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

    bids.forEach(bid => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><a href="http://localhost:63342/Online-Auction-Application/BidZoneFrontEnd/client/bid.html?id=${bid.auctionId}">${bid.auctionName}</a></td>
            <td>${formatDateTime(bid.placedAt)}</td>
            <td>${bid.comment}</td>
            <td>${bid.amount}</td>
            <td>${bid.auctionCurrentHighestBidAmount}</td>
            <td>${formatDateTime(bid.auctionClosingTime)}</td>
            <td>${bid.amount === bid.auctionCurrentHighestBidAmount ? `<button class="btn btn-primary make-payment-btn" onclick="showCar('${bid.auctionName}', '${bid.amount}')">Make Payment</button>` : ''}</td>
        `;
        tableBody.appendChild(row);
    });
}

function showCar(auctionName, amount) {
   localStorage.setItem('auctionName', auctionName);
   localStorage.setItem('amount', amount);
   window.location.href = 'http://localhost:63342/Online-Auction-Application/BidZoneFrontEnd/client/payment.html';
   // window.location.href = 'http://localhost:63342/Online-Auction-Application/BidZoneFrontEnd/client/payment1.html';

}

document.addEventListener('DOMContentLoaded', fetchMyBids);
