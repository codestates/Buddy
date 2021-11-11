import { useHistory } from 'react-router-dom';

export function DemoIntro() {
  const history = useHistory();

  function moveDemopage() {
    history.push('/tutorial');
  }

  return (
    <>
      {/* 데모 관련 기능 소개  */}
      <div id="demo__intro__introduce">
        <div id="demo__intro__wrapper" data-aos="fade-right" data-aos-duration="1200" data-aos-easing="ease">
          <div id="demo__intro__text">
            <h1>Buddy 사용법을</h1>
            <h1>한번 알아볼까요</h1>
            <span>지금 바로 Buddy의 사용법을</span>
            <span>확인하실 수 있습니다.</span>
            <button className="demointro__button" onClick={moveDemopage}>
              튜토리얼
            </button>
          </div>
          <div id="demo__intro__image">
            <img src="images/demo_char_img.png" alt="introduce_image" />
          </div>
        </div>
      </div>
    </>
  );
}
