async function tryLoginByCookies() {
    const response = await fetch("http://localhost:8080/getMe", {
        method: 'GET',
        credentials: 'include'
    });
    const data = await response.json();
    if (response.ok) {
        document.getElementById("username").innerText = `Юзернейм: ${data.username}`;
        document.getElementById("first-name").innerText = `Имя: ${data.firstname}`;
        document.getElementById("last-name").innerText = `Фамилия: ${data.lastname}`;
        document.getElementById("phone").innerText = `Номер телефона: ${data.phoneNumber}`;
        if (data.isWorker) {
            document.getElementById("make-order").remove();
        }

        await fetchNotificationsAmount();
    } else {
        alert(data.error);
        window.location.href = "login.html";
    }
}

async function fetchNotificationsAmount() {
    try {
        const response = await fetch("http://localhost:8080/getUnreadNotificationsAmount", {
            method: 'GET',
            credentials: 'include'
        });

        if (response.ok) {
            const data = await response.json();
            const badge = document.getElementById('notif-count');

            if (data.amount > 0) {
                badge.innerText = data.amount;
                badge.style.display = 'inline-block';
            } else {
                badge.style.display = 'none';
            }
        }
    } catch (error) {
        console.error("Ошибка при получении количества уведомлений:", error);
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