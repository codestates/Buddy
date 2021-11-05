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
