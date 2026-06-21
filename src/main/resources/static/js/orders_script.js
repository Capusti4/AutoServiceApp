async function getOrders() {
    const container = document.getElementById("orders");

    const response = await fetch("http://localhost:8080/getOrders", {
        method: 'GET', credentials: 'include'
    });
    const data = await response.json();

    if (!response.ok) {
        alert(data.error);
        window.location.href = "index.html";
        return;
    }

    const meResponse = await fetch("http://localhost:8080/getMe", {
        method: 'GET', credentials: 'include'
    });
    const me = await meResponse.json();

    data.orders.forEach(order => {
        const div = document.createElement("div");

        div.className = "order-card";
        let status;
        switch (order.status) {
            case "new":
                status = "Новый";
                break;
            case "active":
                status = "В работе";
                break;
            case "completed":
                status = "Завершен";
                break;
        }

        let feedbackButtonHtml = "";
        if (order.status === "completed") {
            if (me.isWorker && !order.hasWorkerFeedback) {
                feedbackButtonHtml = `<button onclick="openFeedbackModal(${order.id}, ${order.customer.id})">Оставить отзыв клиенту</button>`;
            } else if (!me.isWorker && !order.hasCustomerFeedback) {
                feedbackButtonHtml = `<button onclick="openFeedbackModal(${order.id}, ${order.worker.id})">Оставить отзыв работнику</button>`;
            }
        }

        const workerHtml = order.worker
            ? `<span class="clickable-user" onclick="viewUserFeedbacks(${order.worker.id}, '${order.worker.lastName}', '${order.worker.firstName}')">${order.worker.lastName} ${order.worker.firstName}</span>`
            : "не назначен";

        const clientHtml = order.customer
            ? `<span class="clickable-user" onclick="viewUserFeedbacks(${order.customer.id}, '${order.customer.lastName}', '${order.customer.firstName}')">${order.customer.lastName} ${order.customer.firstName}</span>`
            : "неизвестен";

        div.innerHTML = `
        <p><b>ID заказа:</b> ${order.id}</p>
        <p><b>Клиент:</b> ${clientHtml}</p>
        <p><b>Работник:</b> ${workerHtml}</p>
        <p><b>Цена:</b> ${order.price ?? "Не назначена"}</p>
        <p><b>Бюджет:</b> ${order.budget ?? "Не ограничен"}</p>
        <p><b>Тип:</b> ${order.type}</p>
        <p><b>Статус:</b> ${status}</p>
        <p><b>Комментарий:</b> ${order.comment || "Отсутствует"}</p>
        ${feedbackButtonHtml}
    `;
        if (me.isWorker) {
            if (order.status === "new") {
                div.innerHTML += `<button onclick="openPreliminaryPriceModal('${order.id}')">Взять в работу</button>`;
            } else if (order.status === "active") {
                div.innerHTML += `<button onclick="openFinalPriceModal('${order.id}')">Завершить</button>`;
            }
        }

        container.appendChild(div);
    });
}

let currentOrderId = null;

function openPreliminaryPriceModal(orderId) {
    currentOrderId = orderId;
    document.getElementById("preliminary-price-input-modal").style.display = "block";
}

async function submitPreliminaryPrice() {
    const value = document.getElementById("preliminaryPriceInput").value;

    if (!currentOrderId) {
        alert("Ошибка: нет заказа");
        return;
    }

    await startOrder(currentOrderId, value);

    document.getElementById("preliminary-price-input-modal").style.display = "none";
    currentOrderId = null;
}

async function startOrder(id, price) {
    const response = await fetch(`http://localhost:8080/startOrder/${id}`, {
        method: 'PATCH', credentials: 'include', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({price: price})
    });

    await parseOrderResponse(response);
}

function openFinalPriceModal(orderId) {
    currentOrderId = orderId;
    document.getElementById("final-price-input-modal").style.display = "block";
}

async function submitFinalPrice() {
    const value = document.getElementById("finalPriceInput").value;

    if (!currentOrderId) {
        alert("Ошибка: нет заказа");
        return;
    }

    await completeOrder(currentOrderId, value);

    document.getElementById("final-price-input-modal").style.display = "none";
    currentOrderId = null;
}

async function completeOrder(id, price) {
    const response = await fetch(`http://localhost:8080/completeOrder/${id}`, {
        method: 'PATCH', credentials: 'include', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({price: price})
    });

    await parseOrderResponse(response);
}

async function parseOrderResponse(response) {
    const data = await response.json();

    if (!response.ok) {
        alert(data.error);
        window.location.href = "index.html";
        return;
    }

    alert(data.message);
    location.reload();
}

let currentFeedbackOrderId = null;
let currentFeedbackTargetId = null;

function openFeedbackModal(orderId, targetId) {
    if (!targetId) {
        alert("Ошибка: не удалось определить получателя отзыва.");
        return;
    }
    currentFeedbackOrderId = orderId;
    currentFeedbackTargetId = targetId;
    document.getElementById("feedback-modal").style.display = "flex"; // Используем flex для центрирования из твоего CSS
}

function closeFeedbackModal() {
    document.getElementById("feedback-modal").style.display = "none";
    document.getElementById("feedback-rating").value = "5"; // Сбрасываем на 5
    document.getElementById("feedback-comment").value = ""; // Очищаем текст
    currentFeedbackOrderId = null;
    currentFeedbackTargetId = null;
}

async function submitFeedback() {
    if (!currentFeedbackOrderId || !currentFeedbackTargetId) return;

    const rating = document.getElementById("feedback-rating").value;
    const comment = document.getElementById("feedback-comment").value;

    // Получаем свои данные, чтобы знать свой authorId
    const meResponse = await fetch("http://localhost:8080/getMe", {
        method: 'GET', credentials: 'include'
    });

    if (!meResponse.ok) {
        alert("Ошибка авторизации. Пожалуйста, перезайдите в аккаунт.");
        return;
    }

    const payload = {
        targetId: currentFeedbackTargetId,
        orderId: currentFeedbackOrderId,
        rating: parseInt(rating),
        feedback: comment
    };

    const response = await fetch('http://localhost:8080/sendFeedback', {
        method: 'POST', credentials: 'include', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify(payload)
    });

    if (response.ok) {
        const data = await response.json();
        alert(data.message || "Отзыв успешно отправлен!");
        closeFeedbackModal();
        location.reload(); // Перезагружаем страницу, чтобы кнопка пропала
    } else {
        const errorData = await response.json();
        alert(errorData.error || "Ошибка при отправке отзыва");
    }
}

async function viewUserFeedbacks(userId, lastName, firstName) {
    const modal = document.getElementById("view-feedbacks-modal");
    const container = document.getElementById("user-feedbacks-container");
    const title = document.getElementById("view-feedbacks-title");

    console.log(userId, lastName, firstName)
    // Используем полученные параметры напрямую
    title.innerText = `Отзывы о пользователе ${lastName} ${firstName} ID: ${userId}`;
    container.innerHTML = "<p style='text-align: center;'>Загрузка...</p>";
    modal.style.display = "flex";

    try {
        // Подставляем userId
        const response = await fetch(`http://localhost:8080/getUserFeedbacks/${userId}`, {
            method: 'GET', credentials: 'include'
        });

        if (!response.ok) {
            container.innerHTML = "<p style='color: red; text-align: center;'>Не удалось загрузить отзывы</p>";
            return;
        }

        const data = await response.json();
        const feedbacks = data.feedbacks || data;

        if (!feedbacks || feedbacks.length === 0) {
            container.innerHTML = "<p style='color: var(--text-muted); text-align: center;'>У этого пользователя пока нет отзывов.</p>";
            return;
        }

        container.innerHTML = "";
        feedbacks.forEach(item => {
            const stars = "★".repeat(item.rating) + "☆".repeat(5 - item.rating);
            container.innerHTML += `
                <div style="background: #f8fafc; padding: 1rem; border-radius: 8px; border: 1px solid var(--border-color);">
                    <p style="color: #f59e0b; font-size: 1.1rem; margin-bottom: 0.3rem;">${stars}</p>
                    <p style="font-size: 0.9rem; color: var(--text-main); margin:0;">${item.feedback || "<i>Без комментария</i>"}</p>
                </div>
            `;
        });
    } catch (e) {
        container.innerHTML = "<p style='color: red; text-align: center;'>Ошибка сети</p>";
    }
}

function closeViewFeedbacksModal() {
    document.getElementById("view-feedbacks-modal").style.display = "none";
}

getOrders().then();