let MyAucyions=null;

async function fetchMyAuctions() {
    const username = localStorage.getItem('username');
    if (!username) return;

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/getmyAllAuctions?username=${encodeURIComponent(username)}`);
        if (!response.ok) {
            throw new Error('Failed to fetch auctions');
        }
        const auctions = await response.json();
        displayMyAuctions(auctions);
        MyAucyions=auctions;

    } catch (error) {
        console.error('Error fetching auctions:', error);
    }
}

function displayMyAuctions(auctions) {

    const auctionList = document.getElementById('auction-list');
    auctionList.innerHTML = '';
    auctions.forEach(auction => {
        const imageUrl = auction.image.startsWith('/')
            ? `http://localhost:8080${auction.image}`
            : auction.image;
        const item = auction.item || { startingPrice: 'N/A', name: 'Unknown Item', description: 'No Description' };

        const card = document.createElement('div');
        card.classList.add('col-md-6', 'col-lg-4');
        const cardClass = auction.closed ? 'closed-auction' : 'open-auction';
        card.innerHTML = `
        <div class="card ${cardClass}" style="height: auto">
            <img src="${imageUrl || 'https://via.placeholder.com/300x200'}" class="card-img-top" alt="Auction Item">
            <div class="card-body">
                <p class="card-id">${auction.id}</p>
                <h5 class="card-title">${auction.action_name}</h5>
                <p class="card-text">${auction.description}</p>
                <p>Starting Price: ${item.startingPrice}</p>
                  ${auction.closed ? `<p class="text-danger">Auction is closed</p>` : `<p class="text-success">Closing Time ${new Date(auction.closingTime).toLocaleDateString()}</p>`}
              
                <button type="button" class="btn btn-primary view-bids-btn" data-id="${auction.id}">View Placed Bids</button>
                ${!auction.closed ? `<button type="button" class="btn btn-primary update-btn" data-id="${auction.id}">Update Details</button>` : ''}
                ${auction.closed ? `<button type="button" class="btn btn-primary delete-btn" data-id="${auction.id}">Delete Listing</button>` : ''}
            </div>
        </div>
    `;

        auctionList.appendChild(card);
    });

    document.querySelectorAll('.view-bids-btn').forEach(button => {
        button.addEventListener('click', (event) => handleViewBids(event.target.dataset.id));
    });

    document.querySelectorAll('.update-btn').forEach(button => {
        button.addEventListener('click', (event) => handleUpdateDetails(event.target.dataset.id));
    });

    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', (event) => handleDeleteListing(event.target.dataset.id));
    });

}
document.addEventListener('DOMContentLoaded', fetchMyAuctions);

async function filterMyAuctions(){
    const selectedOrder = document.getElementById('category-Oder').value;
    console.log(selectedOrder)

    const username = localStorage.getItem('username');
    if (!username) return;
    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/getMyAllLisingSpesificOrder?username=${encodeURIComponent(username)}&order=${encodeURIComponent(selectedOrder)}`);

        if (!response.ok) {
            throw new Error('Failed to fetch auctions');
        }
        const auctions = await response.json();
        displayMyAuctions(auctions);
        console.log(auctions)
    } catch (error) {
        console.error('Error fetching auctions:', error);
    }
}

function handleViewBids(auctionId) {

    const id = parseInt(auctionId);
    const auction = MyAucyions.find(a => a.id === id);
    console.log(auction)

}

function handleUpdateDetails(auctionId) {
    alert(`Updating details for auction: ${auctionId}`);

}

function handleDeleteListing(auctionId) {
    alert(`Deleting auction listing: ${auctionId}`);
}
