import React, { useEffect } from 'react';

// 컴포넌트
import { Tutorials } from '../components/pages/Tutorial/Tutorials';

// 라이브러리
import AOS from 'aos';
import 'aos/dist/aos.css';

// css
import '../styles/pages/Demopage.css';

export function DemoPage() {
  useEffect(() => {
    AOS.init({
      duration: 1000,
    });
  });

  return (
    <>
      <Tutorials
        h1text01="회원 가입을"
        h1text02="진행해주세요"
        spantext01="Buddy의 기능을 사용하려면"
        spantext02="회원 가입을 해야해요"
        data_aos="fade-up"
        data_aos_duration="1200"
        data_aos_easing="ease"
        src="images/tuto01.gif"
        alt="튜토리얼01"
      />
      <Tutorials
        h1text01="회원 가입을"
        h1text02="완료해볼까요?"
        spantext01="기본 정보를 입력하는 것만으로"
        spantext02="손쉽게 회원가입을 할 수 있어요"
        data_aos="fade-up"
        data_aos_duration="1200"
        data_aos_easing="ease"
        src="images/tuto02.gif"
        alt="튜토리얼02"
      />
      <Tutorials
        h1text01="회원 가입 완료 후"
        h1text02="계정을 로그인 해주세요"
        spantext01="가입 완료가 끝나면"
        spantext02="Buddy 채팅을 이용하실 수 있어요"
        data_aos="fade-up"
        data_aos_duration="1200"
        data_aos_easing="ease"
        src="images/tuto03.gif"
        alt="튜토리얼03"
      />
      <Tutorials
        h1text01="채팅을"
        h1text02="시작해볼까요?"
        spantext01="채팅 메뉴에서 버튼만 누르면"
        spantext02="자동으로 유저를 매칭해줘요"
        data_aos="fade-up"
        data_aos_duration="1200"
        data_aos_easing="ease"
        src="images/tuto04.gif"
        alt="튜토리얼04"
      />
      <Tutorials
        h1text01="새롭게 만난 친구와"
        h1text02="즐거운 대화를 나누세요"
        spantext01="미리 대기 중인 친구와"
        spantext02="이제 즐거운 대화를 나눌 수 있어요"
        data_aos="fade-up"
        data_aos_duration="1200"
        data_aos_easing="ease"
        src="images/tuto05.gif"
        alt="튜토리얼05"
      />
    </>
  );
}
