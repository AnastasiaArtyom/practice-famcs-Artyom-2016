SELECT Users.name, Messages.text, Messages.date FROM chat.Users, chat.Messages WHERE Messages.user_id=Users.id AND  Users.id=2 AND Messages.date='2015-05-09'
