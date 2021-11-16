# Welcome to the CarSaver Code Review Project!

First off, congratulations! You've made it to the next level of our hiring process.

This is a fairly bare-bones Spring Boot application. Some mistakes have intentionally been introduced. Some may have been unintentionally introduced (we're only human 🤷 ). 

The application can be run in all the usual ways:

- `mvn spring-boot:run`

- `./mvnw spring-boot:run`

- An IDE that makes you feel comfortable and productive

Keep an eye out for code smells, potential errors, code style inconsistencies etc.  The point of this code review 
exercise is to **spawn conversation**, not necessarily to test your ability to "find all the bugs".  Some things
you may find are subjective, and that's okay.  We'd love to hear your opinion on those things.

Also, If you'd like, you can attempt to fix the problems you see using a Pull Request. This is not required but might provide a good framework for our conversation.

If you decide to do a Pull Request, please fork the repo following the instructions [here](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork).

---
**NOTE**

The application is using an H2 in memory database. The console for which can be accessed at http://localhost:8080/h2-console 

JDBC URL: jdbc:h2:mem:codereview

Username: admin

Password: password

---

**Notes from the Reviewer**
I've added some comments on what changes I put in place throughout the code.  Additionally, there need to be tests 
written to exercise the business logic for accuracy.  I would specifically like to see tests on the createUser and  
updateUserLocation endpoints within the UserApiController class, as well as the updateEmail and getNames methods on UserService 
(assuming those are still necessary)