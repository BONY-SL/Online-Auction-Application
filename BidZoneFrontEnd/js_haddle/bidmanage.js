document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const auctionId = params.get('id');
    const close=params.get("isclose");
    console.log(auctionId)
    console.log(close)

    if (!auctionId) {
        console.error('Auction ID not provided');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/getAuctiondetails?id=${auctionId}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const auction = await response.json();
        if (close === "true") {
            document.querySelector('.auction-container').style.display = 'none';
            await displayAuctionDetails(auction);
        }else {
            await displayAuctionDetails(auction);
        }


    } catch (error) {
        console.error('Error fetching auction details:', error);
    }
});
async function getTheAllBidsUnderTheAuction(auctionId){
    try {

        const response = await fetch(`http://localhost:8080/auctionappBidZone/getTheAllBidsUnderTheAuction?auctionId=${auctionId}`);
        if (!response.ok) {
            throw new Error("Failed to fetch bids data");
        }

        const bids = await response.json();
        console.log(bids)
        populateBidTable(bids);
    } catch (error) {
        console.error("Error fetching bids data:", error);
    }
}

function populateBidTable(bids) {

    const tableBody = document.querySelector(".bid-list table tbody");
    tableBody.innerHTML = "";

    bids.forEach(bid => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${bid.placedByUsername}</td>
            <td>${new Date(bid.placedAt).toLocaleString()}</td>
            <td>${bid.comment || "N/A"}</td>
            <td>${bid.amount.toFixed(2)}</td>
        `;
        tableBody.appendChild(row);
    });
}

//display Auction Current Details In the database
async function displayAuctionDetails(auction) {


    document.querySelector('.created-by').innerHTML = `<span class="label">Created By:</span> ${auction.createdById}`;
    document.querySelector('.auction-id').innerHTML = `<span class="label">Auction ID:</span> ${auction.id}`;
    document.querySelector('h3').textContent = auction.action_name;
    document.querySelector('.description').innerHTML = `<span class="label">Description:</span> ${auction.description}`;
    document.querySelector('.starting-price').innerHTML = `<span class="label">Starting Price Rs:</span> ${auction.item.startingPrice}`;


    if (auction.currentHighestBid && auction.currentHighestBid.amount !== null && auction.currentHighestBid.amount !== undefined) {
        document.querySelector('.highest-bid').innerHTML = `<span class="label">Current Highest Bid Rs:</span> ${auction.currentHighestBid.amount}`;
    } else {
        document.querySelector('.highest-bid').innerHTML = '<span class="label">Current Highest Bid Rs:</span> No bids yet';
    }


    document.querySelector('.closing-time').innerHTML = `<span class="label">Closing Time:</span> ${new Date(auction.closingTime).toLocaleString()}`;
    document.querySelector('.category').innerHTML = `<span class="label">Category:</span> ${auction.item.category.name}`;
    document.querySelector('.item-name').innerHTML = `<span class="label">Item Name:</span> ${auction.item.name}`;
    document.querySelector('.item-specific').innerHTML = `<span class="label">Item Description:</span> ${auction.item.description}`;

    const imgElement = document.querySelector('.img-fluid');
    imgElement.src = auction.image.startsWith('/') ? `http://localhost:8080${auction.image}` : auction.image;
    imgElement.alt = auction.item.name;

    imgElement.style.width = '300px';
    imgElement.style.height = '400px';
    const id=auction.createdById;
    await getTheAllBidsUnderTheAuction(auction.id);

    const userDetails = await fetch(`http://localhost:8080/auctionappBidZone/getUserDetails?id=${id}`)
        .then(response => response.json())
        .catch(error => console.error('Error fetching user details:', error));


    if (userDetails) {

        document.querySelector('.user-image').src = userDetails.profile.profilePictureURL.startsWith('/')
            ? `http://localhost:8080${userDetails.profile.profilePictureURL}`
            : userDetails.profile.profilePictureURL;
        document.querySelector('.username').textContent = userDetails.username;
        document.querySelector('.full-name').textContent = `${userDetails.profile.firstName} ${userDetails.profile.lastName}`;
    }

}

async function placedBid() {
    const username = localStorage.getItem('username');

    const auctionIdElement = document.querySelector('.auction-id');
    const auctionIdValue = parseInt(auctionIdElement.textContent.match(/\d+/)[0]);
    const amount = document.getElementById('amount').value;
    const comment = document.getElementById('comment').value;

    if (!amount || !comment) {
        alert('Please fill in both amount and comment');
        return;
    }

    const bidToItemDTO = {
        amount: parseFloat(amount),
        comment: comment
    };

    const bidRequestDTO = {
        auctionId: auctionIdValue,
        username: username,
        bidToItemDTO: bidToItemDTO
    };

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/bidForAuctionItem`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(bidRequestDTO)
        });

        if (response.ok) {
            alert('Bid placed successfully!');
            const updatedAuctionResponse = await fetch(`http://localhost:8080/auctionappBidZone/getAuctiondetails?id=${auctionIdValue}`);
            const updatedAuction = await updatedAuctionResponse.json();
            await displayAuctionDetails(updatedAuction);
            const modal = document.querySelector('#placeBidModal');
            modal.classList.remove('show');
        } else {
            const errorData = await response.json();
            console.error(`Error Data: ${JSON.stringify(errorData)}`);
            alert(`Error: ${errorData.message}`);
        }
    } catch (error) {
        console.error(`Network Error: ${error}`);
        alert(`Unexpected error: ${error.message}`);
    }
}





function showPlaceBidModal() {
    const modal = document.querySelector('#placeBidModal');
    modal.classList.add('show');
}

function placedBidhide(){

    const modal = document.querySelector('#placeBidModal');
    modal.classList.remove('show');

}
function navigateToChat() {
    const chatContainer = document.getElementById('chatContainer');
    if (chatContainer.style.display === 'none') {
        chatContainer.style.display = 'block';
    } else {
        chatContainer.style.display = 'none';
    }
}
function hidechat(){
    const chatContainer = document.getElementById('chatContainer');
    chatContainer.style.display = 'none';
}