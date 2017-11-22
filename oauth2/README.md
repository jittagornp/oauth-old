OAuth 2.0
=================

[![Build Status](https://travis-ci.org/pamarin-tech/oauth2.svg?branch=master)](https://travis-ci.org/pamarin-tech/oauth2)
[![Coverage Status](https://coveralls.io/repos/github/pamarin-tech/oauth2/badge.svg?branch=master)](https://coveralls.io/github/pamarin-tech/oauth2?branch=master)
[![codecov](https://codecov.io/gh/pamarin-tech/oauth2/branch/master/graph/badge.svg)](https://codecov.io/gh/pamarin-tech/oauth2)
[![CodeFactor](https://www.codefactor.io/repository/github/pamarin-tech/oauth2/badge)](https://www.codefactor.io/repository/github/pamarin-tech/oauth2)
[![Maintainability](https://api.codeclimate.com/v1/badges/e85d9a83858693089c62/maintainability)](https://codeclimate.com/github/pamarin-tech/oauth2/maintainability)
[![BCH compliance](https://bettercodehub.com/edge/badge/pamarin-tech/oauth2?branch=master)](https://bettercodehub.com/)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/aa8033d61aba478aa0f9541c6d3d59d4)](https://www.codacy.com/app/jittagornp/oauth2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pamarin-tech/oauth2&amp;utm_campaign=Badge_Grade)
[![Known Vulnerabilities](https://snyk.io/test/github/pamarin-tech/oauth2/badge.svg)](https://snyk.io/test/github/pamarin-tech/oauth2)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/pamarin-tech/oauth2.svg?columns=all)](https://waffle.io/pamarin-tech/oauth2)

ตัวอย่างการเขียน OAuth 2.0 - Java Spring-Boot

Implement ตาม Spec `RFC6749` : https://tools.ietf.org/html/rfc6749

> OAuth 2.0 (Open Authentication version 2.0) เป็น Framework/วิธีการ ที่อนุญาตให้ Application ใดๆ (Third-Party Application) สามารถเข้าถึงข้อมูล/ทรัพยากรของผู้ใช้งานระบบ (Resource Owner) ของ Application หนึ่ง ผ่าน Http Service ด้วยการอนุญาตจากเจ้าของทรัพยากรนั้น  

# Core

- [OAuth 2.0 Framework](https://tools.ietf.org/html/rfc6749) - RFC 6749
- [Bearer Token Usage](https://tools.ietf.org/html/rfc6750) - RFC 6750
- [Threat Model and Security Considerations](https://tools.ietf.org/html/rfc6819) - RFC 6819 

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

1. build

> $ mvn clean install -P error-prone -U

2. run
> $ mvn spring-boot:run

# Http Session

- ตั้งชื่อเป็น ssid   
- เก็บไว้ใน Redis  

# Token  

- Authorization Code + Access Token เป็น Stateless Sign/Verify ด้วย JWT โดยใช้ RSA Algorithm https://github.com/auth0/java-jwt  
- Authorization Code จะหมดอายุภายใน 5 นาที  
- Access Token จะหมดอายุภายใน 15 นาที
- Refresh Token เป็น Stateful เก็บไว้ใน Redis และจะหมดอายุภายใน 8 ชั่วโมง ถ้าไม่ได้ใช้  

# วิธีการ Generate Key Pairs (public key/private key)
ในกรณีที่ต้องการ generate key pairs ใหม่ https://github.com/jittagornp/guideline/blob/master/how_to_generate_keypairs.md  

# CSRF Token

Generate csrf token ด้วยวิธีเดียวกับที่ Rails (Github และ Gitlab) ใช้ https://medium.com/rubyinside/a-deep-dive-into-csrf-protection-in-rails-19fa0a42c0ef ซึ่งการเปรียบเทียบ (Compare) token มีการป้องกัน [Timing Attacks](https://thisdata.com/blog/timing-attacks-against-string-comparison/) ด้วย 

> [src/main/java/com/pamarin/oauth2/security/DefaultAuthenticityToken.java](src/main/java/com/pamarin/oauth2/security/DefaultAuthenticityToken.java)  

verfiy token ด้วยวิธี Synchronizer (CSRF) Tokens และ Double Submit Cookie ตาม [Cross-Site Request Forgery (CSRF) Prevention Cheat Sheet](https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)_Prevention_Cheat_Sheet)  

# Reference

- https://oauth.net/2/  
- https://www.owasp.org  
