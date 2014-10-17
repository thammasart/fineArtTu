angular.module('exportRepairApp', ['ui.bootstrap'])
    .controller('exportDel',function($scope,$modal){

            $scope.openDelModal = function() {
                var delModalInstance = $modal.open({
                        templateUrl : 'cancelList.html',
                        controller : deleteModalInstanceCtrl,
                        size : 'lg'
                });
            };
        }
);

var deleteModalInstanceCtrl = function($scope, $modalInstance){
    $scope.name = "Delete List(s).";

   $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
}

