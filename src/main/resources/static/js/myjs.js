
function toggler(){
	
	if($('.sidebar').is(":visible")){
		//if side bar is visible then do hide 
		
		$('.sidebar').css('display','none');
		$('.content').css('margin-left','1%');
		
		
	}else{
		//if side bar is hide then  do visible 
	    $('.sidebar').css('display','block');
		$('.content').css('margin-left','20%');
		
		
		
	}
	
	
}

function search(){
	
	//get value of search bar 
	
let result=$('#search-input').val();


if(result==''){
	$(".search-result").hide();
	
}else{
	
	//sending request to server  by the help of fetch Api
	
	let url=`http://localhost:9999/search/${result}`;
	
	
	
	fetch(url).then((response) =>{
		     return  response.json();
		     
		      
		
		
	}).then((data)=>{
		
		console.log(data);
		let text=`<div class='list-group'>`;
		 
		data.forEach(contact=>{
			
			text+=`<a href="/users/view-profile/${contact.cid}" class="list-group-item list-group-item-action">${contact.name}</a>`
			
		});
		
		
		
		text+=`</div>`;
		
		$(".search-result").html(text);
		$(".search-result").show();
		
		
	});
	
	
	$(".search-result").show();
	
}


	
	
}

function imagechange(){
   	$('#fileinput').trigger('click'); 


}




function handleCheckOut(){

  let amount=$('#check_out').val();
  console.log(amount)
  if(amount=="" || amount==null){
	alert("amount is  required");
  }

  //send request to server to create order

  $.ajax(

     {
        url:'/users/create_order',
		data:JSON.stringify({amount:amount}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response){

			if(response.status=="created"){
				//open form of razor pay

				let options={

                    key:'rzp_test_U1SGGFHqPvoBY1',
					amount:response.amount,
					currency:"INR",
					name:'smart contact manager',
					description:'Donation',
					order_id:response.id,
					handler:function(response){
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						console.log('payment successfull');
						alert("payement successfully");
					},

					"prefill": {
						"name": "",
						"email": "",
						"contact": ""
						},

						 "notes": {

							"address": "Razorpay Corporate Office"}
							,
							 "theme":{
							 "color": "#3399cc"

							}


				};

				let rzp=new Razorpay(options);
				 rzp1.on('payment.failed', function (response){
					alert(response.error.code);
					alert(response.error.description);
					alert(response.error.source);
					alert(response.error.step);
					alert(response.error.reason);
					alert(response.error.metadata.order_id);
					alert(response.error.metadata.payment_id);
					});
               
					rzp.open();


			}


		},
		error:function(error){

		}


	 }



  )
 

}



















