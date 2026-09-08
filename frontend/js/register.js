/**
 * UniStay User Registration Script (register.js)
 * Manages form validation, tab switching, and REST API submissions for Student and Owner.
 */
document.addEventListener('DOMContentLoaded', function () {
    // Check URL parameters for active role tab (e.g. ?role=owner)
    const urlParams = new URLSearchParams(window.location.search);
    const roleParam = urlParams.get('role');
    if (roleParam && roleParam.toLowerCase() === 'owner') {
        switchRole('OWNER');
    } else {
        switchRole('STUDENT');
    }
});

/**
 * Switch between Student and Owner registration forms
 */
function switchRole(role) {
    const studentTab = document.getElementById('tab-student');
    const ownerTab = document.getElementById('tab-owner');
    const studentForm = document.getElementById('student-form');
    const ownerForm = document.getElementById('owner-form');
    const alertContainer = document.getElementById('alert-container');

    if (alertContainer) alertContainer.innerHTML = '';

    if (role === 'OWNER') {
        ownerTab.style.background = 'var(--secondary)';
        ownerTab.style.color = 'white';
        ownerTab.classList.remove('btn-outline');

        studentTab.style.background = 'transparent';
        studentTab.style.color = 'var(--text-main)';
        studentTab.classList.add('btn-outline');

        ownerForm.style.display = 'block';
        studentForm.style.display = 'none';
    } else {
        studentTab.style.background = 'var(--primary)';
        studentTab.style.color = 'white';
        studentTab.classList.remove('btn-outline');

        ownerTab.style.background = 'transparent';
        ownerTab.style.color = 'var(--text-main)';
        ownerTab.classList.add('btn-outline');

        studentForm.style.display = 'block';
        ownerForm.style.display = 'none';
    }
}

/**
 * Handle Student Form Submission
 */
async function handleStudentSubmit(event) {
    event.preventDefault();
    clearAlerts();

    const fullName = document.getElementById('student-fullname').value.trim();
    const email = document.getElementById('student-email').value.trim();
    const phone = document.getElementById('student-phone').value.trim();
    const password = document.getElementById('student-password').value;
    const university = document.getElementById('student-university').value;
    const gender = document.getElementById('student-gender').value;

    // Basic Client Validation
    if (!fullName || !email || !phone || !password || !university || !gender) {
        showAlert('Please fill in all required fields.', 'danger');
        return;
    }

    if (!validateEmail(email)) {
        showAlert('Please enter a valid email address.', 'danger');
        return;
    }

    if (password.length < 6) {
        showAlert('Password must be at least 6 characters long.', 'danger');
        return;
    }

    const payload = { fullName, email, phone, password, university, gender };
    const btnSubmit = document.getElementById('btn-submit-student');
    
    try {
        setLoading(btnSubmit, true, 'Registering...');
        const response = await API.post('/users/register/student', payload);
        
        showAlert(`Success! Welcome to UniStay, ${response.data.fullName}. Your student account has been created.`, 'success');
        document.getElementById('student-form').reset();
        
        if (typeof showToast === 'function') {
            showToast('Student registration successful!', 'success');
        }
    } catch (error) {
        showAlert(error.message || 'Registration failed. Please check your inputs.', 'danger');
    } finally {
        setLoading(btnSubmit, false, 'Create Student Account');
    }
}

/**
 * Handle Owner Form Submission
 */
async function handleOwnerSubmit(event) {
    event.preventDefault();
    clearAlerts();

    const fullName = document.getElementById('owner-fullname').value.trim();
    const email = document.getElementById('owner-email').value.trim();
    const phone = document.getElementById('owner-phone').value.trim();
    const password = document.getElementById('owner-password').value;
    const nic = document.getElementById('owner-nic').value.trim();
    const address = document.getElementById('owner-address').value.trim();

    // Basic Client Validation
    if (!fullName || !email || !phone || !password || !nic || !address) {
        showAlert('Please fill in all required fields.', 'danger');
        return;
    }

    if (!validateEmail(email)) {
        showAlert('Please enter a valid email address.', 'danger');
        return;
    }

    if (password.length < 6) {
        showAlert('Password must be at least 6 characters long.', 'danger');
        return;
    }

    const payload = { fullName, email, phone, password, nic, address };
    const btnSubmit = document.getElementById('btn-submit-owner');

    try {
        setLoading(btnSubmit, true, 'Registering...');
        const response = await API.post('/users/register/owner', payload);

        showAlert(`Success! Welcome aboard, ${response.data.fullName}. Your Boarding Owner account has been created.`, 'success');
        document.getElementById('owner-form').reset();

        if (typeof showToast === 'function') {
            showToast('Owner registration successful!', 'success');
        }
    } catch (error) {
        showAlert(error.message || 'Registration failed. Please check your inputs.', 'danger');
    } finally {
        setLoading(btnSubmit, false, 'Create Boarding Owner Account');
    }
}

/* Helper Functions */
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alert-container');
    if (alertContainer) {
        alertContainer.innerHTML = `
            <div class="alert alert-${type}">
                <span>${message}</span>
            </div>
        `;
    }
}

function clearAlerts() {
    const alertContainer = document.getElementById('alert-container');
    if (alertContainer) alertContainer.innerHTML = '';
}

function setLoading(button, isLoading, text) {
    if (button) {
        button.disabled = isLoading;
        button.innerText = text;
    }
}
