import React, { useState, useEffect } from 'react';

// 라이브러리
import { Cookies } from 'react-cookie';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import axios from 'axios';

// 컴포넌트
import Header from './components/layout/Header';
import Footer from './components/layout/Footer';
import { LoginModal } from './components/modals/LoginModal';

// 페이지
import { MyPage } from './pages/MyPage';
import { LandingPage } from './pages/LandingPage';
import { TermPage } from './pages/TermPage';
import { PrivacyPage } from './pages/PrivacyPage';
import { ChattingPage } from './pages/ChattingPage';
import { DemoPage } from './pages/DemoPage';

// 유틸
import ScrollToTop from './utils/ScrollToTop';
import GlobalStyles from './utils/GlobalStyles';

// Constants
import { AXIOS_DEFAULT_HEADER } from './constants/constants';

// 도메인간 쿠키 전송을 하기 위해서는 true로 해야 함.
axios.defaults.withCredentials = true;

function App() {
  const [loginOn, setLoginOn] = useState(false); // 로그인 여부 (test : true로 바꾸고 개발)
  const [modalOn, setModalOn] = useState(false); // 모달 오픈 여부
  const [userInfo, setUserInfo] = useState({}); // 로그인 회원 정보

  const cookies = new Cookies();

  // 새로고침해도 로그인 유지
  useEffect(() => {
    accessTokenCheck(); //마운트 될 때만 실행된다.
  }, []);

  const accessTokenCheck = () => {
    // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
    axios.defaults.headers.common['Authorization'] = `Bearer ${cookies.get('refreshToken')}`;

    // 윗 줄에 기본 헤더로 `Bearer ${accessToken}`를 넣었기 때문에
    // 해당 accesstoken이 유효하면 GET 요청으로 로그인 회원 정보를 받아옴
    axios(`${process.env.REACT_APP_HTTPS_URL}/token_check`, {
      method: 'GET',
      headers: AXIOS_DEFAULT_HEADER,
    })
      .then((res) => {
        // id, pw가 맞고 토큰이 유효하면 받아온 데이터를 userInfo에 저장
        console.log(res.data);
        console.log(userInfo);
        setUserInfo(res.data);

        // useHistory를 사용하여 로그인 성공시 모달창을 끄고 mypage로 이동
        setModalOn(false);
        setLoginOn(true);
      })
      .catch((err) => {});
  };

  return (
    <BrowserRouter>
      <ScrollToTop />
      <div className="App">
        <GlobalStyles />
        <LoginModal
          modalOn={modalOn}
          setModalOn={setModalOn}
          setLoginOn={setLoginOn}
          userInfo={userInfo}
          setUserInfo={setUserInfo}
          accessTokenCheck={accessTokenCheck}
        />
        <Header
          modalOn={modalOn}
          setModalOn={setModalOn}
          setUserInfo={setUserInfo}
          loginOn={loginOn}
          setLoginOn={setLoginOn}
        />
        <Switch>
          <Route exact path="/">
            <LandingPage />
          </Route>
          <Route exact path="/"></Route>
          <Route exact path="/chat">
            <ChattingPage userInfo={userInfo} />
          </Route>
          <Route exact path="/demo">
            <DemoPage />
          </Route>
          <Route exact path="/term">
            <TermPage />
          </Route>
          <Route exact path="/privacy">
            <PrivacyPage />
          </Route>
          <Route exact path="/mypage">
            <MyPage userInfo={userInfo} setLoginOn={setLoginOn} />
          </Route>
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
