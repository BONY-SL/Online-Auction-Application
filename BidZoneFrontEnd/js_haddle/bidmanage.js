document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const auctionId = params.get('id');

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
        displayAuctionDetails(auction);
    } catch (error) {
        console.error('Error fetching auction details:', error);
    }
});

function displayAuctionDetails(auction) {
    document.querySelector('h3').textContent = auction.action_name;
    document.querySelector('.img-fluid').src = auction.image.startsWith('/') ? `http://localhost:8080${auction.image}` : auction.image;

    console.log(auction)

}
