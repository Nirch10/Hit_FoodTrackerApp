
function setSignUpBody(usernameId,passwordId){
    var jsonBody = {};
    jsonBody["UserName"] = $(usernameId).val();
    jsonBody["Password"] = $(passwordId).val();
    return jsonBody;
}

function login(){
    var reqBody = setSignUpBody("#userNameInputLogin","#passwordInputLogin")
    $.ajax({
         url: 'http://'+serverIp +':'+port+'/api/login',
         type: 'POST',
         dataType: 'json',
          beforeSend: function() {
                     $.mobile.showPageLoadingMsg(true);
                 },
                 complete: function() {
                     $.mobile.hidePageLoadingMsg();
                 },
         data: JSON.stringify(reqBody),
         success: function(data, textStatus, jqXHR){
              onLoginSuccess(data);
         },
         error: function(a,b,c) {
             $.mobile.hidePageLoadingMsg();
             document.getElementById("wrong-cred-lbl").style.visibility = 'visible';
             console.log('Error occurred',a);
         }
    });
}

function onLoginSuccess(user){
    initLoad();
    setLoggedUser(user);
    initUserRecords()
    goToHome();
}

function onLoginError(){
    initGenerics();
    goToLogin();
}

function signUp(){
    var jsonBodyReq = setSignUpBody("#new-username-input","#new-password-input");
    $.ajax({
         url: 'http://'+serverIp +':'+port+'/api/signup',
         type: 'POST',
         dataType: 'json',
          beforeSend: function() {
                     $.mobile.showPageLoadingMsg(true);
                 },
                 complete: function() {
                     $.mobile.hidePageLoadingMsg();
                 },
         data: JSON.stringify(jsonBodyReq),
         success: function(data, textStatus, jqXHR){
               document.getElementById("wrong-new-user").style.visibility = 'hidden';
               goToLogin();
         },
         error: function(a,b,c) {
          $.mobile.hidePageLoadingMsg();
            document.getElementById("wrong-new-user").style.visibility = 'visible';
            console.log('Error Occurred',a);
         }
    });
}