async function loadNotifications() {
    const container = document.getElementById("notifications-list");
    container.innerHTML = "<p>Загрузка...</p>";

    const response = await fetch("http://localhost:8080/getNotifications", {
        method: 'GET',
        credentials: 'include'
    });

    if (!response.ok) {
        container.innerHTML = "<p style='color: #ef4444;'>Ошибка при загрузке уведомлений</p>";
        return;
    }

    const data = await response.json();

    const notifications = data.notifications;

    container.innerHTML = "";

    if (!notifications || notifications.length === 0) {
        container.innerHTML = "<p style='color: var(--text-muted);'>У вас нет уведомлений.</p>";
        return;
    }

    notifications.forEach(notif => {
        const div = document.createElement("div");
        const isRead = notif.read;

        div.className = `notification-card ${!isRead ? 'unread' : ''}`;

        div.innerHTML = `
            <p>${notif.message || "Новое уведомление"}</p>
            <div class="notification-actions">
                ${!isRead
            ? `<button onclick="toggleReadStatus(${notif.id}, 'read')">Прочитано</button>`
            : `<button class="btn-secondary" onclick="toggleReadStatus(${notif.id}, 'unread')">Не прочитано</button>`
        }
                <button class="btn-danger" onclick="deleteNotification(${notif.id})">Удалить</button>
            </div>
        `;
        container.appendChild(div);
    });
}

async function toggleReadStatus(id, action) {
    const response = await fetch(`http://localhost:8080/${action}Notification/${id}`, {
        method: 'PATCH',
        credentials: 'include'
    });

    if (response.ok) {
        await loadNotifications();
    } else {
        alert("Ошибка при изменении статуса");
    }
}


async function deleteNotification(id) {
    if (!confirm("Удалить это уведомление?")) return;

    const response = await fetch(`http://localhost:8080/deleteNotification/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    });

    if (response.ok) {
        loadNotifications();
    } else {
        alert("Ошибка при удалении");
    }
}


async function readAllNotifications() {
    const response = await fetch(`http://localhost:8080/readAllNotifications`, {
        method: 'PATCH',
        credentials: 'include'
    });

    if (response.ok) {
        await loadNotifications();
    } else {
        alert("Ошибка при обновлении статуса");
    }
}


loadNotifications().then();