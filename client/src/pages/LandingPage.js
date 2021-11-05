import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import '../styles/LandingPage.css';

export function LandingPage() {
  useEffect(() => {
    AOS.init({
      duration: 1000,
    });
  });

  return (
    <div className="landing__page">
      <Introduce />
      <ChatIntro />
      <DemoIntro />
    </div>
  );
}

export function Introduce() {
  return (
    <>
      {/* 랜딩 페이지 소개  */}
      <div id="landing__introduce">
        <div id="introduce__wrapper" data-aos="fade-right" data-aos-duration="1200" data-aos-easing="ease">
          <div id="introduce__text">
            <h1>부담 없이 얘기하고 싶은</h1>
            <h1>친구를 찾고 계신가요?</h1>
            <span>랜덤 채팅을 통해 다양한 사람들과</span>
            <span>대화할 수 있어요</span>
          </div>
          <div id="introduce__image">
            <img src="images/buddy_main_char.png" alt="소개 캐릭터" />
          </div>
        </div>
      </div>
    </>
  );
}

export function ChatIntro() {
  return (
    <>
      {/* 채팅 관련 기능 소개  */}
      <div id="chat__intro__introduce">
        <div id="chat__intro__wrapper" data-aos="fade-left" data-aos-duration="1200" data-aos-easing="ease">
          <div id="chat__intro__image">
            <img src="images/chat_char_img.png" alt="introduce_image" />
          </div>
          <div id="chat__intro__text">
            <h1>일상 대화, 소소한 대화를</h1>
            <h1>부담없이 즐길 수 있어요</h1>
            <span>언제 어디서나 Buddy를 통해</span>
            <span>소소한 대화를 나눠보세요</span>
          </div>
        </div>
      </div>
    </>
  );
}

export function DemoIntro() {
  return (
    <>
      {/* 데모 관련 기능 소개  */}
      <div id="demo__intro__introduce">
        <div id="demo__intro__wrapper" data-aos="fade-right" data-aos-duration="1200" data-aos-easing="ease">
          <div id="demo__intro__text">
            <h1>Buddy의 기능을</h1>
            <h1>직접 체험해보세요</h1>
            <span>회원 가입없이 버디의 채팅을</span>
            <span>체험해볼 수 있습니다.</span>
            <button className="demointro__button">체험하기</button>
          </div>
          <div id="demo__intro__image">
            <img src="images/demo_char_img.png" alt="introduce_image" />
          </div>
        </div>
      </div>
    </>
  );
}
