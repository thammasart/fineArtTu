var durableArticlesProcurementTick = [];
var durableArticlesProcurementNameTick = [];
var goodsProcurementTick = [];
var goodsProcurementNameTick = [];
var orderType;
var deleteType;

angular.module('importOrderApp', ['ui.bootstrap'])
    .controller('delOrder',function($scope,$modal){

        $scope.open = function(mType){
            
            deleteType = mType;
            if(mType == 'durableArticles'){
                orderType= "Articles Order";
                deleteList = durableArticlesProcurementNameTick;
            }else  if(mType == 'goods'){
                orderType= "Goods Order";
                deleteList =goodsProcurementNameTick;
            }
            var modalInstance = $modal.open({
                templateUrl: 'delOrder.html',
                controller: resultModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    name : function(){
                        return $scope.name;
                    }
                }
            });
        };
    }
);

var resultModalInstanceCtrl= function($scope, $modalInstance){
    $scope.name = "Delete Order(s).";
    $scope.mt = "Delete " + orderType + " : ";
    $scope.dt = deleteList;
    $scope.deleteType = deleteType;

   $scope.ok = function () {
        removeProcurement(deleteType);
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}
function addTick(name,type,title)
{
	
	var procurementID;
	if(type=='durableArticles')
	{
		procurementID = name;
		console.log(durableArticlesProcurementTick);
		if(durableArticlesProcurementTick.indexOf(procurementID) > -1){
			
			durableArticlesProcurementTick.remove(procurementID);
			durableArticlesProcurementNameTick.remove(title);
			document.getElementById("row" + name).style.color = "";
			document.getElementById("check"+name).checked = false;
			
			
		}else{
			durableArticlesProcurementTick.push(procurementID);
			durableArticlesProcurementNameTick.push(title);
			document.getElementById("row" + name).style.color = "#cc3300";
			document.getElementById("check"+name).checked = true;

		}
	}
	else if(type=='goods')
	{
		procurementID = name;
		console.log(goodsProcurementTick);
		if(goodsProcurementTick.indexOf(procurementID) > -1){
			goodsProcurementTick.remove(procurementID);
			goodsProcurementNameTick.remove(title);
			document.getElementById("row2" + name).style.color = "";
			document.getElementById("check2"+name).checked = false;
		}else{
			goodsProcurementTick.push(procurementID);
			goodsProcurementNameTick.push(title);
			document.getElementById("row2" + name).style.color = "#cc3300";
			document.getElementById("check2"+name).checked = true;
		}
		
	}
	
}//end func

function removeProcurement(type){
	var sendData;

	
	if(type=='durableArticles')
	{
		
		sendData={
			'durableArticlesProcurementTickList':durableArticlesProcurementTick,
			'type' : type
		};
		console.log(durableArticlesProcurementTick);
		postData('/import/removeProcurement',sendData);
	}
	else if(type=='goods')
	{
		sendData={
			'goodsProcurementTickList':goodsProcurementTick,
			'type' : type
		};
		postData('/import/removeProcurement',sendData);
	}

	
	
}
