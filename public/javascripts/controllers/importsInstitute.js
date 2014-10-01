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
        
        $scope.name="hello";
        
        $scope.open = function(){
            var modalInstance = $modal.open({
                templateUrl: 'imp.html',
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
        
        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

    }


