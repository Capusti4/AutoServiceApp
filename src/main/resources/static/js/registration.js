const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    await register();
});


async function register() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("confirm_password").value;
    let firstName = document.getElementById("first_name").value;
    let lastName = document.getElementById("last_name").value;
    let phone = document.getElementById("phone").value;
    let isWorker = document.getElementById("isWorker").checked;

    if (password !== confirmPassword) {
        alert("Пароли не совпадают");
        return;
    }

    const response = fetch('http://localhost:8080/register', {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password,
            firstName: firstName,
            lastName: lastName,
            phoneNumber: phone,
            isWorker: isWorker
        })
    });

    const data = await response.json();

    if (response.status !== 201) {
        alert(data.error);
        return;
    }

    alert(data.message);
    window.location.href = "index.html";
}

