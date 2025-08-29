# Bajaj Java Test – Spring Boot Application

## 📌 Overview
This Spring Boot application is built to solve the **Bajaj Finserv Health Java Placement Test**.  

### Steps Implemented:
1. On application startup, it calls the **generateWebhook API** with candidate details (`name`, `regNo`, `email`).  
2. The API responds with:
   - A unique **webhook URL** (to submit the solution).  
   - An **accessToken (JWT)** for authorization.  
3. Based on my registration number ((22BCE9312)even → Question 2), the application has the SQL Query to the question asked.  
4. The SQL query is submitted back to the **webhook URL** using the **Authorization: <JWT>** header.  
5. The server response (success/failure) is printed to the console.  

---

## 📝 SQL Solution (Question 2)

The problem:  
*For each employee, return the number of employees in the same department who are younger than them, along with their department name. Output ordered by employee ID in descending order.*  

### Final Query:
```sql
SELECT 
    e1.EMP_ID,
    e1.FIRST_NAME,
    e1.LAST_NAME,
    d.DEPARTMENT_NAME,
    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e1
JOIN DEPARTMENT d 
    ON e1.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE e2 
    ON e1.DEPARTMENT = e2.DEPARTMENT
    AND e2.DOB > e1.DOB
GROUP BY 
    e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY 
    e1.EMP_ID DESC;
```

# RAW downloadable GitHub link to the JAR:
```link
https://github.com/SwenZ77/bajaj-java-test/raw/refs/heads/main/target/bajaj-java-test-0.0.1-SNAPSHOT.jar
```
