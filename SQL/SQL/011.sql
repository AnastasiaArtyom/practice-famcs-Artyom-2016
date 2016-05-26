SELECT DATEDIFF (CURDATE(), (SELECT Messages.date FROM Messages WHERE Messages.id = (SELECT MIN(id) FROM Messages)))

