async function fetchMyBids() {
    const username=localStorage.getItem("username")
    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/myBids?username=${username}`);
        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }
        const bids = await response.json();
        console.log(bids);
        displayBids(bids); // This line calls displayBids function
    } catch (error) {
        console.error('Error fetching bids:', error);
    }
}

function displayBids(bids) {
    const tableBody = document.querySelector('#bidsTable tbody');
    tableBody.innerHTML = ''; // Clear existing rows

    bids.forEach(bid => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${bid.auctionName}</td>
            <td>${bid.placedAt}</td>
            <td>${bid.comment}</td>
            <td>${bid.amount}</td>
            <td>${bid.highestBid}</td>
            <td>${bid.auctionClosesIn}</td>
        `;
        tableBody.appendChild(row); // This line appends the newly created row
    });
}

document.addEventListener('DOMContentLoaded', fetchMyBids);