async function tryLoginByCookies() {
    const response = await fetch("http://localhost:8080/getMe", {
        method: 'GET',
        credentials: 'include'
    });
    const data = await response.json();
    if (response.ok) {
        // document.getElementById("username").innerText = data.username;
        // document.getElementById("first-name").innerText = data.firstname;
        // document.getElementById("last-name").innerText = data.lastname;
        // document.getElementById("phone").innerText = data.phoneNumber;
        if (data.isWorker) {
            document.getElementById("make-order").remove();
        }
    } else {
        alert(data.error);
        window.location.href = "login.html";
    }
}

async function logout() {
    if (confirm('Вы уверены, что хотите выйти?')) {
        fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('Вы успешно вышли');
                    window.location.href = '/login.html';
                } else {
                    alert('Ошибка при выходе из системы');
                }
            })
            .catch(error => console.error('Ошибка:', error));
    }
}

tryLoginByCookies().then()