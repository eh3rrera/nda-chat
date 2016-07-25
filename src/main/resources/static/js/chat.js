$(document).ready(function() {
	var chatHistory = $('.chat-history');
	var chatHistoryList =  chatHistory.find('ul');
	var sendBtn = $('#send-btn');
	var ndaBtn = $('#request-nda-btn');
	var textarea = $('#message-to-send');
	
	function addMessage() {
		var messageToSend = textarea.val().trim();
		if(messageToSend !== '') {
			$.ajax({
				  method: 'POST',
				  url: '/chat/message',
				  contentType: 'application/json; charset=UTF-8',
				  data: JSON.stringify({ "message": messageToSend })
			})
			.done(function(msg) {
				  console.log(msg);
				  textarea.val('');
			}); 
		}
	}
	
	function scrollToBottom() {
		chatHistory.scrollTop(chatHistory[0].scrollHeight);
	}
	
	function addSystemMessage(message) {
		var template = Handlebars.compile($('#message-system-template').html());
        var params = { 
        	msg: message,
        };

        chatHistoryList.append(template(params));
        scrollToBottom();
	}
	
	scrollToBottom();

	var pusher = new Pusher(PUSHER_KEY, {
    	encrypted: true,
    	authEndpoint: '/chat/auth'
    });
	var presenceChannel = pusher.subscribe(CHANNEL);
	
	presenceChannel.bind('pusher:subscription_succeeded', function() {
		console.log(presenceChannel.members.me);
		addSystemMessage('You have joined the chat');
	});
	
	presenceChannel.bind('pusher:subscription_error', function(status) {
		alert('Subscription to the channel failed with status ' + status);
	});
	
	presenceChannel.bind('pusher:member_added', function(member) {
		console.log('pusher:member_added');
		addSystemMessage(member.info.name + ' has joined the chat');
	});
	
	presenceChannel.bind('pusher:member_removed', function(member) {
		console.log('pusher:member_removed');
		addSystemMessage(member.info.name + ' has left the chat');
	});
	
	presenceChannel.bind('new_message', function(data) {
		if(data.message !== '') {
			var templateEl = (presenceChannel.members.me.id === data.userId) 
								? $('#message-template') 
										: $('#message-response-template')
			var template = Handlebars.compile(templateEl.html());
	        var params = { 
	        	msg: data.message.replace(/(\r?\n)/g, '<br />'),
	        	name: data.userName,
	        	time: data.time
	        };

	        chatHistoryList.append(template(params));
	        scrollToBottom();
		}
    });
	
	presenceChannel.bind('system_message', function(data) {
		if(data.message !== '') {
			addSystemMessage(data.message);
		}
    });
	
	sendBtn.on('click', function() {
		addMessage();
	});
	
	ndaBtn.on('click', function() {
		$.ajax({
			  method: 'POST',
			  url: '/chat/request/nda'
		})
		.done(function(msg) {
			  console.log(msg);
		}); 
	});
	
	$( document ).ajaxStart(function() {
	  $('.loader').show();
	}).ajaxStop(function() {
	  $('.loader').hide();
	});
});