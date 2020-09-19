$(document).on("change","select",function(){
  $('#'+this.value+'Exp',this)
  .attr("selected", true).siblings()
  .removeAttr("selected")
});

function removeRecord(id){
    $.ajax({
         url: 'http://'+serverIp +':'+port+'/api/home/deleterecord/'+ id,
         type: 'DELETE',
         dataType: 'json',
         headers:{
            "Access-Control-Request-Origin": "*",
         },
          beforeSend: function() {
                     $.mobile.showPageLoadingMsg(true);
                 },
                 complete: function() {
                     $.mobile.hidePageLoadingMsg();
                 },
         success: function(data, textStatus, jqXHR){
               onDeleteRecordSuccess(data, id);
         },
         error: function(a,b,c) {
              console.log('Error occurred',a,b,c);
         }
    });
}

function buildJsonBodyReq(){
    var user = getLoggedUser();
    var foodTypeId = document.querySelector('#foodTypes-choose-list').value;
    var calories = $("#calories-input").val();
    var description = $("#description-input").val();
    var jsonData = {};
    jsonData["User"] = {"UserId": user.UserId};
    jsonData["FoodType"] = {"TypeId" : foodTypeId};
    jsonData["Calories"] = calories;
    jsonData["Description"] = description;
    return jsonData;
}

function addMeal(){
    jsonBodyReq = buildJsonBodyReq();
    $.ajax({
     url: 'http://'+serverIp +':'+port+'/api/home/addrecord',
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
        document.getElementById("wrong-meal-added").style.visibility = 'hidden';
        onAddRecordSuccess(data);
     },
     error: function(a,b,c) {
        document.getElementById("wrong-meal-added").style.visibility = 'visible';
        $.mobile.hidePageLoadingMsg();
        console.log('Error occurred',a);
     }
   });
}

function onDeleteRecordSuccess(data, id){initUserRecords();}

function onAddRecordSuccess(data){
    var viewedT = setRecordForView(data);
    addToRecordsListView("records-list",viewedT);
    addNewRecordCalories(viewedT.Calories);
    goToHome();
}


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




