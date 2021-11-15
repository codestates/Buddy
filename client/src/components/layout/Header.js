import React from 'react';
import { Link, useHistory } from 'react-router-dom';

// 라이브러리
import { Cookies } from 'react-cookie';
import dotenv from 'dotenv';
import axios from 'axios';
import Swal from 'sweetalert2/dist/sweetalert2.js';
import 'sweetalert2/dist/sweetalert2.css';

// Constants
import { AXIOS_DEFAULT_HEADER } from '../../constants/constants';

// css
import '../../styles/layout/Header.css';

// .env 환경변수 사용
dotenv.config();

export default function Header(props) {
  const history = useHistory();

  const cookies = new Cookies();

  // 토큰
  const token = cookies.get('refreshToken');

  const loginModalOpen = () => {
    props.setModalOn(true);
    // console.log(props.modalOn);
  };

  const handleSignOut = () => {
    axios(`${process.env.REACT_APP_HTTPS_URL}/logout`, {
      method: 'POST',
      data: { email: props.userInfo.email },
      headers: AXIOS_DEFAULT_HEADER,
    })
      .then((res) => {
        // console.log(res.data);
        cookies.remove('refreshToken');
        history.push('/');
        Swal.fire({ title: '정상적으로 로그아웃되었습니다.', confirmButtonText: '확인' });
        props.setLoginOn(false);
        props.setUserInfo({});
        cookies.remove('chatRoomid');
        cookies.remove('enterroom');
      })
      .catch((err) => {});
  };

  const handleOnlyUserError = () => {
    Swal.fire({ title: '로그인 후 사용가능합니다. 로그인 해주세요.', confirmButtonText: '확인' });
    cookies.remove('chatRoomid');
    cookies.remove('enterroom');
  };

  const chatroomidDelete = () => {
    cookies.remove('chatRoomid');
    cookies.remove('enterroom');
  };

  return (
    <div id="header__wrapper">
      <header id="headers">
        <Link className="header__link" to="/">
          <img src="/images/logo.png" alt="Buddy LOGO" />
        </Link>
        <nav id="nav">
          <ul id="ul">
            <li>
              {!token ? (
                <Link className="header__link" to="/" onClick={handleOnlyUserError}>
                  채팅
                </Link>
              ) : (
                <Link className="header__link" to="/chat">
                  채팅
                </Link>
              )}
            </li>
            <li>
              <Link className="header__link" to="/tutorial" onClick={chatroomidDelete}>
                튜토리얼
              </Link>
            </li>
            {props.loginOn === true ? (
              <li>
                <Link className="header__link" to="/mypage" onClick={chatroomidDelete}>
                  마이페이지
                </Link>
              </li>
            ) : null}
            <li>
              {props.loginOn === true ? (
                <button className="header__link" onClick={handleSignOut}>
                  로그아웃
                </button>
              ) : (
                <button className="header__link" onClick={loginModalOpen}>
                  로그인
                </button>
              )}
            </li>
          </ul>
        </nav>
      </header>
    </div>
  );
}
