async function loadMyFeedbacks(type) {
    const container = document.getElementById("feedbacks-list");
    container.innerHTML = "<p>Загрузка...</p>";

    const tabForMe = document.getElementById("tab-for-me");
    const tabByMe = document.getElementById("tab-by-me");

    if (type === 'ForUser') {
        tabForMe.style.background = 'var(--primary-color)'; tabForMe.style.color = 'white';
        tabByMe.style.background = 'transparent'; tabByMe.style.color = 'var(--primary-color)';
    } else {
        tabByMe.style.background = 'var(--primary-color)'; tabByMe.style.color = 'white';
        tabForMe.style.background = 'transparent'; tabForMe.style.color = 'var(--primary-color)';
    }

    const response = await fetch(`http://localhost:8080/getFeedbacks${type}`, {
        method: 'GET', credentials: 'include'
    });

    if (!response.ok) {
        container.innerHTML = "<p style='color: #ef4444;'>Ошибка при загрузке отзывов</p>";
        return;
    }

    const data = await response.json();
    const feedbacks = data.feedbacks || data;

    if (!feedbacks || feedbacks.length === 0) {
        container.innerHTML = "<p style='color: var(--text-muted);'>Отзывов пока нет.</p>";
        return;
    }

    container.innerHTML = "";
    feedbacks.forEach(item => {
        const stars = "★".repeat(item.rating) + "☆".repeat(5 - item.rating);
        const div = document.createElement("div");
        div.className = "order-card";
        div.innerHTML = `
            <p style="color: #f59e0b; font-size: 1.2rem; margin-bottom: 0.5rem;">${stars}</p>
            <p style="color: var(--text-main);">${item.feedback || "<i>Без комментария</i>"}</p>
        `;
        container.appendChild(div);
    });
}


loadMyFeedbacks('ForUser').then();