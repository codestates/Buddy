import styles from '../styles/pages/login.module.css';

export default function login(modalOn : Boolean) {
  return (
    <div id={styles.login__wrapper}>login!
      {modalOn ? (
        <div className="popup">
          <div className="popup_inner">
            <div id="signin_close_btn">
              <button>닫기</button>
            </div>
            <div id="signin_contents">
              <img src="../../public/images/log.png" alt="로고 사진" />
              <span id="signin_title">로그인</span>
              <fieldset>
                <input
                  className="signin-input"
                  type="email"
                  id="username"
                  placeholder="이메일"
                ></input>
              </fieldset>
              <fieldset>
                <input
                  className="signin-input"
                  type="password"
                  id="password"
                  placeholder="비밀번호"
                ></input>
              </fieldset>
              <div id="signin_btn">
                <button className="signin_btn_contents">
                  로그인
                </button>
              </div>
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
}
