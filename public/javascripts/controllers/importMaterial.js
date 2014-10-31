var durableArticlesTick = [];
var durableArticlesNameTick = [];
var durableGoodsTick = [];
var durableGoodsNameTick = [];
var consumableGoodsNameTick = [];
var consumableGoodsTick = [];
var materialType ;
var deleteList;
var deleteType;

angular.module('materialApp', ['ui.bootstrap'])
    .controller('delMaterial',function($scope,$modal){

        $scope.open = function(mType){
            
            deleteType = mType;
            if(mType == 'durableArticles'){
                materialType = "Durable Articles";
                deleteList = durableArticlesNameTick;
            }else  if(mType == 'durableGoods'){
                materialType = "Durable Goods";
                deleteList = durableGoodsNameTick;
            }else if(mType == "consumableGoods"){
                materialType = "Consumable Goods";
                deleteList = consumableGoodsNameTick;
            }

            var modalInstance = $modal.open({
                templateUrl: 'delMaterial.html',
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
    $scope.name = "Delete Material(s).";
    $scope.mt = "Delete " + materialType + " : ";
    $scope.dt = deleteList;
    $scope.deleteType = deleteType;

   $scope.ok = function () {
        removeMaterialCode(deleteType);
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}


function addTick(name,type,description)
{
	
	var materialsName;
	
	if(type=='durableArticles')
	{
		materialsName = name;
		console.log(durableArticlesTick);
		if(durableArticlesTick.indexOf(materialsName) > -1){
			durableArticlesTick.remove(materialsName);
			durableArticlesNameTick.remove(description);
			document.getElementById("row" + name).style.color = "";
			document.getElementById(name).checked = false;
		}else{
			durableArticlesTick.push(materialsName);
			durableArticlesNameTick.push(description);
			document.getElementById("row" + name).style.color = "#cc3300";
			document.getElementById(name).checked = true;
		}
	}
	/*
	else if(type=='durableGoods')
	{
		materialsName = name;
		console.log(durableGoodsTick);
		if(durableGoodsTick.indexOf(materialsName) > -1){
			durableGoodsTick.remove(materialsName);
			durableGoodsNameTick.remove(description);
			document.getElementById("row" + name).style.color = "";
			document.getElementById(name).checked = false;
		}else{
			durableGoodsTick.push(materialsName);
			durableGoodsNameTick.push(description);
			document.getElementById("row" + name).style.color = "#cc3300";
			document.getElementById(name).checked = true;
		}
		
	}
	*/
	else
	{
		materialsName = name;
		console.log(consumableGoodsTick);
		if(consumableGoodsTick.indexOf(materialsName) > -1){
			consumableGoodsTick.remove(materialsName);
			consumableGoodsNameTick.remove(description);
			document.getElementById("row" + name).style.color = "";
			document.getElementById(name).checked = false;
		}else{
			consumableGoodsTick.push(materialsName);
			consumableGoodsNameTick.push(description);
			document.getElementById("row" + name).style.color = "#cc3300";
			document.getElementById(name).checked = true;
		}
		
	}
	
}//end func


function removeMaterialCode(type){
	var sendData;
	
	if(type=='durableArticles')
	{
		sendData={
			'materialCodeTickList':durableArticlesTick,
			'type' : 'durableArticles'
		};
		postData('/import/removeFSNCode',sendData);
	}
	else if(type=='durableGoods')
	{
		sendData={
				'materialCodeTickList':durableGoodsTick,
				'type' : 'durableGoods'
		};
		postData('/import/removeCode',sendData);
	}
	else
	{
		sendData={
				'materialCodeTickList':consumableGoodsTick,
				'type' : 'consumableGoods'
				};
		postData('/import/removeCode',sendData);
	}
	
	
	
}
