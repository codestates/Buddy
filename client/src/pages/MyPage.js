import React, { useEffect } from 'react';
import '../styles/MyPage.css';

export function MyPage(props) {
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
          <button>회원 탈퇴</button>
        </div>
      </section>
    </div>
  );
}
