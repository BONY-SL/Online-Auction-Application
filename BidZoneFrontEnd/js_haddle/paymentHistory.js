

document.addEventListener('DOMContentLoaded', function () {
    // Get username from localStorage
    var username = localStorage.getItem('username');

    // Check if username is available
    if (!username) {
        console.error('Username not found in localStorage.');
        return;
    }

    // API endpoint URL
    var apiUrl = 'http://localhost:8080/auctionappBidZone/getpaymentdetails/' + encodeURIComponent(username);

    // Fetch data from API endpoint
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Payment Details:', data);

            // Check if data is an array or object
            if (Array.isArray(data)) {
                // Call function to render payment details in the table
                renderPaymentDetails(data);
            } else if (typeof data === 'object') {
                // If data is an object, convert it to an array with a single item
                renderPaymentDetails([data]);
            } else {
                console.error('Invalid data format. Expected an array or object.');
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
});

function renderPaymentDetails(paymentData) {
    var bidsTableBody = document.querySelector('#bidsTable tbody');

    // Clear existing table rows
    bidsTableBody.innerHTML = '';

    // Check if paymentData is an array
    if (!Array.isArray(paymentData)) {
        console.error('Invalid payment data format. Expected an array.');
        return;
    }

    // Loop through payment data and create table rows
    paymentData.forEach(payment => {
        const newRow = document.createElement('tr');

        // Format the date string
        const formattedDate = new Date(payment.payment_date).toLocaleString('en-US', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        newRow.innerHTML = `
            <td>${payment.auction_name}</td>
            <td>RS.${payment.amount}</td>
            <td>${payment.addresss}</td>
            <td>${payment.pnumber}</td>
            <td>${payment.paymentmethord}</td>
            <td>${formattedDate}</td>
        `;
        bidsTableBody.appendChild(newRow);
    });
}

