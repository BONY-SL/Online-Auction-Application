async function fetchAuctions() {
    try {
        const response = await fetch('http://localhost:8080/auctionappBidZone/getAllauctions');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const auctions = await response.json();
        displayAuctions(auctions);
    } catch (error) {
        console.error('Error fetching auctions:', error);
    }
}
function displayAuctions(auctions) {
    const auctionList = document.getElementById('auction-list');
    auctionList.innerHTML = '';
    auctions.forEach(auction => {
        const imageUrl = auction.image.startsWith('/')
            ? `http://localhost:8080${auction.image}`
            : auction.image;
        const item = auction.item || { startingPrice: 'N/A', name: 'Unknown Item', description: 'No Description' };
        const cardClass = auction.closed ? 'closed-auction' : 'open-auction';
        const card = `
            <div class="col-md-6 col-lg-4">
                <div class="card ${cardClass}">
                    <img src="${imageUrl || 'https://via.placeholder.com/300x200'}" class="card-img-top" alt="Auction Item">
                    <div class="card-body">
                        <h5 class="card-title">${auction.action_name}</h5>
                        <p class="card-text">${auction.description}</p>
                        <p>Starting Price: ${item.startingPrice}</p>
                        ${auction.closed ? `<p class="text-danger">Auction is closed</p>` : `<p class="text-success">Closing Time ${new Date(auction.closingTime).toLocaleDateString()}</p>`}
                        
                        ${!auction.closed ? `<a href="bid.html?id=${auction.id}&isclose=${auction.closed}" class="btn btn-primary">Bid Now</a>` : ''}
                        ${auction.closed ? `<a href="bid.html?id=${auction.id}&isclose=${auction.closed}" class="btn btn-primary">Show Bid History</a>` : ''}
                    </div>
                </div>
            </div>
        `;
        auctionList.insertAdjacentHTML('beforeend', card);
    });
}


document.addEventListener('DOMContentLoaded', fetchAuctions);
