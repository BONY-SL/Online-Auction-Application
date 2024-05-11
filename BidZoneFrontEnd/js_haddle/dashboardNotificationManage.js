async function showNewStarListing() {
    try {
        const response = await fetch("http://localhost:8080/auctionappBidZone/getNotifyMessageAddNewAuction");
        if (!response.ok) {
            throw new Error('Network response was not ok.');
        }
        const auctionDetails = await response.json();
        console.log(auctionDetails);

        if(!auctionDetails.id){
            alert("Not Have Add New Listing Recently")
            return
        }

        document.getElementById('auctionId').textContent = auctionDetails.id;
        document.getElementById('auctionName').textContent = "Auction Name : "+auctionDetails.action_name;
        document.getElementById('auctionDescription').textContent ="Description : "+ auctionDetails.description;
        document.getElementById('startingPrice').textContent = auctionDetails.item.startingPrice;
        document.getElementById('closingTime').textContent = auctionDetails.closingTime;
        document.getElementById('auctionImage').src = auctionDetails.image.startsWith('/')
            ? `http://localhost:8080${auctionDetails.image}`
            : auctionDetails.image;

        document.getElementById('newListingCard').style.display = 'block';
    } catch (error) {
        console.error('Failed to fetch data: ', error);
        console.error('Error details: ', error.message);
    }
}






function hideNewListing() {
    document.getElementById('newListingCard').style.display = 'none';
}
