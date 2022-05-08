# DD2477-project-group14

**Book Recommendation Engine**


Frontend:
1. go to working directory `<repo>/code/frontend/vue-frontend`
2. run `npm install` in terminal
3. run `npm run serve` in terminal
4. go to http://localhost:8080/

Backend:
1. download elasticsearch 7.6.2
2. Append line `indices.query.bool.max_clause_count: 4096` to file elasticsearch.yml
3. run `elasticsearch.bat` in `elasticsearch-7.6.2\bin`
4. Add `<repo>\code\tianyu-es-bs\pom.xml` as a maven project if not yet
5. go to `<repo>/code/tianyu-es-bs/src/main/java/com/tianyu/service/BookContentService.java`
    - change `/Users/filipkana/Documents/Skolarbete/DD2477/irProject/Data/dataset` to `<repo>/Data/dataset`
6. go to `<repo>/code/tianyu-es-bs/src/main/java/com/tianyu/pojo/User.java`
      - change `/Users/filipkana/Documents/Skolarbete/DD2477/irProject/code/tianyu-es-bs/src/main/resources/stopWords.txt` to `<repo>/code/tianyu-es-bs/src/main/resources/stopWords.txt`
    - change `/Users/filipkana/Documents/Skolarbete/DD2477/irProject/code/tianyu-es-bs/src/main/resources/patterns.txt` to `<repo>/code/tianyu-es-bs/src/main/resources/patterns.txt`
7. run `ElasticSearchBookSearchApplication.java` in `<repo>/code/tianyu-es-bs/src/main/java/com/tianyu`

Indexing:
1. Go to ´src/test/java/com/tianyu/BookContentServiceTest.java´
2. Run test 'contextloads' to insert all books into elasticsearch

Running:
Test the program from localhost:8080

