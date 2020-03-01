var closeMsgBox = document.getElementById('closeMsgBox'),
    msgText = document.getElementById('msgText'),
    msgBox = document.getElementById('msgBox'),
    loader = document.getElementById('loader'),
    signInUserName = document.getElementById('signInUserName'),
    signUpUserName = document.getElementById('signUpUserName'),
    signInUserPassword = document.getElementById('signInUserPassword'),
    signUpUserPassword = document.getElementById('signUpUserPassword'),
    signUpConfirmUserPassword = document.getElementById('signUpConfirmUserPassword'),
    signUpUserEmail = document.getElementById('signUpUserEmail'),
    signInForm = document.getElementById('signInForm'),
    signUpForm = document.getElementById('signUpForm'),
    makeABetForm = document.getElementById('makeABetForm'),
    signInButton = document.getElementById('signInButton'),
    signUpButton = document.getElementById('signUpButton'),
    useSignInForm = document.getElementById('useSignInForm'),
    useSignUpForm = document.getElementById('useSignUpForm'),
    errorMsg = document.getElementsByClassName('error-msg'),
    errorInput = document.getElementsByTagName('input'),
    incorrectSignInMsg = document.getElementById('incorrectSignInMsg'),
    incorrectSignUpLoginMsg = document.getElementById('incorrectSignUpLoginMsg'),
    incorrectSignUpEmailMsg = document.getElementById('incorrectSignUpEmailMsg'),
    incorrectSignUpPasswordMsg = document.getElementById('incorrectSignUpPasswordMsg'),
    incorrectConfirmPasswordMsg = document.getElementById('incorrectConfirmPasswordMsg'),
    incorrectBetDataMsg = document.getElementById('incorrectBetDataMsg'),
    raceNumberInput = document.getElementById('raceNumberInput'),
    horseNumberInput = document.getElementById('horseNumberInput'),
    selectBetType = document.getElementById('selectBetType'),
    betAmountInput = document.getElementById('betAmountInput'),
    showRacesButton = document.getElementById('showRacesButton'),
    makeABetButton = document.getElementById('makeABetButton'),
    racesContent = document.getElementsByClassName('races-content')[0],
    betsContent = document.getElementsByClassName('bets-content')[0],
    shouldAuthorizedMsg = document.getElementsByClassName('should-authorized')[0],
    loginButton = document.getElementsByClassName('login-button')[0],
    content = document.getElementsByClassName('content')[0],
    login = document.getElementsByClassName('login')[0],
    userProfile = document.getElementsByClassName('user-profile')[0],
    makeABetProfileOption = document.getElementById('makeABetProfileOption'),
    showRacesProfileOption = document.getElementById('showRacesProfileOption'),
    addMoneyProfileOption = document.getElementById('addMoneyProfileOption'),
    addMoneyForm = document.getElementById('addMoneyForm'),
    closeAddMoneyForm = document.getElementById('closeAddMoneyForm'),
    closeSignInForm = document.getElementById('closeSignInForm'),
    closeSignUpForm = document.getElementById('closeSignUpForm'),
    logOutProfileOption = document.getElementById('logOutProfileOption'),
    racesTable = document.getElementById('racesTable'),
    betsTable = document.getElementById('betsTable'),
    userBalance = document.getElementById('userBalance'),
    profileUsername = document.getElementById('profileUsername'),
    betsDataOutput = document.getElementsByClassName('data-output')[0];


function userRequest(CustomerHandler, json, resolve, reject) {

    var xhr = new XMLHttpRequest();

    if (!xhr) {
        console.log('Unable to create XMLHTTP instance.');
        return false;
    }

    xhr.open('POST', CustomerHandler, true);
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

function correctSignUpData(login, email, password, confirmPassword) {
    var isValid = true;

    var loginPattern = /^[a-zA-ZА-Яа-я]{1}[0-9a-zA-ZА-Яа-я]{3,19}$/;
    var passwordPattern = /^[a-zA-ZА-Яа-я0-9\`\'\"\/\|]{4,20}$/;
    var emailPattern = /^[A-Za-z0-9._]+@[A-Za-z0-9]+.[A-Za-z.]{2,4}$/;

    if (loginPattern.test(login)) {
        incorrectSignUpLoginMsg.classList.add('hidden-msg');
        signUpUserName.classList.remove('wrong-data');
    } else {
        incorrectSignUpLoginMsg.classList.remove('hidden-msg');
        signUpUserName.classList.add('wrong-data');
        isValid = false;
    }

    if (passwordPattern.test(password)) {
        incorrectSignUpPasswordMsg.classList.add('hidden-msg');
        signUpUserPassword.classList.remove('wrong-data');
    } else {
        incorrectSignUpPasswordMsg.classList.remove('hidden-msg');
        signUpUserPassword.classList.add('wrong-data');
        isValid = false;
    }

    if (confirmPassword === password) {
        incorrectConfirmPasswordMsg.classList.add('hidden-msg');
        signUpConfirmUserPassword.classList.remove('wrong-data');
    } else {
        incorrectConfirmPasswordMsg.classList.remove('hidden-msg');
        signUpConfirmUserPassword.classList.add('wrong-data');
        isValid = false;
    }

    if (emailPattern.test(email)) {
        incorrectSignUpEmailMsg.classList.add('hidden-msg');
        signUpUserEmail.classList.remove('wrong-data');
    } else {
        incorrectSignUpEmailMsg.classList.remove('hidden-msg');
        signUpUserEmail.classList.add('wrong-data');
        isValid = false;
    }

    return isValid;
}
function correctBetData(raceNumber, horseNumber, betAmount) {
    var isValid = true;

    var numberInputPattern = /^[1-9]{1}[0-9]{0,10}$/;

    if (numberInputPattern.test(raceNumber)) {
        incorrectSignUpLoginMsg.classList.add('hidden-msg');
        raceNumberInput.classList.remove('wrong-data');
    } else {
        incorrectBetDataMsg.classList.remove('hidden-msg');
        raceNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(horseNumber)) {
        incorrectSignUpLoginMsg.classList.add('hidden-msg');
        horseNumberInput.classList.remove('wrong-data');
    } else {
        incorrectBetDataMsg.classList.remove('hidden-msg');
        horseNumberInput.classList.add('wrong-data');
        isValid = false;
    }
    if (numberInputPattern.test(betAmount)) {
        incorrectSignUpLoginMsg.classList.add('hidden-msg');
        betAmountInput.classList.remove('wrong-data');
    } else {
        incorrectBetDataMsg.classList.remove('hidden-msg');
        betAmountInput.classList.add('wrong-data');
        isValid = false;
    }
    

    return isValid;
}
function loginUser(res) {
    if (res.url) {
        document.location = res.url;
    }
    updateUser(res.user);
    login.classList.add('hide');
    userProfile.classList.remove('hide');
}

function updateUser(newUser) {
    user = newUser;

    if (!user.name) {
        user.name = '';
    }
    if (!user.balance) {
        user.balance = 0;
    }

    profileUsername.innerHTML = user.name;
    userBalance.innerHTML = user.balance;
}

function showResMsg(res) {
    msgText.innerHTML = res.msg;
    msgBox.classList.remove('hidden');
}
function displayRaces() {
    userRequest('CustomerHandler', JSON.stringify({
        action: 'GET_ALL_RACES'
    }), (res) => {
        for (var race of res.races) {
            displayRace(race);
        }
    });
}
function displayBets() {
    userRequest('CustomerHandler', JSON.stringify({
        action: 'GET_CUSTOMER_BETS'
    }), (res) => {
        betsTable.innerHTML = '';
        for (var bet of res.bets) {
            displayBet(bet);
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

function displayBet(bet) {
    var row = document.createElement("tr");
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");
    var td4 = document.createElement("td");
    var td5 = document.createElement("td");
    
    td1.appendChild(document.createTextNode(bet.id ? bet.id : ''));
    row.appendChild(td1);
    td2.appendChild(document.createTextNode(bet.raceID ? bet.raceID : ''));
    row.appendChild(td2);
    td3.appendChild(document.createTextNode(bet.betType ? bet.betType : ''));
    row.appendChild(td3);
    td4.appendChild(document.createTextNode(bet.horseNumber ? bet.horseNumber : ''));
    row.appendChild(td4);
    td5.appendChild(document.createTextNode(bet.betAmount ? bet.betAmount : ''));
    row.appendChild(td5);
    
    betsTable.appendChild(row);
}

useSignInForm.addEventListener('click', function() {
    signUpForm.classList.add('hide');
    signInForm.classList.remove('hide');
});
useSignUpForm.addEventListener('click', function() {
    signInForm.classList.add('hide');
    signUpForm.classList.remove('hide');
});

showRacesButton.addEventListener('click', function() {
    betsContent.classList.add('hide');
    racesContent.classList.remove('hide');
});
makeABetButton.addEventListener('click', function() {
    racesContent.classList.add('hide');
    betsContent.classList.remove('hide');
    if (userProfile.classList.contains('hide')) {
        shouldAuthorizedMsg.classList.remove('hide');
        makeABetForm.classList.add('hide');
    } else {
        shouldAuthorizedMsg.classList.add('hide');
        makeABetForm.classList.remove('hide');
    }
});
loginButton.addEventListener('click', function() {
    content.classList.add('hide');
    signUpForm.classList.add('hide');
    signInForm.classList.remove('hide');
});
closeMsgBox.addEventListener('click', function() {
    loader.classList.add('hidden');
    msgBox.classList.add('hidden');
});

makeABetProfileOption.addEventListener('click', function() {
    racesContent.classList.add('hide');
    betsContent.classList.remove('hide');
    if (userProfile.classList.contains('hide')) {
        shouldAuthorizedMsg.classList.remove('hide');
        makeABetForm.classList.add('hide');
    } else {
        shouldAuthorizedMsg.classList.add('hide');
        makeABetForm.classList.remove('hide');
    }
});
showRacesProfileOption.addEventListener('click', function() {
    betsContent.classList.add('hide');
    racesContent.classList.remove('hide');
});
addMoneyProfileOption.addEventListener('click', function() {
    addMoneyForm.classList.remove('hidden');
});
closeAddMoneyForm.addEventListener('click', function() {
    addMoneyForm.classList.add('hidden');
});
closeSignInForm.addEventListener('click', function() {
    signInForm.classList.add('hide');
    content.classList.remove('hide');
});
closeSignUpForm.addEventListener('click', function() {
    signUpForm.classList.add('hide');
    content.classList.remove('hide');
});
logOutProfileOption.addEventListener('click', function() {
    userRequest('CustomerHandler', JSON.stringify({
        action: 'LOGOUT'
    }), (res) => {
        updateUser({name: '', balance: 0});
        login.classList.remove('hide');
        userProfile.classList.add('hide');
    });
    betsTable.innerHTML = '';
    shouldAuthorizedMsg.classList.remove('hide');
    makeABetForm.classList.add('hide');
    betsDataOutput.classList.add('hide');
});

signUpForm.onsubmit = function() {
    if (!correctSignUpData(signUpForm.userName.value,
        signUpForm.userEmail.value,
        signUpForm.password.value, 
        signUpForm.confirmPassword.value)) {

        return false;
    }

    signUpForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('CustomerHandler', JSON.stringify({
        username: signUpForm.userName.value, 
        email: signUpForm.userEmail.value,
        password: signUpForm.password.value,
        userType: signUpForm.userType.value,
        action: 'REGISTER'
    }), loginUser, showResMsg);

    signUpForm.classList.add('hide');
    signUpForm.classList.remove('hidden');
    shouldAuthorizedMsg.classList.add('hide');
    makeABetForm.classList.remove('hide');
    content.classList.remove('hide');

    return false;
}

signInForm.onsubmit = function() {

    signInForm.classList.add('hide');
    loader.classList.remove('hidden');

    userRequest('CustomerHandler', JSON.stringify({
        email: signInForm.userEmail.value, 
        password: signInForm.password.value,
        userType: signInForm.userType.value,
        action: 'LOGIN'
    }), loginUser, showResMsg);

    signInForm.classList.add('hide');
    signInForm.classList.remove('hidden');
    shouldAuthorizedMsg.classList.add('hide');
    makeABetForm.classList.remove('hide');
    content.classList.remove('hide');
    betsDataOutput.classList.remove('hide');
    
    return false;
}

makeABetForm.onsubmit = function() {
    if (!correctBetData(makeABetForm.raceNumberInput.value,
        makeABetForm.horseNumberInput.value,
        makeABetForm.betAmountInput.value)) {
        return false;
    }
    
    makeABetForm.classList.add('hidden');
    loader.classList.remove('hidden');

    userRequest('CustomerHandler', JSON.stringify({
        raceNumber: makeABetForm.raceNumberInput.value, 
        betType: makeABetForm.betType.value,
        horseNumber: makeABetForm.horseNumberInput.value,
        betAmount: makeABetForm.betAmountInput.value,
        action: 'MAKE_A_BET'
    }), (res) => {
        addMoneyForm.classList.add('hidden');
        updateUser(res.user);
        displayBets();
    }, showResMsg);

    makeABetForm.classList.remove('hidden');

    return false;
}

addMoneyForm.onsubmit = function() {
    userRequest('CustomerHandler', JSON.stringify({
        action: 'ADD_MONEY',
        amount: addMoneyForm.addMoneyInput.value
    }), (res) => {
        addMoneyForm.classList.add('hidden');
        updateUser(res.user);
    }, showResMsg);

    addMoneyForm.classList.add('hidden');

    return false;
}

displayRaces();