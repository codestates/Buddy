import React, { useEffect } from 'react';

// 컴포넌트
import { Introduce } from '../components/pages/Landing/Introduce';
import { ChatIntro } from '../components/pages/Landing/ChatIntro';
import { DemoIntro } from '../components/pages/Landing/DemoIntro';

// 라이브러리
import AOS from 'aos';
import 'aos/dist/aos.css';

// css
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
