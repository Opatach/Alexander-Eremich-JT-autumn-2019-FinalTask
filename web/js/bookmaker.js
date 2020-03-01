var loader = document.getElementById('loader'),
    msgText = document.getElementById('msgText'),
    msgBox = document.getElementById('msgBox'),
    setWinAmountForm = document.getElementById('setWinAmountForm'),
    errorMsg = document.getElementsByClassName('error-msg'),
    errorInput = document.getElementsByTagName('input'),
    incorrectBetDataMsg = document.getElementById('incorrectBetDataMsg'),
    betIdInput = document.getElementById('betIdInput'),
    showBetsButton = document.getElementById('showBetsButton'),
    setWinAmountButton = document.getElementById('setWinAmountButton'),
    racesContent = document.getElementsByClassName('races-content')[0],
    bookmakerContent = document.getElementsByClassName('set-win-amount-content')[0],
    content = document.getElementsByClassName('content')[0],
    setWinAmountProfileOption = document.getElementById('setWinAmountProfileOption'),
    showRacesProfileOption = document.getElementById('showRacesProfileOption'),
    closeMsgBox = document.getElementById('closeMsgBox'),
    logOutProfileOption = document.getElementById('logOutProfileOption'),
    betsTable = document.getElementById('betsTable');


function userRequest(BookmakerHandler, json, resolve, reject) {

    var xhr = new XMLHttpRequest();

    if (!xhr) {
        console.log('Unable to create XMLHTTP instance.');
        return false;
    }

    xhr.open('POST', BookmakerHandler, true);
    xhr.responseType = 'json';
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);

    loader.classList.remove('hidden');

    if (!xhr) {
        loader.classList.add('hidden');
        return false;
    }

    xhr.onload = function() {
        loader.classList.add('hidden');

        if (xhr.status === 200) {
            var res = xhr.response;
            
            if (res.success && resolve) {
                resolve(res);
                reject(res);
            } else if (reject) {
                reject(res);
            }

        } else {
            console.log('Status: ' + xhr.status);
        }
    }
}

function correctBetData(betID) {
    var isValid = true;

    var numberInputPattern = /^[1-9]{1}[0-9]{0,10}$/;

    if (numberInputPattern.test(betID)) {
        incorrectBetDataMsg.classList.add('hidden-msg');
        betIdInput.classList.remove('wrong-data');
    } else {
        incorrectBetDataMsg.classList.remove('hidden-msg');
        betIdInput.classList.add('wrong-data');
        isValid = false;
    }
    

    return isValid;
}

function showResMsg(res) {
    msgText.innerHTML = res.msg;
    msgBox.classList.remove('hidden');
}
function displayBets() {
    userRequest('../BookmakerHandler', JSON.stringify({
        action: 'GET_All_BETS'
    }), (res) => {
        betsTable.innerHTML = '';
        for (var bet of res.bets) {
            displayBet(bet);
        }
    });
}
function displayBet(bet) {
    var row = document.createElement("tr");
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");
    var td4 = document.createElement("td");
    var td5 = document.createElement("td");
    var td6 = document.createElement("td");
    
    td1.appendChild(document.createTextNode(bet.id ? bet.id : ''));
    row.appendChild(td1);
    td2.appendChild(document.createTextNode(bet.customerID ? bet.customerID : ''));
    row.appendChild(td2);
    td3.appendChild(document.createTextNode(bet.raceID ? bet.raceID : ''));
    row.appendChild(td3);
    td4.appendChild(document.createTextNode(bet.betType ? bet.betType : ''));
    row.appendChild(td4);
    td5.appendChild(document.createTextNode(bet.horseNumber ? bet.horseNumber : ''));
    row.appendChild(td5);
    td6.appendChild(document.createTextNode(bet.betAmount ? bet.betAmount : ''));
    row.appendChild(td6);
    
    betsTable.appendChild(row);
}

showBetsButton.addEventListener('click', function() {
    bookmakerContent.classList.add('hide');
    racesContent.classList.remove('hide');
});
setWinAmountButton.addEventListener('click', function() {
    racesContent.classList.add('hide');
    bookmakerContent.classList.remove('hide');
});
closeMsgBox.addEventListener('click', function() {
    loader.classList.add('hidden');
    msgBox.classList.add('hidden');
});

setWinAmountProfileOption.addEventListener('click', function() {
    racesContent.classList.add('hide');
    bookmakerContent.classList.remove('hide');
});
showRacesProfileOption.addEventListener('click', function() {
    bookmakerContent.classList.add('hide');
    racesContent.classList.remove('hide');
});
logOutProfileOption.addEventListener('click', function() {
    userRequest('../BookmakerHandler', JSON.stringify({
        action: 'LOGOUT'
    }), (res) => {
        document.location = res.url;
    });
});

setWinAmountForm.onsubmit = function() {
    
    if (!correctBetData(setWinAmountForm.betIdInput.value)) {
        return false;
    }
    
    setWinAmountForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('../BookmakerHandler', JSON.stringify({
        betID: setWinAmountForm.betIdInput.value,
        action: 'SET_WIN'
    }), displayBets, showResMsg);

    
    setWinAmountForm.classList.remove('hidden');

    return false;
}

displayBets();