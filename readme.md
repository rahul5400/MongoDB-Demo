# How to run
To run this locally, you'll need an instance of MongoDB Atlas and an appropriate .env file containing connection links.
Create a file named `.env` at the project root containing:
```
DATABASE_URL=mongodb+srv://cisc:<connection_information_here>.mongodb.net/?retryWrites=true&w=majority&appName=<cluster_name_here>
```
The database URL will need to connect to a database / namspace named `cisc` with read and write privileges to a collection called `users`.

# Driver docs
https://www.mongodb.com/docs/drivers/java/sync/current/

# Filters reference
https://www.mongodb.com/docs/drivers/java/sync/v5.2/fundamentals/builders/filters/
