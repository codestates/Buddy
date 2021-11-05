import React from 'react';

// css
import '../styles/pages/PrivacyPage.css';

export function PrivacyPage() {
  return (
    <div className="privacy__page">
      <section className="privacy__wrapper">
        <h1 className="privacy__h1">{`Buddy 개인정보처리방침`}</h1>
        <span className="privacy__span">
          Buddy('https://yana-buddy.com'이하 'Buddy')은(는) 「개인정보 보호법」 제30조에 따라 정보주체의 개인정보를
          보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을
          수립·공개합니다.
        </span>
        <span className="privacy__span">○ 이 개인정보처리방침은 2021년 10월 1일부터 적용됩니다.</span>
        <h2 className="privacy__h2">제1조(개인정보의 처리 목적)</h2>
        <span className="privacy__span">
          Buddy('https://yana-buddy.com'이하 'Buddy')은(는) 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는
          개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며 이용 목적이 변경되는 경우에는 「개인정보 보호법」
          제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.
        </span>
        <span className="privacy__span">1. 홈페이지 회원가입 및 관리</span>
        <span className="privacy__span">
          회원 가입의사 확인, 회원제 서비스 제공에 따른 본인 식별·인증, 회원자격 유지·관리, 서비스 부정이용 방지, 만14세
          미만 아동의 개인정보 처리 시 법정대리인의 동의여부 확인, 각종 고지·통지, 고충처리 목적으로 개인정보를
          처리합니다.
        </span>
        <span className="privacy__span">2. 민원사무 처리</span>
        <span className="privacy__span">
          민원인의 신원 확인, 민원사항 확인, 사실조사를 위한 연락·통지, 처리결과 통보 목적으로 개인정보를 처리합니다.
        </span>
        <span className="privacy__span">3. 재화 또는 서비스 제공</span>
        <span className="privacy__span">
          물품배송, 서비스 제공, 계약서·청구서 발송, 콘텐츠 제공, 맞춤서비스 제공, 본인인증, 연령인증, 요금결제·정산,
          채권추심을 목적으로 개인정보를 처리합니다.
        </span>
        <span className="privacy__span">4. 마케팅 및 광고에의 활용</span>
        <span className="privacy__span">
          신규 서비스(제품) 개발 및 맞춤 서비스 제공, 이벤트 및 광고성 정보 제공 및 참여기회 제공 , 인구통계학적 특성에
          따른 서비스 제공 및 광고 게재 , 서비스의 유효성 확인, 접속빈도 파악 또는 회원의 서비스 이용에 대한 통계 등을
          목적으로 개인정보를 처리합니다.
        </span>
        <span className="privacy__span">5. 개인영상정보</span>
        <span className="privacy__span">
          범죄의 예방 및 수사, 시설안전 및 화재예방, 교통단속, 교통정보의 수집·분석 및 제공 등을 목적으로 개인정보를
          처리합니다.
        </span>
      </section>
    </div>
  );
}
