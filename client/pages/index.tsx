import React, { useEffect } from 'react';
import styles from '../styles/Home.module.css';
import AOS from 'aos';

// import aos styles
import 'aos/dist/aos.css';

export default function Home() {
  useEffect(() => {
    // here you can add your aos options
    AOS.init({
      offset: 300,
    });
  }, []);

  return (
    <div className={styles.landing__wrapper}>
      <Introduce />
    </div>
  );
}

function Introduce() {
  return (
    <>
      {/* 랜딩 페이지 소개  */}
      <div id={styles.landing__introduce}>
        <div id={styles.introduce__wrapper} data-aos="fade-right" data-aos-duration="1200" data-aos-easing="ease">
          <div id={styles.introduce__image}>
            <img src="images/introduce_img.png" alt="introduce_image" />
          </div>
          <div id={styles.introduce__text}>
            <h1>심심한 날 익명의 친구들과 대화를 나누고 싶으신가요?</h1>
            <span>버디(Buddy)는 다양한 주제의 챗봇과 익명의 친구들과 대화할 수 있는 채팅방이 준비되어 있습니다.</span>
            <br />
            <span>일상의 무료함을 달래고 싶을 땐 저 버디(Buddy)를 찾아주세요!</span>
          </div>
        </div>
      </div>
    </>
  );
}
