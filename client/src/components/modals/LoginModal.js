import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import '../../styles/modal/LoginModal.css';
import dotenv from 'dotenv';
import { SignupModal } from './SignupModal';
import { Cookies } from 'react-cookie';

axios.defaults.withCredentials = true;

// .env 환경변수 사용
dotenv.config();

export function LoginModal(props) {
  const [userEmail, setUserEmail] = useState(''); // 이메일
  const [userPassword, setUserPassword] = useState(''); // 비밀번호
  const [userLoginError, setUserLoginError] = useState(''); // 로그인 에러 메세지
  const [userInfo, setUserInfo] = useState({ email: '', nickname: '', authority: '', gender: '' }); // 로그인 성공 시 저장되는 유저 정보
  const [signupModalOn, setSignupModalOn] = useState(false); // 모달 오픈 여부

  const history = useHistory();

  const cookies = new Cookies();

  // 새로고침해도 로그인 유지
  useEffect(() => {
    accessTokenCheck();
    kakaoAccessTokenCheck();
    googleCodeOauth(); // 구글 인가코드 및 로그인 함수
    kakaoCodeOauth(); // 카카오 소셜 로그인 데이터 저장 함수
  }, []);

  // google oAuth 인가코드 백엔드 서버에 쿼리 스크링으로 보내기
  const googleCodeOauth = () => {
    const googleUrl = new URL(window.location.href); // 주소창 값 가져오기
    const googleSearch = googleUrl.search; // 쿼리 스크링 가져오기

    if (googleSearch) {
      const googleCode = googleSearch.split('=')[1].split('&')[0]; // google code 값만 추출

      axios(`${process.env.REACT_APP_API_URL}/oauth/google/callback?code=${googleCode}`, {
        method: 'GET',
      })
        .then((res) => {
          console.log(res.data);
          setUserInfo(res.data); // res.data userInfo에 저장
          console.log(cookies.get('refreshToken'));
          cookies.set('refreshToken', res.data.refreshToken);
          props.setLoginOn(true); // 로그인 true
          history.push('/');
          accessTokenCheck(); // 새로고침 시 로그인 유지
        })
        .catch((err) => {});
    }
  };

  // kakao oAuth 인가코드 백엔드 서버에 쿼리 스크링으로 보내기
  const kakaoCodeOauth = () => {
    const kakaoUrl = new URL(window.location.href); // 주소창 값 가져오기
    const kakaoSearch = kakaoUrl.search; // 쿼리 스크링 가져오기

    if (kakaoSearch) {
      const kakaoCode = kakaoSearch.split('=')[1].split('&')[0]; // kakao code 값만 추출

      console.log(kakaoCode);

      axios(`${process.env.REACT_APP_API_URL}/oauth/kakao/token?code=${kakaoCode}`, {
        method: 'GET',
        headers: {
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Origin': 'http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com',
          'Access-Control-Allow-Methods': 'GET',
          'Access-Control-Allow-Credentials': 'true',
        },
        withCredentials: true,
      })
        .then((res) => {
          console.log(res.data);
          const kakaoAccessToken = res.data.access_token;
          cookies.set('kakaoAccessToken', kakaoAccessToken);

          axios(`https://kapi.kakao.com/v2/user/me`, {
            method: 'GET',
            headers: {
              'Access-Control-Allow-Headers': 'Content-Type',
              'Access-Control-Allow-Origin': '*',
              'Access-Control-Allow-Methods': 'GET',
              'Access-Control-Allow-Credentials': 'true',
              Authorization: `Bearer ${kakaoAccessToken}`,
            },

            withCredentials: true,
          })
            .then((res) => {
              console.log(res.data.access_token);

              let copyUserInfo = { ...userInfo };

              copyUserInfo.email = `${res.data.kakao_account.email}`;
              copyUserInfo.gender = `${res.data.kakao_account.gender}`;
              copyUserInfo.nickname = `${res.data.properties.nickname}`;
              copyUserInfo.authority = 'GENERAL';
              props.setUserInfo(copyUserInfo);
              console.log(cookies.get('kakaoAccessToken'));
              props.setLoginOn(true); // 로그인 true
              history.push('/');

              // 이메일 체크
              axios(`${process.env.REACT_APP_API_URL}/email_check`, {
                method: 'POST',
                data: { email: res.data.kakao_account.email },
                headers: {
                  'Access-Control-Allow-Headers': 'Content-Type',
                  'Access-Control-Allow-Origin': '*',
                  'Access-Control-Allow-Methods': 'POST',
                  'Access-Control-Allow-Credentials': 'true',
                },
                withCredentials: true,
              })
                // 이메일 일치하지 않을 때 회원가입
                .then((res) => {
                  // 이메일 체크
                  axios(`${process.env.REACT_APP_API_URL}/email_check`, {
                    method: 'POST',
                    data: { email: res.data.kakao_account.email },
                    headers: {
                      'Access-Control-Allow-Headers': 'Content-Type',
                      'Access-Control-Allow-Origin': '*',
                      'Access-Control-Allow-Methods': 'POST',
                      'Access-Control-Allow-Credentials': 'true',
                    },
                    withCredentials: true,
                  })
                    // 이메일 일치하지 않을 때 회원가입
                    .then((res) => {
                      axios(`${process.env.REACT_APP_API_URL}/signup`, {
                        method: 'POST',
                        data: copyUserInfo,
                        headers: {
                          'Access-Control-Allow-Headers': 'Content-Type',
                          'Access-Control-Allow-Origin': '*',
                          'Access-Control-Allow-Methods': 'POST',
                          'Access-Control-Allow-Credentials': 'true',
                        },
                        withCredentials: true,
                      })
                        .then((res) => {})
                        .catch((err) => {});
                    })

                    .catch((err) => {});
                })

                .catch((err) => {});

              kakaoAccessTokenCheck(); // 새로고침 시 로그인 유지
            })
            .catch((err) => {});
        })
        .catch((err) => {});
    }
  };

  // 일반 로그인 인증 처리
  const onLogin = async () => {
    const userData = {
      email: userEmail,
      password: userPassword,
    };

    // id, pw 입력란 초기화
    setUserEmail('');
    setUserPassword('');

    console.log(userData);

    // 로그인 JWT 인증 처리 (API POST : /login)
    await axios(`${process.env.REACT_APP_API_URL}/login`, {
      method: 'POST',
      data: userData,
      headers: {
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'POST',
        'Access-Control-Allow-Credentials': 'true',
      },
      withCredentials: true,
    })
      .then((res) => {
        console.log(res.data); // accessToken (클라이언트에 따로 저장)
        console.log(cookies.get('refreshToken'));
        cookies.set('refreshToken', res.data.refreshToken);
        props.setLoginOn(true);
        accessTokenCheck();
      })
      .catch((err) => {
        console.error(err);
        console.log(`email = ${userData.email}, password = ${userData.password}`);

        if (userData.email === '' && userData.password === '') {
          setUserLoginError('이메일 또는 비밀번호를 입력해주세요.');
        } else {
          setUserLoginError('이메일 또는 비밀번호를 잘못 입력하셨습니다.');
        }
        if (err.response) {
          // 에러에 response가 있으면 해당 data를 출력
          console.log(err.response.data);
        }
      });
  };

  // 쿠키에 저장된 refreshToken 확인으로 새로고침 시 로그인 유지
  const accessTokenCheck = () => {
    // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
    axios.defaults.headers.common['Authorization'] = `Bearer ${cookies.get('kakaoAccessToken')}`;

    // 윗 줄에 기본 헤더로 `Bearer ${accessToken}`를 넣었기 때문에
    // 해당 accesstoken이 유효하면 GET 요청으로 로그인 회원 정보를 받아옴
    axios(`${process.env.REACT_APP_API_URL}/token_check`, {
      method: 'GET',
      headers: {
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET',
        'Access-Control-Allow-Credentials': 'true',
      },
      withCredentials: true,
    })
      .then((res) => {
        // id, pw가 맞고 토큰이 유효하면 받아온 데이터를 userInfo에 저장
        console.log(res.data);
        props.setUserInfo(res.data);
        setUserInfo(props.userInfo);
        console.log(userInfo);

        // useHistory를 사용하여 로그인 성공시 모달창 닫기
        props.setModalOn(false);
        setUserEmail('');
        setUserPassword('');
        setUserLoginError('');
        props.setLoginOn(true);
      })
      .catch((err) => {
        console.error(err);
      });
  };

  // 카카오 : 쿠키에 저장된 kakaoAccessToken 확인으로 새로고침 시 로그인 유지
  const kakaoAccessTokenCheck = () => {
    // 해당 accesstoken이 유효하면 GET 요청으로 로그인 회원 정보를 받아옴
    axios(`https://kapi.kakao.com/v1/user/access_token_info`, {
      method: 'GET',
      headers: {
        'Access-Control-Allow-Headers': 'application/x-www-form-urlencoded;charset=utf-8',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET',
        'Access-Control-Allow-Credentials': 'true',
        Authorization: `Bearer ${cookies.get('kakaoAccessToken')}`,
      },
      withCredentials: true,
    })
      .then((res) => {
        axios(`https://kapi.kakao.com/v2/user/me`, {
          method: 'GET',
          headers: {
            'Access-Control-Allow-Headers': 'Content-Type',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET',
            'Access-Control-Allow-Credentials': 'true',
            Authorization: `Bearer ${cookies.get('kakaoAccessToken')}`,
          },

          withCredentials: true,
        })
          .then((res) => {
            console.log(res.data);
            let copyUserInfo = { ...userInfo };

            copyUserInfo.email = `${res.data.kakao_account.email}`;
            copyUserInfo.gender = `${res.data.kakao_account.gender}`;
            copyUserInfo.nickname = `${res.data.properties.nickname}`;
            copyUserInfo.authority = 'GENERAL';
            props.setUserInfo(copyUserInfo);
            props.setLoginOn(true); // 로그인 true
          })
          .catch((err) => {});
      })
      .catch((err) => {
        console.error(err);
      });
  };

  // 모달 관련 팝업 이벤트
  const togglePopup = () => {
    if (props.modalOn === false) {
      props.setModalOn(true);
    } else {
      props.setModalOn(false);
      setUserEmail('');
      setUserPassword('');
      setUserLoginError('');
    }
  };

  // 이메일 입력 상태관리
  const handleChangeEmail = (e) => {
    setUserEmail(e.target.value);
    console.log(userEmail);
  };

  // 비밀번호 입력 상태관리
  const handleChangePassword = (e) => {
    setUserPassword(e.target.value);
    console.log(userPassword);
  };

  // 로그인 모달창 끄고 회원가입 모달창 열기
  const signupModalOpen = () => {
    props.setModalOn(false);
    setUserEmail('');
    setUserPassword('');
    setUserLoginError('');
    setSignupModalOn(true);
  };

  return (
    <div>
      <SignupModal signupModalOn={signupModalOn} setSignupModalOn={setSignupModalOn} />
      {props.modalOn ? (
        <div className="popup">
          <div className="popup__inner">
            <div id="signin__close__btn">
              <button className="close" onClick={togglePopup}>
                <img src="images/close_btn.png" alt="닫기 버튼" />
              </button>
            </div>
            <div id="signin__contents">
              <img src="images/logo.png" alt="buddy_logo" />
              <span id="signin__title">로그인</span>
              <fieldset>
                <input
                  className="signin__input"
                  onChange={handleChangeEmail}
                  type="email"
                  id="username"
                  placeholder="이메일"
                  maxLength="30"
                  value={userEmail}
                ></input>
              </fieldset>
              <fieldset>
                <input
                  className="signin__input"
                  onChange={handleChangePassword}
                  type="password"
                  id="password"
                  placeholder="비밀번호"
                  maxLength="30"
                  value={userPassword}
                ></input>
              </fieldset>
              <span id="login__error">{userLoginError}</span>
              <div id="signin__btn">
                <button className="signin__btn__contents" onClick={onLogin}>
                  로그인
                </button>
                <span id="not__user">
                  아직 회원이 아니신가요?
                  <button id="signup__link" onClick={signupModalOpen}>
                    회원가입
                  </button>
                </span>
                <div id="social__login">
                  <a id="google__link" href={`${process.env.REACT_APP_API_URL}/login_google`}>
                    <img src="images/google_login.png" alt="구글 로그인" />
                  </a>
                  <a id="kakao__link" href={`${process.env.REACT_APP_API_URL}/login_kakao`}>
                    <img src="images/kakao_login.png" alt="카카오 로그인" />
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
}
