SELECT Users.name, Messages.text, Messages.date FROM chat.Users, chat.Messages WHERE Users.id=2 AND Messages.user_id=Users.id AND Messages.text  LIKE '%hello%'
