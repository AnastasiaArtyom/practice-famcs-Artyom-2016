SELECT U.id, U.name, (SELECT COUNT(*) FROM Messages M WHERE U.id = M.user_id AND M.date = '2015-05-09') AS counts FROM Users U
