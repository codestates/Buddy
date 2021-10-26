import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import '../styles/MyPage.css';

export function MyPage(props) {
  // 회원 탈퇴 로직

  const history = useHistory();

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
                placeholder="변경할 닉네임을 입력해주세요"
                maxLength="15"
              ></input>
              <button>중복확인</button>
              <button>변경</button>
            </div>
            <div className="mypage__modifyinfo__container">
              <input
                className="modify__password"
                type="password"
                placeholder="변경할 비밀번호를 입력해주세요"
                maxLength="15"
                autocomplete="new-password"
              ></input>
            </div>
            <div className="mypage__modifyinfo__container">
              <input
                className="modify__password__valid"
                type="password"
                placeholder="비밀번호 재입력"
                maxLength="15"
              ></input>
              <button>변경</button>
            </div>
          </div>
        </div>
        <div className="mypage__image__container">
          <img src="images/mypage_img.png" alt="마이페이지 이미지" />
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
