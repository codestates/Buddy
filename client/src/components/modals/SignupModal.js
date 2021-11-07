import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import { PASSWORD_REGEXP, EMAIL_REGEXP, AXIOS_DEFAULT_HEADER } from '../../constants/constants';
import Swal from 'sweetalert2/dist/sweetalert2.js';
import 'sweetalert2/dist/sweetalert2.css';
import '../../styles/modal/SignupModal.css';
import dotenv from 'dotenv';
import { PASSWORD_REGEXP, EMAIL_REGEXP } from '../../constants/constants';

export function SignupModal(props) {
  const [signupUserEmail, setSignupUserEmail] = useState('');
  const [signupUserEmailCheck, setSignupUserEmailCheck] = useState(0);
  const [signupUserPassword, setSignupUserPassword] = useState('');
  const [signupUserPasswordValid, setSignupUserPasswordValid] = useState('');
  const [signupUserNickname, setSignupUserNickname] = useState('');
  const [signupUserNicknameCheck, setSignupUserNicknameCheck] = useState(0);
  const [signupUserGender, setSignupUserGender] = useState('MALE');
  const [signupUserinfo, setSignupUserinfo] = useState({});

  const history = useHistory();

  // userinfo 값이 바뀌면 signup 진행
  useEffect(() => {
    if (
      signupUserEmailCheck === 1 &&
      signupUserNicknameCheck === 1 &&
      PASSWORD_REGEXP.test(signupUserPassword) &&
      signupUserPassword === signupUserPasswordValid &&
      signupUserPasswordValid !== ''
    ) {
      axios(`${process.env.REACT_APP_LOCAL_URL}/signup`, {
        method: 'POST',
        data: signupUserinfo,
        headers: {
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'POST',
          'Access-Control-Allow-Credentials': 'true',
        },
        withCredentials: true,
      })
        .then((res) => {
          console.log(res.data);
          props.setSignupModalOn(false);
<<<<<<< HEAD
          setSignupUserEmail('');
          setSignupUserPassword('');
          setSignupUserPasswordValid('');
          setSignupUserNickname('');
          setSignupUserGender('MALE');
=======
          resetSignupInput();
<<<<<<< HEAD
          Swal.fire({ title: '회원가입이 완료되었습니다.', confirmButtonText: '확인' });
=======
          Swal.fire('회원가입이 완료되었습니다.');
>>>>>>> 63d9ddd304ae8b8af3af0d3498fcafeddc09cb48
>>>>>>> af7224d54eb528186ae6440b4a80e56f8297d98a
          history.push('/');
        })
        .catch((err) => {
          console.error(err);
        });
    }
  }, [signupUserinfo]);

  // 회원 가입 모달창 이벤트
  const togglePopup = () => {
    if (props.signupModalOn === false) {
      props.setSignupModalOn(true);
    } else {
      props.setSignupModalOn(false);
      setSignupUserEmail('');
      setSignupUserPassword('');
      setSignupUserPasswordValid('');
      setSignupUserNickname('');
      setSignupUserGender('MALE');
    }
  };

  // 이메일 입력 상태관리
  const handleChangeEmail = (e) => {
    setSignupUserEmail(e.target.value);
    setSignupUserEmailCheck(0);
    console.log(signupUserEmail);
  };

  // 이메일 중복 체크
  const handleEmailValidCheck = () => {
<<<<<<< HEAD
    if (EMAIL_REGEXP.test(signupUserEmail)) {
      axios(`${process.env.REACT_APP_LOCAL_URL}/email_check`, {
        method: 'POST',
        data: { email: signupUserEmail },
        headers: AXIOS_DEFAULT_HEADER,
      })
        .then((res) => {
          console.log(res.data);
          setSignupUserEmailCheck(1);
          console.log('사용 가능한 이메일입니다.');

          // 이메일 인증 코드 보내기
          axios(`${process.env.REACT_APP_LOCAL_URL}/email_confirm`, {
            method: 'POST',
            data: { email: signupUserEmail },
            headers: AXIOS_DEFAULT_HEADER,
          })
            .then((res) => {
              console.log(res.data);
            })
            .catch((err) => {});
        })
        .catch((err) => {
          setSignupUserEmailCheck(2);
          console.log('이미 존재하는 이메일입니다!');
        });
    }
  };

  // 이메일 인증코드 체크
  const handleEmailCodeCheck = () => {
    // 이메일 코드 일치 확인
    axios(`${process.env.REACT_APP_LOCAL_URL}/email_code_check`, {
=======
    axios(`${process.env.REACT_APP_API_URL}/email_check`, {
>>>>>>> af7224d54eb528186ae6440b4a80e56f8297d98a
      method: 'POST',
      data: { email: signupUserEmail },
      headers: {
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'POST',
        'Access-Control-Allow-Credentials': 'true',
      },
      withCredentials: true,
    })
      .then((res) => {
        console.log(res.data);

        setSignupUserEmailCheck(1);
        console.log('사용 가능한 이메일입니다.');
      })
      .catch((err) => {
        console.error(err);
        setSignupUserEmailCheck(2);
        console.log('이미 존재하는 이메일입니다!');
      });
  };

  // 비밀번호 입력 상태관리
  const handleChangePassword = (e) => {
    setSignupUserPassword(e.target.value);
    console.log(signupUserPassword);
  };

  // 비밀번호 재입력 상태관리
  const handleChangePasswordValid = (e) => {
    setSignupUserPasswordValid(e.target.value);
    console.log(signupUserPasswordValid);
  };

  // 닉네임 상태관리
  const handleChangeNickname = (e) => {
    setSignupUserNickname(e.target.value);
    setSignupUserNicknameCheck(0);
    console.log(signupUserNickname);
  };

  // 닉네임 중복 체크
  const handleNicknameValidCheck = () => {
<<<<<<< HEAD
    if (signupUserNickname !== '') {
      axios(`${process.env.REACT_APP_LOCAL_URL}/nickname_check`, {
        method: 'POST',
        data: { nickname: signupUserNickname },
        headers: AXIOS_DEFAULT_HEADER,
=======
    axios(`${process.env.REACT_APP_API_URL}/nickname_check`, {
      method: 'POST',
      data: { nickname: signupUserNickname },
      headers: {
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'POST',
        'Access-Control-Allow-Credentials': 'true',
      },
      withCredentials: true,
    })
      .then((res) => {
        console.log(res.data);
        setSignupUserNicknameCheck(1);
        console.log('사용 가능한 닉네임입니다.');
>>>>>>> af7224d54eb528186ae6440b4a80e56f8297d98a
      })
      .catch((err) => {
        console.error(err);
        setSignupUserNicknameCheck(2);
        console.log('이미 존재하는 닉네임입니다!');
      });
  };

  // 성별 상태관리
  const handleChangeGender = (e) => {
    setSignupUserGender(e.target.value);
    console.log(signupUserGender);
  };

  // 회원가입 버튼 이벤트
  function handleSignup() {
<<<<<<< HEAD
    setSignupUserinfo({
      email: signupUserEmail,
      password: signupUserPassword,
      nickname: signupUserNickname,
      gender: signupUserGender,
    });
=======
    if (signupUserEmailCodeCheck === 0) {
      Swal.fire({ title: '이메일을 인증해주세요.', confirmButtonText: '확인' });
    } else if (signupUserPassword !== signupUserPasswordValid || signupUserPassword === '') {
      Swal.fire({ title: '입력된 비밀번호가 일치해야 합니다.', confirmButtonText: '확인' });
    } else if (signupUserNicknameCheck === 0) {
      Swal.fire({ title: '닉네임을 인증해주세요.', confirmButtonText: '확인' });
    } else {
      setSignupUserinfo({
        email: signupUserEmail,
        password: signupUserPassword,
        nickname: signupUserNickname,
        gender: signupUserGender,
      });
    }
>>>>>>> 63d9ddd304ae8b8af3af0d3498fcafeddc09cb48

    console.log(signupUserEmailCheck);
    console.log(signupUserNicknameCheck);
  }

  return (
    <div>
      {props.signupModalOn ? (
        <div className="signup__popup">
          <div className="signup__popup__inner">
            <div id="signup__close__btn">
              <button className="signup__close" onClick={togglePopup}>
                <img src="images/close_btn.png" alt="닫기 버튼" />
              </button>
            </div>
            <div id="signup__contents">
              <img src="images/logo.png" alt="buddy_logo" />
              <span id="signup__title">회원가입</span>
              <div className="signup__input__wrappers">
                <fieldset className="signup__input__container">
                  <input
                    className="signup__input"
                    onChange={handleChangeEmail}
                    type="email"
                    placeholder="이메일"
                    maxLength="30"
                    value={signupUserEmail}
                    autocomplete="false"
                  ></input>
                </fieldset>
                <button className="signup__input__btn" onClick={handleEmailValidCheck}>
                  중복검사
                </button>
              </div>
              <div className="signup__error">
                {!EMAIL_REGEXP.test(signupUserEmail) && signupUserEmail !== '' ? (
                  <span className="signup__error__message">이메일 주소에 @를 포함해주세요.</span>
                ) : (!EMAIL_REGEXP.test(signupUserEmail) && signupUserEmail !== '') || signupUserEmailCheck === 1 ? (
                  <span className="signup__correct">사용 가능한 이메일입니다.</span>
                ) : (!EMAIL_REGEXP.test(signupUserEmail) && signupUserEmail !== '') || signupUserEmailCheck === 2 ? (
                  <span className="signup__error__message">중복된 이메일입니다.</span>
                ) : (
                  <span className="signup__error__message"></span>
                )}
              </div>
              <div className="signup__input__wrappers">
                <fieldset className="signup__input__container">
                  <input
                    className="signup__input"
                    onChange={handleChangePassword}
                    type="password"
                    placeholder="비밀번호"
                    maxLength="15"
                    value={signupUserPassword}
                    autocomplete="new-password"
                  ></input>
                </fieldset>
              </div>
              <div className="signup__error">
                {!PASSWORD_REGEXP.test(signupUserPassword) && signupUserPassword !== '' ? (
                  <span className="signup__error__message">
                    특수문자, 영문, 숫자 포함 8자리 이상 암호를 입력해주세요.
                  </span>
                ) : (
                  <span className="signup__error__message"></span>
                )}
              </div>
              <div className="signup__input__wrappers">
                <fieldset className="signup__input__container">
                  <input
                    className="signup__input"
                    onChange={handleChangePasswordValid}
                    type="password"
                    placeholder="비밀번호 재입력"
                    maxLength="15"
                    value={signupUserPasswordValid}
                  ></input>
                </fieldset>
              </div>
              <div className="signup__correct">
                {PASSWORD_REGEXP.test(signupUserPassword) &&
                signupUserPassword === signupUserPasswordValid &&
                signupUserPasswordValid !== '' ? (
                  <span className="signup__error__message">비밀번호가 일치합니다.</span>
                ) : (
                  <span className="signup__error__message"></span>
                )}
              </div>
              <div className="signup__input__wrappers">
                <fieldset className="signup__input__container">
                  <input
                    className="signup__input"
                    onChange={handleChangeNickname}
                    type="text"
                    placeholder="닉네임"
                    maxLength="8"
                    value={signupUserNickname}
                  ></input>
                </fieldset>
                <button className="signup__input__btn" onClick={handleNicknameValidCheck}>
                  중복검사
                </button>
              </div>
              <div className="signup__error">
                {signupUserNickname === '' ? (
                  <span className="signup__error__message">닉네임을 입력해주세요.</span>
                ) : signupUserNickname !== '' && signupUserNicknameCheck === 1 ? (
                  <span className="signup__correct">사용 가능한 닉네임입니다.</span>
                ) : signupUserNickname !== '' && signupUserNicknameCheck === 2 ? (
                  <span className="signup__error__message">중복된 닉네임입니다.</span>
                ) : (
                  <span className="signup__error__message"></span>
                )}
              </div>
              <div className="signup__input__wrappers__gender">
                <fieldset className="signup__input__container">
                  <input
                    type="radio"
                    name="gender_info"
                    onChange={handleChangeGender}
                    value="MALE"
                    checked={signupUserGender === 'MALE'}
                  />
                  남자
                  <input
                    type="radio"
                    name="gender_info"
                    onChange={handleChangeGender}
                    value="FEMALE"
                    checked={signupUserGender === 'FEMALE'}
                  />
                  여자
                </fieldset>
              </div>
              <div id="signup__btn">
                <button className="signup__btn__contents" onClick={handleSignup}>
                  회원가입
                </button>
              </div>
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
}
