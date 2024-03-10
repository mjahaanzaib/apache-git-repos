# Task
We would like you to use GitHub API to find, for the Apache organization (https://github.com/apache), the top 5 repositories with the most downloads.
For each of these repositories, we want you to save the top 10 contributors in a table in H2 database.

Table should contain the following information:
Repository Name - User Information (username, location, and company retrieved from the /users/{username} service) - Number of Commits.

For example:
repo: commons-lang, user: garydgregory, location: Denver, CO, USA, company: Rocket Software, contributions: 845

You can access the services to retrieve repository information at GitHub Repos API, and for user information at GitHub Users API.

We expect Java code, compiled with JDK 11 and packaged as a JAR file.
Additionally, provide a .bat file for executing the JAR."

Note: Please don’t forget to write unit tests. The code should be delivered within a maximum of 7 days.