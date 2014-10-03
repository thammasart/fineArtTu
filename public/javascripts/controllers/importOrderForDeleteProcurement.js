var durableArticlesProcurementTick = [];
var goodsProcurementTick = [];

function addTick(name,type)
{
	
	var procurementID;
	
	if(type=='durableArticles')
	{
		procurementID = name;
		console.log(durableArticlesProcurementTick);
		if(durableArticlesProcurementTick.indexOf(procurementID) > -1){
			durableArticlesProcurementTick.remove(procurementID);
		}else{
			durableArticlesProcurementTick.push(procurementID);
		}
	}
	else if(type=='goods')
	{
		procurementID = name;
		console.log(goodsProcurementTick);
		if(goodsProcurementTick.indexOf(procurementID) > -1){
			goodsProcurementTick.remove(procurementID);
		}else{
			goodsProcurementTick.push(procurementID);
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
