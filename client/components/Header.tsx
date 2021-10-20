import styles from '../styles/Header.module.css';
import Image from 'next/image';

const Header = () => {
  return (
    <header id={styles.header}>
      <img src="/images/projecttt_logo.png" alt="Project_TT LOGO" />
      <nav id={styles.nav}>
        <ul id={styles.ul}>
          <li>테마별 여행</li>
          <li>여행지 리뷰</li>
          <li>마이 페이지</li>
          <li>
            <button className={styles.text__link}>로그아웃</button>
          </li>
          <li>회원가입</li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
