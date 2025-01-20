Hi there !

API docs:

1. Get Authors List

Endpoint:
GET http://localhost:8080/api/v1/authors?q=Jane&page=1&size=5

Query Parameters:
q (optional): Search query for the author's name.
page (optional): The page number for pagination. Default is 0.
size (optional): The number of records per page. Default is 10.
Description:
Fetch a list of authors based on the search query (q):

If authors matching the query exist in the database, the data is retrieved from the database.
If no authors are found, the system queries the OpenLibrary API for matching authors. 
The returned data is saved to the database after ensuring no duplicates exist.

2. Get Author's Works
   
Endpoint:
GET http://localhost:8080/api/v1/authors/OL12345A?page=0&size=10

Path Variables:

authorKey: The unique identifier for the author (e.g., OL12345A).
Query Parameters:

page (optional): The page number for pagination. Default is 0.
size (optional): The number of records per page. Default is 10.
Description:
Fetch a list of works by a specific author identified by authorKey:

If the author exists in the database, their works are checked:
If the works are already in the database, the data is returned.
If no works are found, the system queries the OpenLibrary API for the author's works and saves the data to the database.