$(document).ready(function() {
	$('#create-chat-form').validate({
		rules: {
			chatDescription: 'required',
			userName: 'required',
			chatName: {
				required: true,
				remote: '/chat/validate/name'
			},
			userEmail: {
				required: true,
				email: true
			}
		},
		messages: {
			chatDescription: 'Please enter a description',
			userName: 'Please enter your name',
			chatName: {
				required: 'Please enter a name',
				remote: jQuery.validator.format("{0} is already in use")
			},
			userEmail: 'Please enter a valid email address'
		}
	});

	$('#join-chat-form').validate({
		rules: {
			idChat: 'required',
			userName: 'required',
			userEmail: {
				required: true,
				email: true
			}
		},
		messages: {
			idChat: 'Please choose the chat to join',
			userName: 'Please enter your name',
			userEmail: 'Please enter a valid email address'
		}
	});
});