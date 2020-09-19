jQuery(document).ready(function($){
    $.support.cors = true;
    $.mobile.allowCrossDomainPages = true;
    var login = new Login();
   //load('10.0.2.2', '1234');
    load('127.0.0.1', '1234');
    initFoodTypes();
});