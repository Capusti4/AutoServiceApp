const loginForm = document.getElementById('loginForm');
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    await login();
});

async function login() {
    let username = document.getElementById("login_username").value;
    let password = document.getElementById("login_password").value;

    const response = await fetch('http://localhost:8080/login', {
        method: 'POST', credentials: 'include', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({
            username: username, password: password
        })
    });

    if (response.ok) {
        window.location.href = "index.html";
    } else {
        alert((await response.json()).error);
    }
}