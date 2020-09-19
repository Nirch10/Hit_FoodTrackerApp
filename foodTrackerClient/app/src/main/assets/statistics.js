$(document).on('pageshow', '#type-detailed-page', function(){
    var chart;
    $.when(getAllUserRecords()).done(function(results){
        var chartData = setPieGroupsAndValues(results);
        createChart(chartData);
        return results;
    });
});

function updateChartWithNewDates(){
    var fromDate = document.getElementById("from-date").value;
    var toDate = document.getElementById("to-date").value;
    getTransactionsByDateAndUpdate(fromDate, toDate);
};

function createChart(chartData){
    var dataToShow = getCurrentFoodTypesNames(chartData);
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container',
                type: 'bar',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: 'Meals count',
                x: -20 //center
            },
            subtitle: {
                text: 'Your meals count split by categories',
                x: -20
            },
            xAxis: {
                categories: dataToShow
            },
            yAxis: {
                title: {
                    text: 'Meals count'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'';
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -10,
                y: 100,
                borderWidth: 0
            },
            series: [{
                name: 'Category',
                data: getChartsValue(chartData)
            }]
        });
    });
}

function groupBy(list, keyGetter) {
    const map = new Map();
    list.forEach((item) => {
         const key = keyGetter(item);
         const collection = map.get(key);
         if (!collection) {
             map.set(key, [item]);
         } else {
             collection.push(item);
         }
    });
    return map;
}

function setPieGroupsAndValues(records){
    var results = [];
    var groupedData = groupBy(records, record => record.FoodType.TypeId)
    groupedData.forEach(function(group){
       results.push({label:group[0].FoodType.Name, length: group.length})
    });
    return results;
}

function getChartsValue(array){
    var results = []
    array.forEach(function(cat){
        results.push(cat.length)
    });
    return results;
}

function datePick() {
    $('.date-picker').datepicker( {
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'MM yy',
        onClose: function(dateText, inst) {
            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            $(this).datepicker('setDate', new Date(year, month, 1));
        }
    });
}

function getCurrentFoodTypesNames(chartData){
    var results = []
    chartData.forEach(function(ret){
        results.push(ret.label);
    })
    return results;
}

function onGetByDatesSuccess(data){
    var chartData = setPieGroupsAndValues(data);
    createChart(chartData);
}

function getTransactionsByDateAndUpdate(fromDate, toDate){
  $.ajax({
     url: 'http://'+serverIp +':'+port+'/api/home/getrecordsbydate?userId='
     + getLoggedUser().UserId + '&fromDate=' + fromDate + '&toDate='+toDate,
     type: 'Get',
     dataType: 'json',
      beforeSend: function() {
                 $.mobile.showPageLoadingMsg(true);
             },
             complete: function() {
                 $.mobile.hidePageLoadingMsg();
             },
     success: function(data, textStatus, jqXHR){
           onGetByDatesSuccess(data);
     },
     error: function(a,b,c) {
          console.log('something went wrong1:',a);
          console.log('something went wrong2:',b);
          console.log('something went wrong:3',c);
     }
   });
}