// Загрузка уведомлений при открытии страницы
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

    // Spring обычно возвращает список либо напрямую, либо внутри DTO.
    // Если у тебя в GetNotificationsResponse есть поле notifications (List<Notification>), используем его.
    const notifications = data.notifications;

    container.innerHTML = "";

    if (!notifications || notifications.length === 0) {
        container.innerHTML = "<p style='color: var(--text-muted);'>У вас нет уведомлений.</p>";
        return;
    }

    notifications.forEach(notif => {
        const div = document.createElement("div");
        // Предполагаем, что в Java сущности есть boolean поле isRead / read
        const isRead = notif.read;

        div.className = `notification-card ${!isRead ? 'unread' : ''}`;

        // Предполагаем, что текст лежит в поле message
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

// Смена статуса: Прочитано / Непрочитано
async function toggleReadStatus(id, action) {
    // action может быть 'read' или 'unread'
    const response = await fetch(`http://localhost:8080/${action}Notification/${id}`, {
        method: 'PATCH',
        credentials: 'include'
    });

    if (response.ok) {
        await loadNotifications(); // Перезагружаем список
    } else {
        alert("Ошибка при изменении статуса");
    }
}

// Удаление
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

// Прочитать все сразу
async function readAllNotifications() {
    const response = await fetch(`http://localhost:8080/readAllNotifications`, {
        method: 'PATCH',
        credentials: 'include'
    });

    if (response.ok) {
        loadNotifications();
    } else {
        alert("Ошибка при обновлении статуса");
    }
}

// Запускаем при загрузке скрипта
loadNotifications().then();