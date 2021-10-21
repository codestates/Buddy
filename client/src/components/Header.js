import { Link } from 'react-router-dom';
import React from 'react';
import '../styles/Header.css';

export default function Header() {
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
              <Link className="header__link" to="/login">
                로그인
              </Link>
            </li>
          </ul>
        </nav>
      </header>
    </div>
  );
}
