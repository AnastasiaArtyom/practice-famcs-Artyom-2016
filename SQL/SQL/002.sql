SELECT Users.name, Messages.text, Messages.date FROM chat.Users, chat.Messages WHERE Users.id=1 AND Messages.user_id=Users.id 
