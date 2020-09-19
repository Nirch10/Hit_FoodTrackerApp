var foodTypes = [];
var loggedUser = [];
var caloriesSum = 0;
records = [];
var serverIp = 0;
var port = 0;

function initLoad(){
    caloriesSum = 0;
    loggedUser = [];
    records = []
    document.getElementById("calories-sum").innerHTML = caloriesSum;
    document.getElementById("wrong-cred-lbl").style.visibility = 'hidden';
    document.getElementById("userNameInputLogin").value = '';
    document.getElementById("passwordInputLogin").value = '';
    document.getElementById("wrong-charts-update").style.visibility = 'hidden';
    document.getElementById("wrong-meal-added").style.visibility = 'hidden';
    document.getElementById("wrong-new-user").style.visibility = 'hidden';
}

function addNewRecordCalories(calories){
    caloriesSum += calories;
    document.getElementById("calories-sum").innerHTML = caloriesSum;
}

function load(server_ip, portNum){
    serverIp = server_ip;
    port = portNum;
}

function getFoodTypes(){return foodTypes;}

function getFoodTypesNames(){
    var results = [];
    foodTypes.forEach(function(type){
        results.push(type.Name);
    })
    return results;
}

function getLoggedUser(){return loggedUser;}

function setLoggedUser(user){loggedUser = user;}

function initFoodTypes(){
    $.ajax({
         url: 'http://'+serverIp+':'+port+'/api/home/getfoodtypes',
         type: 'GET',
         dataType: 'json',
         beforeSend: function() {
                     $.mobile.showPageLoadingMsg(true);
         },
         success: function(data, textStatus, jqXHR){
            $.mobile.hidePageLoadingMsg();
            foodTypes = data;
            addToDL('foodTypes-choose-list', getFoodTypes());
         },
         error: function(a,b,c) {
                      foodTypes = [];
                      console.log('something went wrong1:',a);
                      console.log('something went wrong2:',b);
                      console.log('something went wrong:3',c);
                 }
    });
};

function signOut(){
    initLoad();
    $("#records-list").empty();
    goToLogin();
}

function goToHome(){
    if(loggedUser == [])
        goToLogin();
    else
        window.location.href= "#home-page";
}

function goToAddMeal(){

    window.location.href= "#new-record-page";
}

function goToLogin(){

    window.location.href= "#login-page";
}

function goToSignUp(){

    window.location.href= "#sign-up-page";
}

function goToFoodType(){

    window.location.href= "#type-detailed-page";
}
