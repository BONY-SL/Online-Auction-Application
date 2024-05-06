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
        await displayAuctionDetails(auction);
    } catch (error) {
        console.error('Error fetching auction details:', error);
    }
});

async function displayAuctionDetails(auction) {


    document.querySelector('.created-by').innerHTML = `<span class="label">Created By:</span> ${auction.createdById}`;
    document.querySelector('.auction-id').innerHTML = `<span class="label">Auction ID:</span> ${auction.id}`;
    document.querySelector('h3').textContent = auction.action_name;
    document.querySelector('.description').innerHTML = `<span class="label">Description:</span> ${auction.description}`;
    document.querySelector('.starting-price').innerHTML = `<span class="label">Starting Price Rs:</span> ${auction.item.startingPrice}`;


    document.querySelector('.highest-bid').innerHTML = auction.currentHighestBid !== null && auction.currentHighestBid !== undefined
        ? `<span class="label">Current Highest Bid Rs:</span> ${auction.currentHighestBid}`
        : '<span class="label">Current Highest Bid Rs:</span> No bids yet';


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
    console.log(id)
    const userDetails = await fetch(`http://localhost:8080/auctionappBidZone/getUserDetails?id=${id}`)
        .then(response => response.json())
        .catch(error => console.error('Error fetching user details:', error));

    console.log(userDetails)

    if (userDetails) {

        document.querySelector('.user-image').src = userDetails.profile.profilePictureURL.startsWith('/')
            ? `http://localhost:8080${userDetails.profile.profilePictureURL}`
            : userDetails.profile.profilePictureURL;
        document.querySelector('.username').textContent = userDetails.username;
        document.querySelector('.full-name').textContent = `${userDetails.profile.firstName} ${userDetails.profile.lastName}`;
    }

}

function placedBid() {
    alert('Bid placed successfully!');


    const modal = document.querySelector('#placeBidModal');
    modal.classList.remove('show');
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