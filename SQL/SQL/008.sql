SELECT Users.name, Messages.text, Messages.date FROM chat.Users, chat.Messages WHERE Messages.user_id=Users.id AND Users.id=2 GROUP BY Messages.date ASC
