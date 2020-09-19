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
              console.log('Error occurred',a);
              console.log('Error occurred',b);
              console.log('Error occurred',c);
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
           onAddRecordSuccess(data);
     },
     error: function(a,b,c) {
          console.log('Error occurred',a);
          console.log('Error occurred',b);
          console.log('Error occurred',c);
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




