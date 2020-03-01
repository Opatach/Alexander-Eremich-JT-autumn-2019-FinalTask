var loader = document.getElementById('loader'),
    msgText = document.getElementById('msgText'),
    msgBox = document.getElementById('msgBox'),
    addRaceForm = document.getElementById('addRaceForm'),
    editRaceForm = document.getElementById('editRaceForm'),
    deleteRaceForm = document.getElementById('deleteRaceForm'),
    determineTheWinnerForm = document.getElementById('determineTheWinnerForm'),
    errorMsg = document.getElementsByClassName('error-msg'),
    errorInput = document.getElementsByTagName('input'),
    incorrectAddRaceNameMsg = document.getElementById('incorrectAddRaceNameMsg'),
    incorrectAddRaceCoefficientMsg = document.getElementById('incorrectAddRaceCoefficientMsg'),
    incorrectEditRaceNumberMsg = document.getElementById('incorrectEditRaceNumberMsg'),
    incorrectNewRaceNameMsg = document.getElementById('incorrectNewRaceNameMsg'),
    incorrectNewRaceCoefficientMsg = document.getElementById('incorrectNewRaceCoefficientMsg'),
    incorrectNewRaceDataMsg = document.getElementById('incorrectNewRaceDataMsg'),
    incorrectDeleteRaceMsg = document.getElementById('incorrectDeleteRaceMsg'),
    incorrectWinnersDataMsg = document.getElementById('incorrectWinnersDataMsg'),
    betIdInput = document.getElementById('betIdInput'),
    addRaceButton = document.getElementById('addRaceButton'),
    editRaceButton = document.getElementById('editRaceButton'),
    deleteRaceButton = document.getElementById('deleteRaceButton'),
    showRacesButton = document.getElementById('showRacesButton'),
    determineTheWinnerButton = document.getElementById('determineTheWinnerButton'),
    racesContent = document.getElementsByClassName('races-content')[0],
    adminContent = document.getElementsByClassName('admin-content')[0],
    content = document.getElementsByClassName('content')[0],
    determineTheWinnerProfileOption = document.getElementById('determineTheWinnerProfileOption'),
    showRacesProfileOption = document.getElementById('showRacesProfileOption'),
    logOutProfileOption = document.getElementById('logOutProfileOption'),
    closeMsgBox = document.getElementById('closeMsgBox'),
    racesTable = document.getElementById('racesTable');


function userRequest(AdminHandler, json, resolve, reject) {

    var xhr = new XMLHttpRequest();

    if (!xhr) {
        console.log('Unable to create XMLHTTP instance.');
        return false;
    }

    xhr.open('POST', AdminHandler, true);
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
            } else if (reject) {
                reject(res);
            }

        } else {
            console.log('Status: ' + xhr.status);
        }
    }
}

function showResMsg(res) {
    if (!res.success) {
        msgText.innerHTML = res.msg;
        msgBox.classList.remove('hidden');
    }
}
function displayRaces() {
    racesTable.innerHTML = '';
    userRequest('../AdminHandler', JSON.stringify({
        action: 'GET_ALL_RACES'
    }), (res) => {
        for (var race of res.races) {
            displayRace(race);
        }
    });
}
function displayRace(race) {
    var row = document.createElement("tr");
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");
    var td4 = document.createElement("td");
    var td5 = document.createElement("td");
    var td6 = document.createElement("td");
    
    td1.appendChild(document.createTextNode(race.id ? race.id : ''));
    row.appendChild(td1);
    td2.appendChild(document.createTextNode(race.name ? race.name : ''));
    row.appendChild(td2);
    td3.appendChild(document.createTextNode(race.coefficient ? race.coefficient : ''));
    row.appendChild(td3);
    td4.appendChild(document.createTextNode(race.firstWinner ? race.firstWinner : '-'));
    row.appendChild(td4);
    td5.appendChild(document.createTextNode(race.secondWinner ? race.secondWinner : '-'));
    row.appendChild(td5);
    td6.appendChild(document.createTextNode(race.thirdWinner ? race.thirdWinner : '-'));
    row.appendChild(td6);
    
    racesTable.appendChild(row);
}

function correctAddRaceData(raceName, raceCoefficient) {
    var isValid = true;

    var raceNameInputPattern = /^[a-zA-ZА-Яа-я]{1}[0-9a-zA-ZА-Яа-я]{1,19}$/;
    var coefficientInputPattern = /^[0-9]*[.,]?[0-9]+$/;

    var raceNameInput = document.getElementById('raceNameInput'),
        raceCoefficientInput = document.getElementById('raceCoefficientInput');

    if (raceNameInputPattern.test(raceName)) {
        incorrectAddRaceNameMsg.classList.add('hidden-msg');
        raceNameInput.classList.remove('wrong-data');
    } else {
        incorrectAddRaceNameMsg.classList.remove('hidden-msg');
        raceNameInput.classList.add('wrong-data');
        isValid = false;
    }
    if (coefficientInputPattern.test(raceCoefficient)) {
        incorrectAddRaceCoefficientMsg.classList.add('hidden-msg');
        raceCoefficientInput.classList.remove('wrong-data');
    } else {
        incorrectAddRaceCoefficientMsg.classList.remove('hidden-msg');
        raceCoefficientInput.classList.add('wrong-data');
        isValid = false;
    }
    
    return isValid;
}
function correctEditRaceData(raceNumber, raceName, raceCoefficient,
        firstWinner, secondWinner, thirdWinner) {
    var isValid = true;

    var numberInputPattern = /^[1-9]{1}[0-9]{0,10}$/;
    var raceNameInputPattern = /^[a-zA-ZА-Яа-я]{1}[0-9a-zA-ZА-Яа-я]{1,19}$/;
    var coefficientInputPattern = /^[0-9]*[.,]?[0-9]+$/;

    var editRaceNumberInput = document.getElementById('editRaceNumberInput'),
        editRaceNameInput = document.getElementById('editRaceNameInput'),
        editRaceCoefficientInput = document.getElementById('editRaceCoefficientInput'),
        editFirstNumberInput = document.getElementById('editFirstNumberInput'),
        editSecondNumberInput = document.getElementById('editSecondNumberInput'),
        editThirdNumberInput = document.getElementById('editThirdNumberInput');

    if (numberInputPattern.test(raceNumber)) {
        incorrectEditRaceNumberMsg.classList.add('hidden-msg');
        editRaceNumberInput.classList.remove('wrong-data');
    } else {
        incorrectEditRaceNumberMsg.classList.remove('hidden-msg');
        editRaceNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (raceNameInputPattern.test(raceName)) {
        incorrectNewRaceNameMsg.classList.add('hidden-msg');
        editRaceNameInput.classList.remove('wrong-data');
    } else {
        incorrectNewRaceNameMsg.classList.remove('hidden-msg');
        editRaceNameInput.classList.add('wrong-data');
        isValid = false;
    }
    if (coefficientInputPattern.test(raceCoefficient)) {
        incorrectNewRaceCoefficientMsg.classList.add('hidden-msg');
        editRaceCoefficientInput.classList.remove('wrong-data');
    } else {
        incorrectNewRaceCoefficientMsg.classList.remove('hidden-msg');
        editRaceCoefficientInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(firstWinner)) {
        incorrectNewRaceDataMsg.classList.add('hidden-msg');
        editFirstNumberInput.classList.remove('wrong-data');
    } else {
        incorrectNewRaceDataMsg.classList.remove('hidden-msg');
        editFirstNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(secondWinner)) {
        incorrectNewRaceDataMsg.classList.add('hidden-msg');
        editSecondNumberInput.classList.remove('wrong-data');
    } else {
        incorrectNewRaceDataMsg.classList.remove('hidden-msg');
        editSecondNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(thirdWinner)) {
        incorrectNewRaceDataMsg.classList.add('hidden-msg');
        editThirdNumberInput.classList.remove('wrong-data');
    } else {
        incorrectNewRaceDataMsg.classList.remove('hidden-msg');
        editThirdNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    
    return isValid;
}
function correctDeleteRaceData(raceNumber) {
    var isValid = true;

    var numberInputPattern = /^[1-9]{1}[0-9]{0,10}$/;

    deleteRaceNumberInput = document.getElementById('deleteRaceNumberInput');

    if (numberInputPattern.test(raceNumber)) {
        incorrectDeleteRaceMsg.classList.add('hidden-msg');
        deleteRaceNumberInput.classList.remove('wrong-data');
    } else {
        incorrectDeleteRaceMsg.classList.remove('hidden-msg');
        deleteRaceNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    
    return isValid;
}
function correctRaceData(raceNumber, firstWinner, secondWinner, thirdWinner) {
    var isValid = true;

    var numberInputPattern = /^[1-9]{1}[0-9]{0,10}$/;

    var raceNumberInput = document.getElementById('raceNumberInput'),
        firstNumberInput = document.getElementById('firstNumberInput'),
        secondNumberInput = document.getElementById('secondNumberInput'),
        thirdNumberInput = document.getElementById('thirdNumberInput');

    if (numberInputPattern.test(raceNumber)) {
        incorrectWinnersDataMsg.classList.add('hidden-msg');
        raceNumberInput.classList.remove('wrong-data');
    } else {
        incorrectWinnersDataMsg.classList.remove('hidden-msg');
        raceNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(firstWinner)) {
        incorrectWinnersDataMsg.classList.add('hidden-msg');
        firstNumberInput.classList.remove('wrong-data');
    } else {
        incorrectWinnersDataMsg.classList.remove('hidden-msg');
        firstNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(secondWinner)) {
        incorrectWinnersDataMsg.classList.add('hidden-msg');
        secondNumberInput.classList.remove('wrong-data');
    } else {
        incorrectWinnersDataMsg.classList.remove('hidden-msg');
        secondNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(thirdWinner)) {
        incorrectWinnersDataMsg.classList.add('hidden-msg');
        thirdNumberInput.classList.remove('wrong-data');
    } else {
        incorrectWinnersDataMsg.classList.remove('hidden-msg');
        thirdNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    
    return isValid;
}


showRacesButton.addEventListener('click', function() {
    adminContent.classList.add('hide');
    racesContent.classList.remove('hide');
});
determineTheWinnerButton.addEventListener('click', function() {
    racesContent.classList.add('hide');
    adminContent.classList.remove('hide');
});
closeMsgBox.addEventListener('click', function() {
    loader.classList.add('hidden');
    msgBox.classList.add('hidden');
});
determineTheWinnerProfileOption.addEventListener('click', function() {
    racesContent.classList.add('hide');
    adminContent.classList.remove('hide');
});
showRacesProfileOption.addEventListener('click', function() {
    adminContent.classList.add('hide');
    racesContent.classList.remove('hide');
});
logOutProfileOption.addEventListener('click', function() {
    userRequest('../AdminHandler', JSON.stringify({
        action: 'LOGOUT'
    }), (res) => {
        document.location = res.url;
    });
});

addRaceButton.addEventListener('click', function() {
    editRaceForm.classList.add('hide');
    deleteRaceForm.classList.add('hide');
    addRaceForm.classList.remove('hide');
});
editRaceButton.addEventListener('click', function() {
    deleteRaceForm.classList.add('hide');
    addRaceForm.classList.add('hide');
    editRaceForm.classList.remove('hide');
});
deleteRaceButton.addEventListener('click', function() {
    editRaceForm.classList.add('hide');
    addRaceForm.classList.add('hide');
    deleteRaceForm.classList.remove('hide');
});

determineTheWinnerForm.onsubmit = function() {
    if (!correctRaceData(determineTheWinnerForm.raceNumberInput.value, 
        determineTheWinnerForm.firstNumberInput.value,
        determineTheWinnerForm.secondNumberInput.value,
        determineTheWinnerForm.thirdNumberInput.value)) {
        return false;
    }

    determineTheWinnerForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('../AdminHandler', JSON.stringify({
        raceID: determineTheWinnerForm.raceNumberInput.value,
        firstWinner: determineTheWinnerForm.firstNumberInput.value,
        secondWinner: determineTheWinnerForm.secondNumberInput.value,
        thirdWinner: determineTheWinnerForm.thirdNumberInput.value,
        action: 'DETERMINE_THE_WINNER'
    }), displayRaces, showResMsg);

    determineTheWinnerForm.classList.remove('hidden');

    return false;
}
addRaceForm.onsubmit = function() {
    if (!correctAddRaceData(addRaceForm.raceNameInput.value,
        addRaceForm.raceCoefficientInput.value)) {
        return false;
    }

    addRaceForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('../AdminHandler', JSON.stringify({
        name: addRaceForm.raceNameInput.value,
        coefficient: addRaceForm.raceCoefficientInput.value,
        action: 'ADD_RACE'
    }), displayRaces, showResMsg);

    addRaceForm.classList.add('hide');
    addRaceForm.classList.remove('hidden');

    return false;
}
editRaceForm.onsubmit = function() {
    
    if (!correctEditRaceData(editRaceForm.raceNumberInput.value,
        editRaceForm.raceNameInput.value,
        editRaceForm.raceCoefficientInput.value,
        editRaceForm.firstNumberInput.value,
        editRaceForm.secondNumberInput.value,
        editRaceForm.thirdNumberInput.value)) {
        return false;
    }
    
    editRaceForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('../AdminHandler', JSON.stringify({
        raceID: editRaceForm.raceNumberInput.value,
        name: editRaceForm.raceNameInput.value,
        coefficient: editRaceForm.raceCoefficientInput.value,
        firstWinner: editRaceForm.firstNumberInput.value,
        secondWinner: editRaceForm.secondNumberInput.value,
        thirdWinner: editRaceForm.thirdNumberInput.value,
        action: 'EDIT_RACE'
    }), displayRaces, showResMsg);

    
    editRaceForm.classList.add('hide');
    editRaceForm.classList.remove('hidden');

    return false;
}
deleteRaceForm.onsubmit = function() {
    
    if (!correctDeleteRaceData(deleteRaceForm.raceNumberInput.value)) {
        return false;
    }
    
    deleteRaceForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('../AdminHandler', JSON.stringify({
        raceID: deleteRaceForm.raceNumberInput.value,
        action: 'DELETE_RACE'
    }), displayRaces, showResMsg);

    
    deleteRaceForm.classList.add('hide');
    deleteRaceForm.classList.remove('hidden');

    return false;
}

displayRaces();