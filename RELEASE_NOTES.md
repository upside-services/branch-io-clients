# Branch.io Java Client

## 0.7.0 2021/02/16

* Updating to the latest parent pom (2.6.1) w/ latest security patched 3rd party libraries

## 0.6.0 2020/05/26

* Updating to the latest parent pom (2.1.1) w/ new Java checkstyle/linting
* Updating the version of our retrofit jar from 2.7 -> 2.9 to pick up new Jackson versions (to shutup whitesource "of of date" library reports)

## 0.5.0 2020/03/17

* Updating to the latest parent pom (1.16.0) and standardizing CI/CD infra directory

## 0.4.0 2020/01/30

* Releasing 0.3.0 with the original "com.upside.branch-io-clients" package and the "branch-io-java-client" artifact 

## 0.3.0 2020/01/30

* Converting from gradle to maven
* Updating to latest CVE-patched retrofit JAR
* This release was mis-packaged as "com.upside:branch-io-java-client" which was a mistake made during the gradle -> maven conversion

## 0.2.0 2016/11/22

* Changed interface to return URIs instead of URLs
* Added support for wrapping BranchClient implementations in a backoff & retry facade

## 0.1.0

* Initial release
