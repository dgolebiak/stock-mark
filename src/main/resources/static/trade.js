var myStockModal = new bootstrap.Modal(document.getElementById('myStockModal'));
function openQuickView({name, symbol, price, pcChange, amount, worth, ownedPriceChangeDollar, ownedPriceChangePercent}) {
    document.getElementById('myStockTicker').value = symbol;
    document.getElementById('stockTitle').innerText = symbol;
    document.getElementById('stockSymbol').innerText = name;
    document.getElementById('stockPrice').innerText = "$" + price;

    if (pcChange > 0) {
        document.getElementById('current-prices').classList.add("stock-positive");
    }
    else{
        document.getElementById('current-prices').classList.add("stock-negative");
    }
    document.getElementById('stockChangePercent').innerText = pcChange + "%";

    document.getElementById('ownedQuantity').innerText = amount;
    document.getElementById('ownedValue').innerText = worth;
    document.getElementById('dollarChangeSincePurchase').innerText = ownedPriceChangeDollar;
    document.getElementById('percentChangeSincePurchase').innerText = ownedPriceChangePercent;

    document.getElementById('totalValue').innerText = document.getElementById('quantity').value * price;

    quantityInput = document.getElementById('quantity');
    totalValue = document.getElementById('totalValue');
    quantityInput.addEventListener("input", function() {
        // Update the text content of the paragraph in real-time
        totalValue.innerText = quantityInput.value * price;
});

    myStockModal.show();
}