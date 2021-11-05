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
