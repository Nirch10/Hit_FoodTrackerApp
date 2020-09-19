var foodTypes = [];
var loggedUser = [];
var caloriesSum = 0;
records = [];
var serverIp = 0;
var port = 0;

function initGenerics(){
    caloriesSum = 0;
    document.getElementById("calories-sum").innerHTML = caloriesSum;
    records = []
    loggedUser = [];
}

function addNewRecordCalories(calories){
    caloriesSum += calories;
    document.getElementById("calories-sum").innerHTML = caloriesSum;
}

function load(serverIpAddr, portNum){
    serverIp = serverIpAddr;
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

function initUserRecords(){
   $.when(getAllUserRecords()).done(function(results){
        results.forEach(function(res){
            var viewedT = setRecordForView(res);
            addToRecordsListView("records-list",viewedT);
                addNewRecordCalories(res.Calories);
        });
        return results;
});
};

function setRecordForView(res){
    var toView = {};
    toView["RecordId"] = res.RecordId;
    toView["Calories"] = res.Calories;
    toView["Description"] = res.Description;
    toView["FoodType"] = res.FoodType.Name;
    toView["Date"] = res.DateOfRecord;
    return toView;
}

function addToRecordsListView(id, item){
  var html = ' <div data-role="collapsible" id="'+item.RecordId
  +'" data-collapsed="true" >'
  +'<h3><label style="text-align:left;color:#007062 !important;">'
  +item.FoodType+' : </label><label style="color:#007062 !important;">' + item.Calories+'</label></h3>'
  +'<p style="color:#5EE6D5 !important;">Date :       '+ item.Date+'</p>'
  +'<p style="color:#5EE6D5 !important;">Description : '+ item.Description+'</p>'
  $("#"+id).append(html).collapsibleset('refresh');
}

function getAllUserRecords(){
    return $.when($.ajax({
         url: 'http://'+serverIp+':'+port+'/api/home/getuserrecords/' + loggedUser.UserId,
         type: 'GET',
         dataType: 'json',
          beforeSend: function() {
                     $.mobile.showPageLoadingMsg(true);
                 },
                 complete: function() {
                     $.mobile.hidePageLoadingMsg();
                 },
         }).then(function(res){
         return res;}));
};

function addToDL(id,array){
    var options = '';
    for(var i = 0; i < array.length; i++)
        options += '<option id="'+array[i].TypeId+'Exp" label="'+array[i].Name +'" value="'+array[i].TypeId+'">'+array[i].Name+'</option>';
    document.getElementById(id).innerHTML = options;
};
function signOut(){
    initGenerics();
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
