async function createNewAuction() {

    const action_name = document.getElementById('name').value.trim();
    const description = document.getElementById('description').value.trim();
    const item = document.getElementById('item').value.trim();
    const itemSpecifications = document.getElementById('itemSpecifications').value.trim();
    const categorySelect = document.getElementById('category-select');
    const category = categorySelect.options[categorySelect.selectedIndex].value;
    const closingDate = document.getElementById('auctionClosingDate').value.trim();
    const startingPrice = document.getElementById('startingPrice').value.trim();
    const imageInput = document.getElementById('Image');
    const image = imageInput.files[0];

    if (!action_name || !description || !item || !category || !closingDate || !startingPrice || !itemSpecifications || !image) {
        alert('Please fill out all required fields.');
        return;
    }

    const auctionData = {
        action_name,
        description,
        item: {
            name: item,
            description: itemSpecifications,
            startingPrice,
            category: { id: category }
        },
        closingTime: closingDate
    };

    const formData = new FormData();
    formData.append('auction', new Blob([JSON.stringify(auctionData)], { type: 'application/json' }));

    if (image) {
        formData.append('image', image);
    }

    const username = localStorage.getItem('username');

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/createNewAuctions?username=${username}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            const result = await response.json();
            alert(`Auction created successfully with ID: ${result.id}`);
            this.clearFormData();

        } else {
            const errorText = await response.text();
            alert(`Failed to create auction: ${errorText}`);
        }
    } catch (error) {
        console.error('Error creating auction:', error);
        alert('An error occurred while creating the auction.');
    }
}

function clearFormData() {

    const form = document.getElementById('createAuctionForm');


    form.querySelectorAll('input, textarea, select').forEach(element => {
        if (element.tagName === 'INPUT' && (element.type === 'text' || element.type === 'number' || element.type === 'date')) {
            element.value = '';
        }

        if (element.type === 'file') {
            element.value = null;
        }
        if (element.tagName === 'TEXTAREA') {
            element.value = '';
        }

        if (element.tagName === 'SELECT') {
            element.selectedIndex = 0;
        }
    });
}
