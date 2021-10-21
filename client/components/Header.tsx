import Link from 'next/link';
import React, { Dispatch, SetStateAction } from 'react';
import styles from '../styles/Header.module.css';

export default function Header(modalOn: Boolean, setModalOn: Function) {
  const loginModalOn = () => {
    setModalOn(true);
    console.log(modalOn)
  }

  return (
    <div id={styles.header__wrapper}>
      <header id={styles.header}>
        <Link href="/">
          <img src="/images/logo.png" alt="Project_TT LOGO" />
        </Link>
        <nav id={styles.nav}>
          <ul id={styles.ul}>
            <li>
              <Link href="/chat" as="/chat.html">
                <a>채팅</a>
              </Link>
            </li>
            <li>
              <Link href="/chatbot" as="/chatbot.html">
                <a>챗봇</a>
              </Link>
            </li>
            <li>
              <Link href="/demo" as="/demo.html">
                <a>체험하기</a>
              </Link>
            </li>
            <li>
              <button className="text_link" onClick={loginModalOn}>
                <a>로그인</a>
              </button>
            </li>
          </ul>
        </nav>
      </header>
    </div>
  );
}
