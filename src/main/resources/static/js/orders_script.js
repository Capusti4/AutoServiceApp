async function getOrders() {
    const container = document.getElementById("orders");

    const response = await fetch("http://localhost:8080/getOrders", {
        method: 'GET',
        credentials: 'include'
    });
    const data = await response.json();

    if (!response.ok) {
        alert(data.error);
        window.location.href = "index.html";
        return;
    }

    const meResponse = await fetch("http://localhost:8080/getMe", {
        method: 'GET',
        credentials: 'include'
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
        div.innerHTML = `
            <p><b>ID:</b> ${order.id}</p>
            <p><b>Работник:</b> ${order.workerId ?? "не назначен"}</p>
            <p><b>Цена:</b> ${order.price ?? "Не назначена"}</p>
            <p><b>Бюджет:</b> ${order.budget ?? "Не ограничен"}</p>
            <p><b>Тип:</b> ${order.type}</p>
            <p><b>Статус:</b> ${status}</p>
            <p><b>Комментарий:</b> ${order.comment || "Отсутствует"}</p>
            <p><b>Отзыв клиента:</b> ${order.hasCustomerFeedback ? "есть" : "нет"}</p>
            <p><b>Отзыв работника:</b> ${order.hasWorkerFeedback ? "есть" : "нет"}</p>
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
    const response = await fetch(`http://localhost:8080/startOrder/${id}`,
        {
            method: 'PATCH',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({price: price})
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
    const response = await fetch(`http://localhost:8080/completeOrder/${id}`,
        {
            method: 'PATCH',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({price: price})
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

getOrders().then();