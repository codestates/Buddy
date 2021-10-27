import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import '../styles/MyPage.css';
import { PASSWORD_REGEXP } from '../constants/constants';

export function MyPage(props) {
  const history = useHistory();

  const [userNickname, SetUserNickname] = useState('');
  const [userNicknameCheck, setUserNicknameCheck] = useState(0);
  const [userPassword, setUserPassword] = useState('');
  const [userPasswordCheck, setUserPasswordCheck] = useState('');

  const handleUnregister = () => {
    axios(`${process.env.REACT_APP_API_URL}/user/${props.userInfo.id}`, {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'DELETE',
        'Access-Control-Allow-Credentials': 'true',
      },
      withCredentials: true,
    })
      .then((res) => {
        console.log(res.data); // 회원정보 삭제 완료
        props.setLoginOn(false); // 로그인 상태 false
        history.push('/'); // 루트 경로로 이동
      })
      .catch((err) => {
        console.error(err);
      });
  };

  // 닉네임 입력 상태관리
  const handleChangeNickname = (e) => {
    SetUserNickname(e.target.value);
    console.log(userNickname);
  };

  // 닉네임 체크 이벤트 함수
  const handleCheckNickname = () => {
    axios(`${process.env.REACT_APP_API_URL}/nickname_check`, {
      method: 'POST',
      data: { nickname: userNickname },
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
        setUserNicknameCheck(1);
        console.log('사용 가능한 닉네임입니다.');
      })
      .catch((err) => {
        console.error(err);
        setUserNicknameCheck(2);
        console.log('이미 존재하는 닉네임입니다!');
      });
  };

  // 닉네임 변경 이벤트 함수
  const handleModifyNickname = () => {
    if (userNickname !== '' && userNicknameCheck === 1) {
      axios(`${process.env.REACT_APP_API_URL}/profile/${props.userInfo.id}`, {
        method: 'PUT',
        data: { nickname: userNickname },
        headers: {
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'PUT',
          'Access-Control-Allow-Credentials': 'true',
        },
        withCredentials: true,
      })
        .then((res) => {
          console.log(res.data);
          window.location.replace('/mypage'); // mypage 새로고침
        })
        .catch((err) => {});
    } else {
    }
  };

  // 비밀번호 입력 이벤트 함수
  const handleChangePassword = (e) => {
    setUserPassword(e.target.value);
    console.log(userPassword);
  };

  // 비밀번호 재입력 이벤트 함수
  const handleChangePasswordValid = (e) => {
    setUserPasswordCheck(e.target.value);
    console.log(userPasswordCheck);
  };

  // 닉네임 변경 이벤트 함수
  const handleModifyPassword = () => {
    if (userPassword === userPasswordCheck) {
      axios(`${process.env.REACT_APP_API_URL}/profile/${props.userInfo.id}`, {
        method: 'PUT',
        data: { password: userPassword },
        headers: {
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'PUT',
          'Access-Control-Allow-Credentials': 'true',
        },
        withCredentials: true,
      })
        .then((res) => {
          console.log(res.data);
          window.location.replace('/mypage'); // mypage 새로고침
        })
        .catch((err) => {});
    } else {
    }
  };

  return (
    <div className="my__page">
      <section className="mypage__wrapper">
        <div className="mypage__container">
          <div className="mypage__basicinfo">
            <span className="mypage__nickname">{props.userInfo.nickname}</span>
            <span className="mypage__email">{props.userInfo.email}</span>
            <span className="mypage__role">{props.userInfo.authority}</span>
            <span className="mypage__gender">{props.userInfo.gender}</span>
          </div>
          <div className="mypage__modifyinfo">
            <div className="mypage__modifyinfo__container">
              <input
                className="modify__nickname"
                type="text"
                onChange={handleChangeNickname}
                placeholder="변경할 닉네임을 입력해주세요"
                maxLength="15"
              ></input>
              <button onClick={handleCheckNickname}>중복확인</button>
              <button onClick={handleModifyNickname}>변경</button>
            </div>
            {userNicknameCheck === 1 ? (
              <span className="mypage__state__message" style={{ color: 'green' }}>
                사용 가능한 닉네임입니다.
              </span>
            ) : userNicknameCheck === 2 ? (
              <span className="mypage__state__message" style={{ color: 'red' }}>
                중복된 닉네임입니다.
              </span>
            ) : null}
            <div className="mypage__modifyinfo__container">
              <input
                className="modify__password"
                type="password"
                placeholder="변경할 비밀번호를 입력해주세요"
                onChange={handleChangePassword}
                maxLength="15"
                autocomplete="new-password"
              ></input>
            </div>
            {!PASSWORD_REGEXP.test(userPassword) && userPassword !== '' ? (
              <span className="mypage__error__message" style={{ color: 'red' }}>
                특수문자, 영문, 숫자 포함 8자리 이상 암호를 입력해주세요.
              </span>
            ) : (
              <span className="mypage__error__message"></span>
            )}
            <div className="mypage__modifyinfo__container">
              <input
                className="modify__password__valid"
                type="password"
                onChange={handleChangePasswordValid}
                placeholder="비밀번호 재입력"
                maxLength="15"
              ></input>
              <button onClick={handleModifyPassword}>변경</button>
            </div>
            {PASSWORD_REGEXP.test(userPassword) && userPassword === userPasswordCheck && userPasswordCheck !== '' ? (
              <span className="mypage__error__message" style={{ color: 'green' }}>
                비밀번호가 일치합니다.
              </span>
            ) : (
              <span className="mypage__error__message"></span>
            )}
          </div>
        </div>
        <div className="mypage__image__container">
          <img src={props.userInfo.profile_image} alt="마이페이지 이미지" />
          <button>이미지 변경</button>
          <button>이미지 삭제</button>
        </div>
      </section>
      <section className="mypage__unregister__wrapper">
        <div className="mypage__unregister__container">
          <button onClick={handleUnregister}>회원 탈퇴</button>
        </div>
      </section>
    </div>
  );
}
