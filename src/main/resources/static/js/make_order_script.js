const orderForm = document.getElementById('orderForm');
orderForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    await makeOrder();
});

async function makeOrder() {
    let orderType = document.getElementById("order-type").value;
    let comment = document.getElementById("comment").value;
    let budget = document.getElementById("budget").value;

    if (comment === "") {
        comment = null
    }

    const response = await fetch('http://localhost:8080/makeOrder', {
        method: 'POST', credentials: 'include', headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify({
            typeId: orderType, comment: comment, budget: budget
        })
    })

    const data = await response.json();
    if (response.status === 201) {
        alert(data.message);
    } else {
        alert(data.error);
    }
    window.location.href = "index.html";
}