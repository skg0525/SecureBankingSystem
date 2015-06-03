SofwareSecurityProject
======================

Functionalities implemented:

1. UI pages
2. DB structure (Account table is present in mysql.sql script, but no use of it is done till 10/24/2014.)
3. Register & Login (Login doesn't work right now because the password is being hashed and stored by a method in DAOImpl package. The original login method does checks the password in plain-text and validates. Register page doesn't show the error message for incorrect captcha input).
4. In the admin functionality, in the remove user module, the place where the admin can view the user, a text box is kept in place. It retrieves the user information from the DB -> This is where DTO comes into picture.

TODO:
1. Implement External User functionalities. Use OTP or likewise (maybe)
2. Implement Internal Users and Admin functionalities.
3. Make the UI more appealing.
4. Add the <sec:authorize> stuff on EVERY page, so that only permitted users are allowed to view stuff.