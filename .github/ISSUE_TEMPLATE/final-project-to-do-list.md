---
name: Final Project To Do List
about: Final Project To Do List
title: Final Project To Do List
labels: ''
assignees: ''

---

구현에 따라 체크박스를 하나씩 체크해가면서, 프로젝트의 필수 혹은 권장할 만한 진행 상황을 확인할 수 있습니다.

## 1. 시스템 아키텍처

- [ ] JWT를 이용한 로그인 구현 | 필수 | FE, BE | ★
- [ ] 권한 부여(Authorization)에 따른 기능의 접근 제한 구현 (예- 일반 사용자, 관리자 권한) | 필수 | FE, BE | ★
- [ ] OAuth 2.0을 이용한 소셜 로그인 | 권장 | FE, BE | ★
- [ ] TypeScript 언어 사용 | 도전 | FE | ★★★
- [ ] next.js를 사용한 Server Side Rendering 적용 | 도전 | FE | ★★★
- [ ] AWS lambda를 사용한 Serverless 아키텍처 도입 | 도전 | BE | ★★★
- [ ] React custom hook 만들어서 쓰기 | 도전 | FE | ★★★
- [ ] socket.io를 이용한 실시간 통신 | 신중 | FE, BE | ★★★★
- [ ] redis를 이용한 캐싱 | 신중 | BE | ★★★★
- [ ] Create React App을 사용하지 않고 빌드 과정 직접 구성 | 신중 | FE | ★★★★
- [ ] 카프카를 사용한 메세지 기능 구현 | 신중 | BE | ★★★★
- [ ] Spring Boot가 아닌 Spring5를 사용한 프로젝트 구성 | 신중 | BE | ★★★★

## 2. UI/UX

- [ ] 라이브러리를 사용하지 않고 React 컴포넌트 직접 작성 (유어클래스에서 다룬 것) | 필수 | ★
- [ ] 3rd-party API 호출 | 권장 | ★
- [ ] 모바일 환경 대응 및 반응형 웹 | 권장 | ★★
- [ ] 라이브러리를 사용하지 않고 React 컴포넌트 직접 작성 (유어클래스에서 다루지 않은 것) | 권장 | ★★★
- [ ] S3로 파일 업로드 | 권장 | ★★★
- [ ] 회원 가입시 인증 메일 발송 | 권장 | ★★★

## 3. 스키마 및 API

- [ ] N:M (다대다) 관계 설정 | 필수 | ★
- [ ] ERD (DB Diagram) 작성 | 필수 | ★
- [ ] API 작성 툴 (Swagger, Postman, Gitbook API) 사용 | 필수 | ★★
- [ ] ORM 사용 (sequelize, typeorm 권장) | 권장 | ★
- [ ] 근거가 분명한 NoSQL 도입 | 권장 | ★★
- [ ] 페이지네이션 구현 | 권장 | ★★★

## 4. DevOps

- [ ] \[코드\] precommit 단계에서 semistandard 적용 | 필수 | ★
- [ ] \[릴리즈\] 깃허브 커밋메세지 규칙 합의 | 필수 | ★
- [ ] \[코드\] 버전 규칙 합의 | 필수 | ★
- [ ] \[릴리즈\] PR 형식 합의 | 권장 | ★
- [ ] \[배포\] 배포 자동화 설정 | 권장 | ★
- [ ] \[배포\] 도메인 구매 및 HTTPS 배포 | 권장 | ★★★
- [ ] \[테스트\] 백엔드 테스트 코드 작성 | 권장 | ★★★
- [ ] \[테스트\] w3c validator 통과 | 권장 | ★★★

## Advanced (도전) 항목을 추가적으로 구현할 계획이라면, 아래에 적어주세요
도전 항목은 유어클래스의 To do 리스트를 참고하세요.

- [ ] 예) TypeScript 언어 사용 | 도전 | ★★★

## Nightmare (신중) 항목을 추가적으로 구현할 계획이라면, 아래에 적어주세요
신중 항목은 유어클래스의 To do 리스트를 참고하세요.

- [ ] 예) redis를 이용한 캐싱 | 신중 | ★★★★
