import React from 'react';
import { Link, useHistory } from 'react-router-dom';
import { Cookies } from 'react-cookie';
import '../styles/Header.css';

export default function Header(props) {
  const history = useHistory();

  const cookies = new Cookies();

  const loginModalOpen = () => {
    props.setModalOn(true);
    console.log(props.modalOn);
  };

  const handleSignOut = () => {
    cookies.remove('refreshToken');
    history.push('/');
    alert('정상적으로 로그아웃되었습니다.');
    props.setLoginOn(false);
    props.setUserInfo({});
  };

  return (
    <div id="header__wrapper">
      <header id="headers">
        <Link className="header__link" to="/">
          <img src="/images/logo.png" alt="Buddy LOGO" />
        </Link>
        <nav id="nav">
          <ul id="ul">
            <li>
              <Link className="header__link" to="/chat/rooms">
                채팅
              </Link>
            </li>
            <li>
              <Link className="header__link" to="/demo">
                체험하기
              </Link>
            </li>
            <li>
              {props.loginOn === true ? (
                <button className="header__link" onClick={handleSignOut}>
                  로그아웃
                </button>
              ) : (
                <button className="header__link" onClick={loginModalOpen}>
                  로그인
                </button>
              )}
            </li>
            <li>
              {props.loginOn === true ? (
                <Link className="header__link" to="/mypage">
                  마이페이지
                </Link>
              ) : null}
            </li>
          </ul>
        </nav>
      </header>
    </div>
  );
}
