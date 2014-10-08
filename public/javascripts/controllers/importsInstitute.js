var institutesTick = [];

function addTick(name){
	var instituteName = name;
	console.log(institutesTick);
	if(institutesTick.indexOf(instituteName) > -1){
		institutesTick.remove(instituteName);
	}else{
		institutesTick.push(instituteName);
	}
}


function removeInstitute(){
	var institutesTickList = {
			'institutesTickList'	: institutesTick
	};
	console.log('aaaaaaaaaaaaaaaa');
	postData('/import/removeInstitute',institutesTickList);
}

angular.module('importsInstituteApp', ['ui.bootstrap'])
    .controller('importsInstituteCtrl',function($scope,$modal){
    	$scope.name = "asd";
    	
        $scope.open = function(id){
//            $http({method : 'GET',url : 'autoCompleteGood' })
//            .success(function(result){
//
//            });
            var modalInstance = $modal.open({			//ดึงโครงกับแจกไอดี
                templateUrl: 'imp.html',				//
                controller: resultModalInstanceCtrl,	//func 
                size: 'lg',
                resolve: {
                    name : function(){
                        return $scope.name;
                    }
                }
            });
            modalInstance.id = id;	//send data to controller
        };
    }
);

	
	
    var resultModalInstanceCtrl= function($scope, $modalInstance){ //func ดึงข้อมูลมาโชว์
    	var obj = {
    			id : $modalInstance.id
    	}
    	$.ajax({
    		url:'/import/institute/retriveInstituteDescription',
    	    type: 'post',
    	    data: JSON.stringify(obj),
    	    contentType: 'application/json',
    	    dataType: 'json',
        	success: function(result){
        		$scope.typeEntrepreneur=result.typeEntrepreneur;
        		$scope.name = result.nameEntrepreneur;
        		$scope.typedealer=result.typedealer;
        		$scope.nameDealer=result.nameDealer;
        		$scope.payCodition=result.payCodition;
        		$scope.payPeriod=result.payPeriod;
        		$scope.sendPeriod=result.sendPeriod;
        		$scope.durableArticlesType=result.durableArticlesType.split(",");
        		$scope.durableGoodsType=result.durableGoodsType.split(",");
        		$scope.buildingNo = result.buildingNo;
        		$scope.village = result.village;
        		$scope.alley = result.alley;
        		$scope.road = result.road;
        		$scope.parish = result.parish;
        		$scope.district = result.district;
        		$scope.province = result.province;
        		$scope.telephoneNumber = result.telephoneNumber;
        		$scope.fax = result.fax;
        		$scope.postCode = result.postCode;
        		$scope.email = result.email;
        	}
    	});
        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

    }


