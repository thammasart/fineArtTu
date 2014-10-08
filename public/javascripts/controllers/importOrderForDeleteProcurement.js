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
		}else{
			durableArticlesProcurementTick.push(procurementID);
			durableArticlesProcurementNameTick.push(title);
		}
	}
	else if(type=='goods')
	{
		procurementID = name;
		console.log(goodsProcurementTick);
		if(goodsProcurementTick.indexOf(procurementID) > -1){
			goodsProcurementTick.remove(procurementID);
			goodsProcurementNameTick.remove(title);
		}else{
			goodsProcurementTick.push(procurementID);
			goodsProcurementNameTick.push(title);
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
