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

async function handleViewBids(auctionId) {

    const id = parseInt(auctionId);
    const auction = MyAucyions.find(a => a.id === id);
    if (auction) {
        console.log(auction);
        const bidList = document.getElementById('bid-list');
        const tbody = bidList.querySelector('tbody');
        tbody.innerHTML = '';
        try {
            const response = await fetch(`http://localhost:8080/auctionappBidZone/getTheAllBidsUnderTheAuction?auctionId=${id}`);
            if (!response.ok) {
                throw new Error("Failed to fetch bids data");
            }
            const bids = await response.json();
            bids.forEach(bid => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${bid.placedByUsername}</td>
                    <td>${new Date(bid.placedAt).toLocaleString()}</td>
                    <td>${bid.comment}</td>
                    <td>${bid.amount}</td>
                `;
                tbody.appendChild(row);
            });
        } catch (error) {
            console.error("Error fetching bids data:", error);
        }

        bidList.style.display = 'block';

        const closeBtn = document.getElementById('close-bid-list');
        closeBtn.addEventListener('click', () => {
            bidList.style.display = 'none';
        });
    } else {
        alert(`No auction found with ID: ${auctionId}`);
    }
}

function handleUpdateDetails(auctionId) {

    const id = parseInt(auctionId);
    const auction = MyAucyions.find(a => a.id === id);
    if (auction) {
        document.getElementById('auctionid').textContent = auction.id;
        document.getElementById('auctionName').value = auction.action_name;
        document.getElementById('description').value = auction.description;
        document.getElementById('startingPrice').value = auction.item.startingPrice;
        document.getElementById('closingTime').value = auction.closingTime;
        document.querySelector('.form-container').style.display = 'block'; // Show the form
    }

    document.getElementById('close-au-list').addEventListener('click', function() {
        document.querySelector('.form-container').style.display = 'none';
    });
}


function handleDeleteListing(auctionId) {
    alert(`Deleting auction listing: ${auctionId}`);

}
async function updateAuction() {
    const auctionId = parseInt(document.getElementById('auctionid').textContent);
    const auctionName = document.getElementById('auctionName').value;
    const description = document.getElementById('description').value;
    const startingPrice = parseFloat(document.getElementById('startingPrice').value);
    const closingTime = document.getElementById('closingTime').value;

    const updatedAuction = {
        id: auctionId,
        action_name: auctionName,
        description: description,
        item: {
            startingPrice: startingPrice
        },
        closingTime: closingTime
    };

    console.log(updatedAuction);

    fetch('http://localhost:8080/auctionappBidZone/updateAuction', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedAuction)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update auction.');
            }
            alert('Auction updated successfully.');
            document.querySelector('.form-container').style.display = 'none';
            fetchMyAuctions(); // Ensure this function exists and fetches updated auctions
        })
        .catch(error => {
            console.error('Error updating auction:', error);
        });
}


