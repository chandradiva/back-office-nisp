var submit = true;
var command = 0;
var scriptArray = [ 'Encryptor1.1.js', 'PINBlock1.1.js', 'CP_PINBlock1.1.js', 'Encryptor1.1.js', 'BigInt1.1.js', 'SHA11.1.js', 'rsa1.1.js', 'jsbn1.1.js', 'jsbn21.1.js' ];

$(document).ready(function() {
	load_scripts(scriptArray);
});

function checkIn(form) {
	if (submit) {
		command = 0;
		submit = false;
		if (form.valid()) {

			disabledAllInput();
			getXstring(form);
		} else {
			submit = true;
			enabledAllInput();
		}
	}

}

function changeForm(form) {
	if (submit) {
		command = 1;
		submit = false;
		if (form.valid()) {
			disabledAllInput();
			getXstring(form);
		} else {
			submit = true;
			enabledAllInput();
		}
	}

}
function changeFormDialog(form, v_url) {
	if (submit) {
		command = 2;
		submit = false;
		if (form.valid()) {
			
			disabledAllInput();
			getXstringDialog(form,v_url);
		} else {
			
			submit = true;
			enabledAllInput();
		}
	}

}

function getXstringDialog(form,v_url) {
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "generateXString",
		dataType : "json",
        success: function(response){
			x_string = response.modulusString;
			GetEncryptedChgPasswordDialog(form,v_url);
		},
		error : function(response,status,error) {
			if(response.status==403){
				alert("Session Expired");
			}else{
//				alert("An Error Occured : " +status+ " nError: " +error);
				console.log("An Error Occured : " +status+ " nError: " +error);
			}
			
			enabledAllInput();
			postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [1]");
			inputClear();
		}

	});
}

function getXstring(form) {
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "generateXString",
		dataType : "json",
        success: function(response){
			x_string = response.modulusString;

//			getGenerateNumber(form);
			if (command == 0) {
				GetEncryptedPassword(form);
			} else if (command == 1) {
				GetEncryptedChgPassword(form);
			} else if (command == 2) {
				GetEncryptedChgPasswordDialog(form);
			}
		},
		error : function(response,status,error) {
			if(response.status==403){
				alert("Session Expired");
			}else{
//				alert("An Error Occured : " +status+ " nError: " +error);
				console.log("An Error Occured : " +status+ " nError: " +error);
			}
			
			enabledAllInput();
			postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [1]");
			inputClear();
		}

	});
}

function getGenerateNumber() {
	$.ajax({
		type : "GET",
		contentType : "application/json",
//		url : "generateRNumber",
		url : "generateRNo",
		dataType : "json",
		success : function(result) {
			//enabledAllInput();
			disabledAllInput();
			randNumber = result.randomNumber;
			randID = result.randomId;
			
			if(!randNumber && !randID){
				postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [2]");
				enabledAllInput();
			}else{
				$('#x_randomNumber').val(randNumber);
				$('#x_randomID').val(randID);
			}
			inputClear();
			
		},
		error : function(result) {
			// enabledAllInput();
			postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [3]");
			inputClear();
			disabledAllInputWithoutLoader();
		}

	});
}

function getGenerateNumberDialog() {
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "generateRNo",
		dataType : "json",
		success : function(result) {
//			disabledAllInput();
			randNumber = result.randomNumber;
			randID = result.randomId;
			
			if(!randNumber && !randID){
				postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [2]");
				enabledAllInput();
			}else{
				$('#x_randomNumber').val(randNumber);
				$('#x_randomID').val(randID);
			}
			inputClear();
			
		},
		error : function(result) {
			// enabledAllInput();
			postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [3]");
			inputClear();
			disabledAllInputWithoutLoader();
		}

	});
}

function GetEncryptedPassword(form) {
	var pin = '';
	var strc = '';
	var strp = '';
	pin = $('#passwordinput').val();
	//only applied for new DRIB
	var randNumber = '';
	randNumber = $('#x_randomNumber').val();
	try {
		var errorNum = createPINBlock(pin, randNumber);
		if (errorNum == 0) {
			strc = getEncryptedUserLoginMsg();
			strp = getEncodingParameter();
		}

		c_string = strc;
		p_string = strp;
		$('#x_cString').val(c_string);
		$('#x_pString').val(p_string);
		$('#x_randomNumber').val(randNumber);

		if (c_string && p_string && randNumber) {

			form.unbind('submit');
//			$(form).find('input#passwordinput').attr('disabled',true);
			form.submit();
		//Start added by bagus 20150509
		} else if(pin.length < 4){
			enabledAllInput();
			postNotifMassage("message_div", "danger", "Password invalid!");
			inputClear();
		//End added by bagus 20150509
		} else {
			postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [4]");
			enabledAllInput();
			inputClear();
		}
	} catch (e) {
		enabledAllInput();
		postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [5]");
		inputClear();
	}

}

function GetEncryptedChgPassword(form) {
	var pin = '';
	var newpin = '';
	var strc = '';
	var strp = '';
	pin = $('#oldPass').val();
	newpin = $('#newPass').val();
	if (pin == '' || newpin == '') {
		postNotifMassage("message_div", "danger", "Password and New Password cannot be empty");
	}

	var errorNum = CP_createPINBlock(pin, newpin, randNumber);

	if (errorNum == 0) {
		strc = getEncryptedUserLoginMsg();
		strp = getEncodingParameter();
	}

	c_string = strc;
	p_string = strp;
	$('#x_cString').val(c_string);
	$('#x_pString').val(p_string);
	$('#x_randomNumber').val(randNumber);

	if (c_string && p_string && randNumber) {

		form.unbind('submit');
		$(form).find('input#oldPass').attr('disabled',true);
		$(form).find('input#newPass').attr('disabled',true);
		$(form).find('input#confNewPass').attr('disabled',true);
		form.submit();		
	} else {
		enabledAllInput();
		postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [6]");
		inputClear();
	}

}

function GetEncryptedChgPasswordDialog(form,v_url) {
	var pin = '';
	var newpin = '';
	var strc = '';
	var strp = '';
	pin = $('#oldPass').val();
	newpin = $('#newPass').val();
	if (pin == '' || newpin == '') {
		postNotifMassage("message_div", "danger", "Password and New Password cannot be empty");
	}

	var errorNum = CP_createPINBlock(pin, newpin, randNumber);
	if (errorNum == 0) {
		strc = getEncryptedUserLoginMsg();
		strp = getEncodingParameter();
	}

	c_string = strc;
	p_string = strp;
	$('#x_cString').val(c_string);
	$('#x_pString').val(p_string);
	$('#x_randomNumber').val(randNumber);
	
	var confirmPin = $('#confNewPass').val();
//	var userCode = $('#userID').val();
//	var orgCode = $('#OrgID').val();
	
//	var token = {
//	    "struts.token.name": "token",
//	    "token": strutsToken,
//	    "paramsC":c_string,
//	    "paramsP":p_string,
//	    "randomNumber":randNumber,
//	    "passwordNew":newpin,
//	    "passwordConfirm":confirmPin,
//	    "password":pin,
//	    "userCode":userCode,
//	    "orgCode":orgCode
//	    
//	};
	
	if (c_string && p_string && randNumber) {
//		form.submit();
		$.ajax({
			url : v_url,
			data : form.serialize(),
			type : "POST",
//			dataType : "html",
			success : function(data) {
				if(data.userResponseFailedMessage!=null){
					postNotifMassage("message_div", "danger", data.userResponseFailedMessage);
				}else{
					postNotifMassage("message_div", "success", data.userResponseSuccessMessage);
				}
				jQuery.fancybox.close();
				

			},
			error : function(data) {
				console.log(data);
			}
		});

	} else {
		enabledAllInput();
		postNotifMassage("message_div", "danger", "System unavailable. Please try again later! [7]");
		inputClear();
	}

}

function disabledAllInput() {
	$('#imageLoader').show();
	
//	$('#userID').prop('disabled', true);
//	$('#passwordinput').prop('disabled', true);
	$('button').prop('disabled', true);
}

function disabledAllInputWithoutLoader() {
	$('#userID').prop('disabled', true);
	$('#passwordinput').prop('disabled', true);
	$('button').prop('disabled', true);
}

function enabledAllInput() {
//	$('#userID').prop('disabled', false);
//	$('#passwordinput').prop('disabled', false);
	$('button').prop('disabled', false);
}

function inputClear() {
	submit = true;
	$('#imageLoader').hide();
}