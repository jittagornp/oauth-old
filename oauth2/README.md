OAuth 2.0
=================

ตัวอย่างการเขียน OAuth 2.0 - Java Spring-Boot

Implement ตาม Spec `RFC6749` : https://tools.ietf.org/html/rfc6749

> OAuth 2.0 (Open Authentication version 2.0) เป็น Framework/วิธีการ ที่อนุญาตให้ Application ใดๆ (Third-Party Application) สามารถเข้าถึงข้อมูล/ทรัพยากรของผู้ใช้งานระบบ (Resource Owner) ของ Application หนึ่ง ผ่าน Http Service ด้วยการอนุญาตจากเจ้าของทรัพยากรนั้น  

# Core

- [OAuth 2.0 Framework](https://tools.ietf.org/html/rfc6749) - RFC 6749
- [Bearer Token Usage](https://tools.ietf.org/html/rfc6750) - RFC 6750
- [Threat Model and Security Considerations](https://tools.ietf.org/html/rfc6819) - RFC 6819 

# บทความ
ผมเขียนบทความไว้ที่ [แนวทางปฏิบัติที่ดี ในการทำ OAuth 2.0 Access Token & Refresh Token เพื่อความปลอดภัย](https://medium.com/@jittagornp/best-practice-%E0%B9%83%E0%B8%99%E0%B8%81%E0%B8%B2%E0%B8%A3%E0%B8%97%E0%B8%B3-oauth2-access-token-refresh-token-457ae3bee4b7)  

# DEMO

> https://oauth2-pamarin.herokuapp.com/ 

# Requires
- Java 8+ http://www.oracle.com/technetwork/java/javase/downloads/index.html 
- Apache Maven 3.0.4+ https://maven.apache.org/ 
- Mysql https://www.mysql.com/ หรืออาจจะใช้ Docker https://store.docker.com/images/mysql  
- Redis https://redis.io/ (Windows สามารถ download ได้ที่ https://github.com/MicrosoftArchive/redis/releases) หรืออาจจะใช้ Docker https://store.docker.com/images/redis  

# Pre Build & Run

- ต้อง ติดตั้งและ set up `redis` ก่อน เป็น `localhost:6379` (default port)
- ต้อง ติดตั้งและ set up `mysql` ก่อน เป็น `localhost:3306` (default port) และ create database ชื่อ oauth2 โดยมี username/password เป็น test/password สิทธิ์ `grant all privileges` (สำหรับ test เท่านั้น) 

# การติดตั้ง Redis บน Docker
ถ้าใช้ Windows ให้ downlaod docker จาก https://store.docker.com/editions/community/docker-ce-desktop-windows  

จากนั้น install redis 

```shell
$ docker run --name oauth2-redis -d -p 6379:6379 redis:latest
```

เราจะ bind port 6379 ของเครื่องไปที่ redis container port 6379 เช่นกัน   

# การติดตั้ง Mysql บน Docker

```shell
$ docker run --name oauth2-mysql -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 mysql:latest
```

วิธีเดียวกับ การติดตั้ง redis บน docker ซึ่งเราจะ set root password เป็น `password` (ตั้งง่ายๆ ไว้ทดสอบเฉยๆ)   

การสร้าง database

```shell
$ mysql -u root -p
Enter password: ********

> create database oauth2;
```
การสร้างและกำหนดสิทธิ์ user

```shell
> create user 'test' identified by 'password';  
> grant all privileges on oauth2.* to 'test';  
```
การเตรียมข้อมูลเริ่มต้น ให้ run sql script นี้  

> [src/main/resources/sql/init.sql](src/main/resources/sql/init.sql)  

# วิธีการ Run Project

1. build parent

> $ mvn clean install -U

2. build OAuth2

> $ cd oauth2  
> $ mvn clean install -P error-prone

3. run
> $ mvn spring-boot:run

# Http Session

- ตั้งชื่อเป็น ssid   
- เก็บไว้ใน Redis  
- [Session Management Cheat Sheet](https://www.owasp.org/index.php/Session_Management_Cheat_Sheet) 

# Token  

- Authorization Code เป็น Stateless Sign/Verify ด้วย JWT โดยใช้ RSA Algorithm https://github.com/auth0/java-jwt และจะหมดอายุภายใน 1 นาที  
- Access Token เป็น Stateful เก็บไว้ใน Redis และใช้ Hash-Based Token ในการ generate ซึ่งจะหมดอายุภายใน 30 นาที
- Refresh Token เป็น Stateful เก็บไว้ใน Redis และใช้ Hash-Based Token ในการ generate ซึ่งจะหมดอายุภายใน 2 สัปดาห์ ถ้าไม่ได้ใช้  

# วิธีการ Generate Key Pairs (public key/private key)
ในกรณีที่ต้องการ generate key pairs ใหม่ https://github.com/jittagornp/guideline/blob/master/how_to_generate_keypairs.md  

# CSRF Token

Generate csrf token ด้วยวิธีเดียวกับที่ Rails (Github และ Gitlab) ใช้ https://medium.com/rubyinside/a-deep-dive-into-csrf-protection-in-rails-19fa0a42c0ef ซึ่งการเปรียบเทียบ (Compare) token มีการป้องกัน [Timing Attacks](https://thisdata.com/blog/timing-attacks-against-string-comparison/) ด้วย 

> [src/main/java/com/pamarin/oauth2/security/DefaultAuthenticityToken.java](src/main/java/com/pamarin/oauth2/security/DefaultAuthenticityToken.java)  

verfiy token ด้วยวิธี Synchronizer (CSRF) Tokens และ Double Submit Cookie ตาม [Cross-Site Request Forgery (CSRF) Prevention Cheat Sheet](https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)_Prevention_Cheat_Sheet)  

# Postman สำหรับทดสอบ
https://www.getpostman.com/collections/5171a08ffd87ea6ced39 

# Reference

- https://oauth.net/2/  
- https://www.owasp.org  
