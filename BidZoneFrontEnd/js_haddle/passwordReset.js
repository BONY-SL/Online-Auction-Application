function sendOTP() {

    document.getElementById('resetForm').style.display = 'none';
    document.getElementById('otpForm').style.display = 'block';
}

function verifyOTP() {

    document.getElementById('otpForm').style.display = 'none';
    document.getElementById('newPasswordForm').style.display = 'block';
}

function resetPassword() {

    alert('Password reset successfully!');

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

