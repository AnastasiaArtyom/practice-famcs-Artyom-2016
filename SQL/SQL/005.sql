SELECT Users.name FROM chat.Users WHERE Users.id IN (SELECT Messages.user_id FROM chat.Messages GROUP BY Messages.user_id HAVING COUNT(*) >= 3)
