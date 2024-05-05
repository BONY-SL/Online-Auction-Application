async function fetchAuctions() {
    try {
        const response = await fetch('http://localhost:8080/auctionappBidZone/getAllauctions');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const auctions = await response.json();
        console.log(auctions)
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

        const card = `
            <div class="col-md-6 col-lg-4">
                <div class="card">
                    <img src="${imageUrl || 'https://via.placeholder.com/300x200'}" class="card-img-top" alt="Auction Item">
                    <div class="card-body">
                        <h5 class="card-title">${auction.action_name}</h5>
                        <p class="card-text">${auction.description}</p>
                        <p>Starting Price: ${item.startingPrice}</p>
                        <p class="text-danger">Closes on ${new Date(auction.closingTime).toLocaleDateString()}</p>
                        <a href="bid.html" class="btn btn-primary">Bid Now</a>
                    </div>
                </div>
            </div>
        `;
        auctionList.insertAdjacentHTML('beforeend', card);
    });
}

document.addEventListener('DOMContentLoaded', fetchAuctions);
