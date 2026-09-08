/**
 * UniStay Common UI JavaScript Helpers (common.js)
 * Interactive behaviors, mobile navbar toggle, toast alerts, and layout initialization.
 */
document.addEventListener('DOMContentLoaded', function () {
    initMobileNav();
    initSmoothScroll();
    checkBackendConnection();
});

/**
 * Mobile Navigation Drawer Toggle
 */
function initMobileNav() {
    const mobileBtn = document.querySelector('.mobile-menu-btn');
    const navMenu = document.querySelector('.navbar-nav');

    if (mobileBtn && navMenu) {
        mobileBtn.addEventListener('click', function () {
            navMenu.classList.toggle('active');
            const isOpen = navMenu.classList.contains('active');
            mobileBtn.setAttribute('aria-expanded', isOpen);
            mobileBtn.innerHTML = isOpen ? '&#10005;' : '&#9776;';
        });
    }
}

/**
 * Smooth Scrolling for Anchor Links
 */
function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const targetId = this.getAttribute('href');
            if (targetId !== '#') {
                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    e.preventDefault();
                    targetElement.scrollIntoView({ behavior: 'smooth' });
                }
            }
        });
    });
}

/**
 * Toast Notification Utility
 * Usage: showToast('Welcome to UniStay!', 'success');
 */
function showToast(message, type = 'info') {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast alert-${type}`;
    toast.innerHTML = `
        <span>${message}</span>
        <button style="background:none;border:none;color:inherit;cursor:pointer;margin-left:10px;" onclick="this.parentElement.remove()">&#10005;</button>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        if (toast.parentElement) {
            toast.remove();
        }
    }, 4000);
}

/**
 * Verify Backend System Health on Page Load
 */
async function checkBackendConnection() {
    try {
        if (typeof API !== 'undefined') {
            const response = await API.checkHealth();
            console.log('UniStay Backend Connected:', response.message);
        }
    } catch (err) {
        console.warn('UniStay Backend Notice: Running in standalone frontend preview mode.');
    }
}
