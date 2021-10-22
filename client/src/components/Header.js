import React from 'react';
import { Link, useHistory } from 'react-router-dom';
import '../styles/Header.css';

export default function Header(props) {
  const history = useHistory();

  const loginModalOpen = () => {
    props.setModalOn(true);
    console.log(props.modalOn);
  };

  const handleSignOut = () => {
    // 로컬스토리지 accessToken 지우기
    localStorage.setItem('accessToken', '');
    history.push('/');
    props.setLoginOn(false);
    props.setUserInfo({});
  };

  return (
    <div id="header__wrapper">
      <header id="headers">
        <Link className="header__link" to="/">
          <img src="/images/logo.png" alt="Project_TT LOGO" />
        </Link>
        <nav id="nav">
          <ul id="ul">
            <li>
              <Link className="header__link" to="/chat">
                채팅
              </Link>
            </li>
            <li>
              <Link className="header__link" to="/demo">
                체험하기
              </Link>
            </li>
            <li>
              <button className="header__link" onClick={loginModalOpen}>
                로그인
              </button>
            </li>
          </ul>
        </nav>
      </header>
    </div>
  );
}
