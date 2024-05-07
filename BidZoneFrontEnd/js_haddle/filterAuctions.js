async function filterCategories() {
    const selectedCategory = document.getElementById('category-select').value;
    console.log(selectedCategory)
    const url = selectedCategory === 'all'
        ? 'http://localhost:8080/auctionappBidZone/getAllauctions'
        : `http://localhost:8080/auctionappBidZone/getAuctionsByCategory?category=${selectedCategory}`;

    const response = await fetch(url);
    const auctions = await response.json();
    console.log(auctions)
    displayAuctions(auctions);
}
