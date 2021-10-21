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
      <ChatIntro />
      <ChatbotIntro />
      <DemoIntro />
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

function ChatIntro() {
  return (
    <>
      {/* 채팅 관련 기능 소개  */}
      <div id={styles.landing__introduce}>
        <div id={styles.introduce__wrapper} data-aos="fade-left" data-aos-duration="1200" data-aos-easing="ease">
          <div id={styles.introduce__image}>
            <img src="images/chat_char_img.png" alt="introduce_image" />
          </div>
          <div id={styles.introduce__text}>
            <h1>모르는 사람, 친한 친구와 채팅할 수 있어요</h1>
            <span>익명 채팅방 및 친구 채팅방을 통해 다양한 사람들과 대화할 수 있어요.</span>
          </div>
        </div>
      </div>
    </>
  );
}

function ChatbotIntro() {
  return (
    <>
      {/* 챗봇 관련 기능 소개  */}
      <div id={styles.landing__introduce}>
        <div id={styles.introduce__wrapper} data-aos="fade-right" data-aos-duration="1200" data-aos-easing="ease">
          <div id={styles.introduce__image}>
            <img src="images/chatbot_char_img.png" alt="introduce_image" />
          </div>
          <div id={styles.introduce__text}>
            <h1>다양한 주제를 통해 재미있는 챗봇을 체험해보세요</h1>
            <span>연애, 타로, 별자리 등 흥미로운 주제들이 준비되어 있습니다.</span>
          </div>
        </div>
      </div>
    </>
  );
}

function DemoIntro() {
  return (
    <>
      {/* 데모 관련 기능 소개  */}
      <div id={styles.landing__introduce}>
        <div id={styles.introduce__wrapper} data-aos="fade-left" data-aos-duration="1200" data-aos-easing="ease">
          <div id={styles.introduce__image}>
            <img src="images/demo_char_img.png" alt="introduce_image" />
          </div>
          <div id={styles.introduce__text}>
            <h1>버디(Buddy)의 기능을 직접 체험해보세요</h1>
            <span>회원 가입없이 버디의 채팅 및 챗봇을 체험해볼 수 있습니다.</span>
          </div>
        </div>
      </div>
    </>
  );
}
