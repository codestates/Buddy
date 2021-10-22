import React, { useState } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import GlobalStyles from './styles/globalStyles';
import { LandingPage } from './pages/LandingPage';
import { TermPage } from './pages/TermPage';
import { PrivacyPage } from './pages/PrivacyPage';
import { LoginModal } from './components/modals/LoginModal';
import { SignupPage } from './pages/SignupPage';
import { ChatingPage } from './pages/ChatingPage';
import { DemoPage } from './pages/DemoPage';

function App() {
  const [loginOn, setLoginOn] = useState(false); // 로그인 여부 (test : true로 바꾸고 개발)
  const [modalOn, setModalOn] = useState(false); // 모달 오픈 여부
  const [userInfo, setUserInfo] = useState({}); // 로그인 회원 정보

  return (
    <BrowserRouter>
      <div className="App">
        <GlobalStyles />
        <LoginModal
          modalOn={modalOn}
          setModalOn={setModalOn}
          setLoginOn={setLoginOn}
          userInfo={userInfo}
          setUserInfo={setUserInfo}
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
            <ChatingPage />
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
          <Route exact path="/signup">
            <SignupPage />
          </Route>
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
