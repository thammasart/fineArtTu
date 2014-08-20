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


