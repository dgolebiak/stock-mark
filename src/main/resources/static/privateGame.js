var gameModal = new bootstrap.Modal(document.getElementById('gameModal'));
function openGameView({ gameName, budget, isPlayerInGame, startDate, endDate }) {
    document.getElementById('gameViewGameName').value = gameName;
    document.getElementById('isPlayerInGame').value = isPlayerInGame;
    document.getElementById('gameViewBudget').innerText = "$" + budget;
    document.getElementById('gameModalTitle').innerText = gameName;
    document.getElementById('gameViewStartDate').innerText = startDate;
    document.getElementById('gameViewEndDate').innerText = endDate;
    console.log(isPlayerInGame);

    if(isPlayerInGame){
        document.getElementById('joinButton').innerText = "Leave Game";
    }
    else{
        document.getElementById('joinButton').innerText = "Join Game";
    }
    gameModal.show();
}


function checkInputs() {
    // Get input values
    var gameName = document.getElementById('gameName').value;
    var budget = document.getElementById('budget').value;
    var startDate = document.getElementById('startDate').value;
    var endDate = document.getElementById('endDate').value;

    // Get the create button
    var createButton = document.getElementById('createButton');

    // Check if any input is empty
    var inputsNotEmpty = (gameName !== '' && budget !== '' && startDate !== '' && endDate !== '');

    var currentDate = new Date();
    var selectedStartDate = new Date(startDate);
    var selectedEndDate = new Date(endDate);

    var isStartDateValid = selectedStartDate >= currentDate;
    var isEndDateValid = selectedEndDate > selectedStartDate;

    console.log("Name: " + gameName + " Budget: " + budget + " Start Date: " + startDate + " End Date: " + endDate);

    console.log("Inputs: " + inputsNotEmpty);
    console.log("Start date: " + isStartDateValid);
    console.log("End date: " + isEndDateValid);

    if (!(isStartDateValid && isEndDateValid && inputsNotEmpty)) {
        createButton.disabled = true;
    } else {
        // Enable or disable the create button based on input values
        createButton.disabled = false;
    }   
}

function activate(clickedLink, concernedTabs, concernedContent, activatedContent){
    var tabs = document.querySelectorAll('.' + concernedTabs);
    tabs.forEach(function(tab) {
        tab.classList.remove('active');
      });

      // Add 'active' class to the clicked link
      clickedLink.classList.add('active');

      // Inactivate all content
      var contents = document.querySelectorAll('.' + concernedContent);
      contents.forEach(function(content) {
        content.classList.remove('activeOption');
      });

      // Activate choosen content
      var activateContent = document.getElementById(activatedContent);
      activateContent.classList.add('activeOption');
}
