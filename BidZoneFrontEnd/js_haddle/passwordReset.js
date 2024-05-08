let getCurrettOpt='';

async function sendOTP() {

    const getemail = document.getElementById('email').value;
    if (!getemail || !validateEmail(getemail)) {
        alert("Please enter a valid email address.");
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/auctionappBidZone/validateUserEmailForResetPassword", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            console.log(`XHR status: ${xhr.status}`); // Debug log
            if (xhr.status === 200) {
                const response = xhr.responseText;
                console.log(`Response received: ${response}`); // Debug log
                if (response === "true") {
                    const email = {
                        to: getemail,
                        subject: "Your OTP",
                        content: ""
                    };
                    fetch('http://localhost:8080/auctionappBidZone/sendmailToUser', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(email)
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Failed to send email');
                            }
                            document.getElementById('resetForm').style.display = 'none';
                            document.getElementById('otpForm').style.display = 'block';
                            document.getElementById('email').value='';
                            return response.json();
                        })
                        .then(data => {
                            console.log('Email sent successfully:', data);
                        })
                        .catch(error => {
                            console.error('Error:', error);
                        });
                } else {
                    alert("The provided email address is not associated with any account.");
                    document.getElementById('email').value='';
                }
            } else {
                alert("Error processing request. Please try again later.");
                document.getElementById('email').value='';
            }
        }
    };
    xhr.send("email=" + encodeURIComponent(getemail));
}

function validateEmail(email) {
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return regex.test(email);
}

async function verifyOTP() {
    const getotp = document.getElementById('otp').value;
    const xhr = new XMLHttpRequest();
    xhr.open("POST", `http://localhost:8080/auctionappBidZone/verifyGetOtp`, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = xhr.responseText;
                if (response) {
                    alert("OTP Verified Successfully!");
                    getCurrettOpt=response;
                    document.getElementById('otpForm').style.display = 'none';
                    document.getElementById('newPasswordForm').style.display = 'block';
                } else if(response === "false"){
                    alert("Invalid OTP Code. Please Enter Valid OTP");
                    document.getElementById('otp').value = '';
                }
            } else {
                const response = xhr.responseText;
                 alert(response)
            }
        }
    };
    xhr.send("otp=" + encodeURIComponent(getotp));
}

async function resetPassword() {

    const newPassword = document.getElementById('newPassword').value;
    const reNewPassword = document.getElementById('RenewPassword').value;


    if(newPassword==='' || reNewPassword===''){
        alert('Please File All Fields');
    }
    else if (newPassword !== reNewPassword) {
        alert('Passwords do not match.');

    } else if (newPassword.length > 8) {
        alert('Password cannot exceed 8 characters.');
    }else{

        const password = newPassword.toString();
        try {
            const response = await fetch(`http://localhost:8080/auctionappBidZone/resetPassword?otp=${getCurrettOpt}&password=${password}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
            });
            if (response.ok) {
                alert('Password Update Successfully!');
                window.location="index.html";
            } else {
                alert('Password Update Not Successfully!');
            }
        } catch (error) {
            alert('Error: ' + error.message);
        }
    }

}


function goBack(formId) {

    document.getElementById(formId).style.display = 'none';

    if (formId === 'otpForm') {
        document.getElementById('resetForm').style.display = 'block';
    } else if (formId === 'newPasswordForm') {
        document.getElementById('otpForm').style.display = 'block';
    }
}
function goBackLogin() {
    window.location.href = 'index.html';
}


function togglePassword(fieldId2,fieldId, checkboxId) {
    const field = document.getElementById(fieldId);
    const field2 = document.getElementById(fieldId2);
    const checkbox = document.getElementById(checkboxId);

    if (checkbox.checked) {
        field.type = 'text';
        field2.type = 'text';
    } else {
        field.type = 'password';
        field2.type = 'password';
    }
}

