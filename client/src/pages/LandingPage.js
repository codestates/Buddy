import React, { useEffect } from 'react';
import '../styles/LandingPage.css';
import AOS from 'aos';
import 'aos/dist/aos.css';

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
          <div id="introduce__image">
            <img src="images/introduce_img.png" alt="introduce_image" />
          </div>
          <div id="introduce__text">
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
            <h1>모르는 사람, 친한 친구와 채팅할 수 있어요</h1>
            <span>익명 채팅방 및 친구 채팅방을 통해 다양한 사람들과 대화할 수 있어요.</span>
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
          <div id="demo__intro__image">
            <img src="images/demo_char_img.png" alt="introduce_image" />
          </div>
          <div id="demo__intro__text">
            <h1>버디(Buddy)의 기능을 직접 체험해보세요</h1>
            <span>회원 가입없이 버디의 채팅 및 챗봇을 체험해볼 수 있습니다.</span>
          </div>
        </div>
      </div>
    </>
  );
}
