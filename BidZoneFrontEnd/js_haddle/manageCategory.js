loadCategories().then(r => null);

async function addNewCategoryvisibleDive(){
    const popupForm = document.getElementById('popup-form');
    const overlay = document.getElementById('popup-overlay');
    const isVisible = popupForm.style.display === 'block';
    const form = document.getElementById('category-form');
    form.category.value='';

    popupForm.style.display = isVisible ? 'none' : 'block';
    overlay.style.display = isVisible ? 'none' : 'block';
}
async function addNewCategory() {
    const form = document.getElementById('category-form');
    const categoryName = form.category.value.trim();

    if (!categoryName) {
        alert("Please Enter Category.js Name");
        return;
    }

    const categoryData = { name: categoryName };

    try {
        const response = await fetch('http://localhost:8080/auctionappBidZone/addcategories', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(categoryData)
        });

        if (response.ok) {
            alert('added successfully!');
            await loadCategories();
            await addNewCategoryvisibleDive();
            window.location.href = 'dashboard.html';
            form.reset();
        } else {
            alert('Failed to add category.');
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
}
async function loadCategories() {
    try {
        const response = await fetch('http://localhost:8080/auctionappBidZone/categories');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const categories = await response.json();
        const select = document.getElementById('category-select');
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.name;
            option.textContent = category.name;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching categories:', error);
    }
}