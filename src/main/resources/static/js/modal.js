let myStockModal;
let chart;

document.addEventListener('DOMContentLoaded', function() {
    myStockModal = new bootstrap.Modal(document.getElementById('myStockModal'));
});


let currentSymbol;

function openQuickView({name, symbol, price, pcChange, amount, worth, ownedPriceChangeDollar, ownedPriceChangePercent}) {
    document.getElementById('stockTitle').innerText = symbol;
    document.getElementById('stockSymbol').innerText = name;
    document.getElementById('stockPrice').innerText = price;
    document.getElementById('stockChangePercent').innerText = pcChange;
    document.getElementById('ownedQuantity').innerText = amount;
    document.getElementById('ownedValue').innerText = worth;
    document.getElementById('dollarChangeSincePurchase').innerText = ownedPriceChangeDollar;
    document.getElementById('percentChangeSincePurchase').innerText = ownedPriceChangePercent;
    document.getElementById('totalValue').innerText = (Math.round(document.getElementById('quantity').value * price * 100) / 100).toFixed(2);
    document.getElementById('myStockTicker').value = symbol;
    currentSymbol = symbol;

    let quantityInput = document.getElementById('quantity');
    let totalValue = document.getElementById('totalValue');
    quantityInput.addEventListener("input", function() {
        let currentBalance = document.getElementById('currentBalance').value.substring(1);
        // Update the text content of the paragraph in real-time
        totalValue.innerText = (Math.round(quantityInput.value * price * 100) / 100).toFixed(2);
        if (quantityInput.value * price > currentBalance) {
            console.log("Hello")
            document.getElementById('totalValue').style.color = "red";
            document.getElementById('placeButton').disabled = true;
        } else {
            document.getElementById('totalValue').style.color = "#5d5c5c";
            document.getElementById('placeButton').disabled = false;
        }
    });

    fetchRender();
    myStockModal.show();
}

async function fetchStockData(ticker, lastNdays, intervals) {
    const url = new URL('http://localhost:8080/stockgraph');

    url.searchParams.append('ticker', ticker);
    url.searchParams.append('lastNdays', lastNdays);
    url.searchParams.append('intervals', intervals);

    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response not ok');
            }
            return response.json();
        })
        .catch(error => {
            console.error('Fetch operation error:', error);
        });
}

function renderGraph(data) {
    // Convert Unix timestamps to JavaScript Date objects

    const ctx = document.getElementById('graph');

    const x = data.map(d => d.timestamp);
    const y = data.map(d => d.adjclose);

    let color = 'rgb(189, 195, 199)';

    if (y[0] < y[y.length - 1]) {
        color = 'rgb(0, 220, 70)';
    } else if (y[0] > y[y.length - 1]) {
        color = 'rgb(175, 65, 84)';
    }

    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: x,
            datasets: [{
                data: y,
                borderColor: color,
                backgroundColor: color,
                fill: false,
            }]
        },
        options: {
            scales: {
                y: {
                    border: {
                        display: false,
                    },
                    grid: {
                        display: false,
                        drawOnChartArea: false,
                    },
                    ticks: {
                        display: false,
                    },
                },
                x: {
                    border: {
                        display: false,
                    },
                    grid: {
                        display: false,
                        drawOnChartArea:false,
                    },
                    ticks: {
                        display: false,
                    },
                },
            },
            plugins: {
                tooltip: {
                    usePointStyle: true,
                    callbacks: {
                        title: function(context) {
                            const chart = context[0].chart;
                            const dataIndex = context[0].dataIndex;
                            const xValue = chart.data.labels[dataIndex];
                            const formattedDate = new Date(xValue * 1000).toDateString();

                            return formattedDate;
                        },
                        label: function(context) {
                            return '$' + (Math.round(context.parsed.y * 100) / 100).toFixed(2);
                        },
                        labelPointStyle: function(context) {
                            return {
                                pointStyle: 'circle',
                            }
                        }
                    }
                },
                legend: {
                    display: false,
                }
            },
            responsive: true,
            maintainAspectRatio: false,
        }
    });
}

function fetchRender(lastNdays = 30, intervals = '1d') {
    if(chart) {
        chart.destroy();
    }
    fetchStockData(currentSymbol, lastNdays, intervals).then(stockData => {
        if (stockData) {
            renderGraph(stockData);
        } else {
            console.error('No stock data received');
        }
    }).catch(err => {
        console.error('Error fetching stock data:', err);
    });
}