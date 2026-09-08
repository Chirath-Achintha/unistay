/**
 * UniStay Admin Student Management Scripts (admin-students.js)
 */

document.addEventListener('DOMContentLoaded', () => {
    loadStudents();
});

function showAlert(message, type = 'success') {
    const container = document.getElementById('alert-container');
    container.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
    setTimeout(() => { container.innerHTML = ''; }, 5000);
}

// -------------------------------------------------------
// Load & Search Students
// -------------------------------------------------------

async function loadStudents(searchTerm = '') {
    const tbody = document.getElementById('students-tbody');
    tbody.innerHTML = '<tr><td colspan="5" class="text-center">Loading students...</td></tr>';
    
    try {
        const query = searchTerm ? `?search=${encodeURIComponent(searchTerm)}` : '';
        const res = await adminAPI.get(`/api/admin/students${query}`);
        const students = res.data;
        
        if (students.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No students found.</td></tr>';
            return;
        }

        tbody.innerHTML = students.map(s => `
            <tr>
                <td>#${s.id}</td>
                <td class="font-semibold">${s.fullName}</td>
                <td>${s.email}</td>
                <td>${s.university || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-outline" onclick="openViewModal(${s.id})">View</button>
                    <button class="btn btn-sm btn-primary" onclick="openEditModal(${s.id})" style="margin: 0 4px;">Edit</button>
                    <button class="btn btn-sm btn-secondary" onclick="confirmDelete(${s.id}, '${s.fullName}')" style="background: var(--danger);">Delete</button>
                </td>
            </tr>
        `).join('');

    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Failed to load students.</td></tr>';
        showAlert(err.message, 'danger');
    }
}

function handleSearch(event) {
    event.preventDefault();
    const term = document.getElementById('search-input').value.trim();
    loadStudents(term);
}

// -------------------------------------------------------
// Modals (View / Edit)
// -------------------------------------------------------

function openModal(id) {
    document.getElementById(id).classList.add('active');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('active');
}

async function openViewModal(id) {
    try {
        const res = await adminAPI.get(`/api/admin/students/${id}`);
        const s = res.data;
        
        const dateStr = s.createdAt ? new Date(s.createdAt).toLocaleString() : 'Unknown';

        document.getElementById('view-details').innerHTML = `
            <div class="detail-row"><div class="detail-label">ID</div><div class="detail-value">#${s.id}</div></div>
            <div class="detail-row"><div class="detail-label">Full Name</div><div class="detail-value">${s.fullName}</div></div>
            <div class="detail-row"><div class="detail-label">Email</div><div class="detail-value">${s.email}</div></div>
            <div class="detail-row"><div class="detail-label">Phone</div><div class="detail-value">${s.phone}</div></div>
            <div class="detail-row"><div class="detail-label">University</div><div class="detail-value">${s.university || 'N/A'}</div></div>
            <div class="detail-row"><div class="detail-label">Gender</div><div class="detail-value">${s.gender || 'N/A'}</div></div>
            <div class="detail-row"><div class="detail-label">Registered</div><div class="detail-value text-muted">${dateStr}</div></div>
        `;
        openModal('view-modal');
    } catch (err) {
        showAlert(err.message, 'danger');
    }
}

async function openEditModal(id) {
    try {
        const res = await adminAPI.get(`/api/admin/students/${id}`);
        const s = res.data;
        
        document.getElementById('edit-id').value = s.id;
        document.getElementById('edit-fullname').value = s.fullName;
        document.getElementById('edit-phone').value = s.phone;
        document.getElementById('edit-university').value = s.university || '';
        document.getElementById('edit-gender').value = s.gender || '';
        
        openModal('edit-modal');
    } catch (err) {
        showAlert(err.message, 'danger');
    }
}

async function handleEditSubmit(event) {
    event.preventDefault();
    const btn = document.getElementById('btn-save');
    const id = document.getElementById('edit-id').value;
    
    const payload = {
        fullName: document.getElementById('edit-fullname').value,
        phone: document.getElementById('edit-phone').value,
        university: document.getElementById('edit-university').value,
        gender: document.getElementById('edit-gender').value
    };

    btn.disabled = true;
    btn.textContent = 'Saving...';

    try {
        await adminAPI.put(`/api/admin/students/${id}`, payload);
        closeModal('edit-modal');
        showAlert('Student updated successfully.');
        loadStudents(document.getElementById('search-input').value); // reload current view
    } catch (err) {
        showAlert(err.message, 'danger');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Save Changes';
    }
}

// -------------------------------------------------------
// Delete Student
// -------------------------------------------------------

async function confirmDelete(id, name) {
    if (confirm(`WARNING: Are you sure you want to permanently delete student "${name}" (ID #${id})?\nThis action cannot be undone.`)) {
        try {
            await adminAPI.delete(`/api/admin/students/${id}`);
            showAlert('Student deleted successfully.');
            loadStudents();
        } catch (err) {
            showAlert(err.message, 'danger');
        }
    }
}
