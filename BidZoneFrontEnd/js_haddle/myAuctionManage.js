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
        console.log(auctions)
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

        card.innerHTML = `
        <div class="card">
            <img src="${imageUrl || 'https://via.placeholder.com/300x200'}" class="card-img-top" alt="Auction Item">
            <div class="card-body">
                <h5 class="card-title">${auction.action_name}</h5>
                <p class="card-text">${auction.description}</p>
                <p>Starting Price: ${item.startingPrice}</p>
                <p class="text-danger">Closes on ${new Date(auction.closingTime).toLocaleDateString()}</p>
                <button type="button" class="btn btn-primary">Update Details</button>
                <button type="button" class="btn btn-primary">View Placed Bids</button>
            </div>
        </div>
    `;

        auctionList.appendChild(card);
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