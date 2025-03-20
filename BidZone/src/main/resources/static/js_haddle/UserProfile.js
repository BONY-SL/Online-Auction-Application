async function loadUserProfile() {
    const username = localStorage.getItem('username');
    console.log(username)
    if (!username) {
        alert("You must be logged in to view this page.");
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/profile?username=${username}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            const profileData = await response.json();
            console.log(profileData)
            document.getElementById('firstName').value = profileData.firstName;
            document.getElementById('lastName').value = profileData.lastName;
            document.getElementById('description').value = profileData.description;
            await updateUserProfile();
        } else {
            const errorText = await response.text();
            alert(`Failed to load profile: ${errorText}`);
        }
    } catch (error) {
        console.error('Error fetching profile:', error);
        alert('An error occurred while fetching the profile.');
    }
}

document.addEventListener('DOMContentLoaded', loadUserProfile);

async function updateUserProfile() {
    const username = localStorage.getItem('username');
    if (!username) {
        alert("You must be logged in to view this page.");
        return;
    }

    console.log(username)
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const description = document.getElementById('description').value;
    const profilePicture = document.getElementById('profilePicture').files[0];

    const formData = new FormData();
    formData.append('profile', new Blob([JSON.stringify({
        firstName, lastName, description
    })], { type: 'application/json' }));
    if (profilePicture) {
        formData.append('image', profilePicture);
    }

    try {
        const response = await fetch(`http://localhost:8080/auctionappBidZone/profile?username=${username}`, {
            method: 'PATCH',
            body: formData
        });

        if (response.ok) {
            const updatedProfile = await response.json();
            document.getElementById('profilePictureDisplay').src = `http://localhost:8080${updatedProfile.profilePictureURL}`;
        } else {
            const errorText = await response.text();
            alert(`Failed to update profile: ${errorText}`);
        }
    } catch (error) {
        console.error('Error updating profile:', error);
        alert('An error occurred while updating the profile.');
    }
}





