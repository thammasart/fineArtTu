var durableArticlesTick = [];
var durableGoodsTick = [];
var consumableGoodsTick = [];


angular.module('insituteApp', ['ui.bootstrap'])
    .controller('delInsitute',function($scope,$modal){


        $scope.open = function(){

            var modalInstance = $modal.open({
                templateUrl: 'delInsitute.html',
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
    $scope.name = "Delete User(s).";
    $scope.userT= usersTick;
    $scope.ok = function () {
        removeUser();
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}


function addTick(name,type)
{
	
	var materialsName;
	
	if(type=='durableArticles')
	{
		materialsName = name;
		console.log(durableArticlesTick);
		if(durableArticlesTick.indexOf(materialsName) > -1){
			durableArticlesTick.remove(materialsName);
		}else{
			durableArticlesTick.push(materialsName);
		}
	}
	else if(type=='durableGoods')
	{
		materialsName = name;
		console.log(durableGoodsTick);
		if(durableGoodsTick.indexOf(materialsName) > -1){
			durableGoodsTick.remove(materialsName);
		}else{
			durableGoodsTick.push(materialsName);
		}
		
	}
	else
	{
		materialsName = name;
		console.log(consumableGoodsTick);
		if(consumableGoodsTick.indexOf(materialsName) > -1){
			consumableGoodsTick.remove(materialsName);
		}else{
			consumableGoodsTick.push(materialsName);
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
