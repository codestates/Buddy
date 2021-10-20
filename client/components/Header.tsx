import Link from 'next/link';
import React from 'react';
import styles from '../styles/Header.module.css';

export default function Header() {
  return (
    <div id={styles.header__wrapper}>
      <header id={styles.header}>
        <Link href="/">
          <img src="/images/logo.png" alt="Project_TT LOGO" />
        </Link>
        <nav id={styles.nav}>
          <ul id={styles.ul}>
            <li>
              <Link href="/chat">
                <a>채팅</a>
              </Link>
            </li>
            <li>
              <Link href="/chatbot">
                <a>챗봇</a>
              </Link>
            </li>
            <li>
              <Link href="/demo">
                <a>체험하기</a>
              </Link>
            </li>
            <li>
              <Link href="/login">
                <a>로그인</a>
              </Link>
            </li>
          </ul>
        </nav>
      </header>
    </div>
  );
}
