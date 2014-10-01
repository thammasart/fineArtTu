var durableArticlesTick = [];
var durableGoodsTick = [];
var consumableGoodsTick = [];

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
	var materialCodeTickList;
	
	if(type=='durableArticles')
	{
		materialCodeTickList={'materialCodeTickList':durableArticlesTick};
		postData('/import/removeFSNCode',materialCodeTickList);
	}
	else if(type=='durableGoods')
	{
	
	}
	else
	{
	
	}
	
	
	
}