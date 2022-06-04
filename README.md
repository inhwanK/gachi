# 가치코딩 - Gachicoding
> 개발 관련 정보 공유, 관련 지식을 묻고 답하는 웹 커뮤니티 서비스 <br>

## 목차
* [저장소](#저장소)
* [참여 인원 및 역할](#참여-인원-및-역할)
* [사용 기술](#사용-기술)
* [서버 구조도](#서버-구조도)
* [ERD](#erd)
* [주요 기능](#주요-기능)
* [프로젝트 관련 내용 정리](#프로젝트-관련-내용-정리)
* [트러블 슈팅](#주요-이슈-및-트러블슈팅)

<br>

## 참여 인원 및 역할

[//]: # (* 2022년 3월 ~ ing)
* [김인표](https://github.com/kiminpyo) - Frontend
* [김인환](https://github.com/inhwanK) - Backend
* [서영준](https://github.com/95Seo) - Backend
* [배지왕](https://github.com/BAE-JI-WANG) - DevOps
* [김세현](https://github.com/saehyen) - DevOps

<br>

## 저장소
* [백엔드 저장소 (현재 저장소)](https://github.com/inhwanK/gachicoding)
* [프론트엔드 저장소](https://github.com/kiminpyo/gachicoding-front-next)
* [DevOps 저장소](https://github.com/BAE-JI-WANG/gachicoding_DevOps)

<br>

## 사용 기술
* Java 11 (jdk-11.0.13)
* gradle 7.4
* react 8.1.2
* aws
* terraform 1.1.7
* Spring Data JPA
* Spring Security
* swagger2
* lombok

<br>

## 서버 구조도

<br>

## ERD
#### 2022.06.05 업데이트 
- auth 테이블 기본키 binary(16)으로 변경
- comment 테이블 기본키 AI 설정, parents_idx null 허용

![가치코딩 ERD](document/ERD.png)
<br>

## 주요 기능

<br>

## 프로젝트 관련 내용 정리
<br>

## 주요 이슈 및 트러블슈팅
* Spring Data JPA - [DynamicInsert,DynamicUpdate 어노테이션의 용도(작성 전)]()
* CORS - [CORS 정책 위반과 이를 올바르게 해결하는 방법(작성 전)]()
* UnexpectedRollbackException - [아이디(이메일) 중복처리에서 @Transactional 에 의한 롤백 예외 발생(작성 전)]()
* Spring Security & OAuth2 - [로그인 연동(구글, 카카오, 깃허브)시 기존 아이디와 중복 처리(작성 전)]()
* AWS Access Denied - [IAM 계정으로 로그인 시 RDS 접근이 안되는 현상(작성 전)]()
* WebMvcConfigurationSupport - [스웨거, pageable 사용을 위해 해당 클래스를 상속받아 오버라이딩(작성 전)]()