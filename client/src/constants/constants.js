// 비밀번호 정규식 (특수문자, 문자, 숫자 포함 형태 8~15자리 이내의 암호 정규식)
export const PASSWORD_REGEXP = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;

// 이메일 정규식
export const EMAIL_REGEXP = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

// 휴대폰번호 정규식
export const MOBILE_REGEXP = /^\d{3}-\d{3,4}-\d{4}$/;

// axios 기본 헤더
export const AXIOS_DEFAULT_HEADER = {
  'Access-Control-Allow-Headers': 'Content-Type',
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Credentials': 'true',
};

// 팀원
export const TEAM_NAME = [
  { name: '황재성', src: 'https://github.com/shreder0804' },
  { name: '남궁민', src: 'https://github.com/nmin11' },
  { name: '남수연', src: 'https://github.com/namtndus' },
];
