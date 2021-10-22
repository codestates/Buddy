import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import '../../styles/modal/SignupModal.css';
import dotenv from 'dotenv';
import { PASSWORD_REGEXP, EMAIL_REGEXP, MOBILE_REGEXP } from '../../constants/constants';

export function SignupModal(props) {
  const [signupUserEmail, setSignupUserEmail] = useState('');
  const [signupUserPassword, setSignupUserPassword] = useState('');
  const [signupUserPasswordValid, setSignupUserPasswordValid] = useState('');
  const [signupUserNickname, setSignupUserNickname] = useState('');
  const [signupUserMobile, setSignupUserMobile] = useState('');

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
      setSignupUserMobile('');
    }
  };

  // 이메일 입력 상태관리
  const handleChangeEmail = (e) => {
    setSignupUserEmail(e.target.value);
    console.log(signupUserEmail);
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
    console.log(signupUserNickname);
  };

  // 휴대폰 상태관리
  const handleChangeMobile = (e) => {
    setSignupUserMobile(e.target.value);
    console.log(signupUserMobile);
  };

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
                  ></input>
                </fieldset>
                <button className="signup__input__btn">중복검사</button>
              </div>
              <div className="signup__error">
                {!EMAIL_REGEXP.test(signupUserEmail) && signupUserEmail !== '' ? (
                  <span className="signup__error__message">이메일 주소에 @를 포함해주세요.</span>
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
                    maxLength="30"
                    value={signupUserPassword}
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
                    maxLength="30"
                    value={signupUserPasswordValid}
                  ></input>
                </fieldset>
              </div>
              <div className="signup__correct">
                {signupUserPassword === signupUserPasswordValid && signupUserPasswordValid !== '' ? (
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
                <button className="signup__input__btn">중복검사</button>
              </div>
              <div className="signup__error">
                <span className="signup__error__message">{/*에러 발생*/}</span>
              </div>
              <div className="signup__input__wrappers">
                <fieldset className="signup__input__container">
                  <input
                    className="signup__input"
                    onChange={handleChangeMobile}
                    type="text"
                    placeholder="휴대폰번호"
                    maxLength="13"
                    value={signupUserMobile}
                  ></input>
                </fieldset>
              </div>
              <div className="signup__error">
                {!MOBILE_REGEXP.test(signupUserMobile) && signupUserMobile !== '' ? (
                  <span className="signup__error__message">올바른 휴대폰 번호를 입력해주세요. (010-1234-5678)</span>
                ) : (
                  <span className="signup__error__message"></span>
                )}
              </div>
              <div id="signup__btn">
                <button className="signup__btn__contents" onClick={''}>
                  회원가입
                </button>
                <div id="social__signup">
                  <Link id="google__link__signup">
                    <img src="images/google_login.png" alt="구글 로그인" />
                  </Link>
                  <Link id="kakao__link__signup">
                    <img src="images/kakao_login.png" alt="카카오 로그인" />
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
}
