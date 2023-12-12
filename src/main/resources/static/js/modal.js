let myStockModal;

document.addEventListener('DOMContentLoaded', function() {
    myStockModal = new bootstrap.Modal(document.getElementById('myStockModal'));
});

function openQuickView({name, symbol, price, pcChange, amount, worth, ownedPriceChangeDollar, ownedPriceChangePercent}) {
    document.getElementById('stockTitle').innerText = symbol;
    document.getElementById('stockSymbol').innerText = name;
    document.getElementById('stockPrice').innerText = price;
    document.getElementById('stockChangePercent').innerText = pcChange;

    document.getElementById('ownedQuantity').innerText = amount;
    document.getElementById('ownedValue').innerText = worth;
    document.getElementById('dollarChangeSincePurchase').innerText = ownedPriceChangeDollar;
    document.getElementById('percentChangeSincePurchase').innerText = ownedPriceChangePercent;

    document.getElementById('totalValue').innerText = document.getElementById('quantity').value * price;

    let quantityInput = document.getElementById('quantity');
    let totalValue = document.getElementById('totalValue');
    quantityInput.addEventListener("input", function() {
        // Update the text content of the paragraph in real-time
        totalValue.innerText = quantityInput.value * price;
    });
    console.log("hello")
    myStockModal.show();
}