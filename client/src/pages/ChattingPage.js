import React, { useState, useEffect, useRef } from 'react';

// 라이브러리
import { useHistory } from 'react-router-dom';
import { Cookies } from 'react-cookie';
import ScrollContainer from 'react-indiana-drag-scroll';
import axios from 'axios';
import dotenv from 'dotenv';
import Swal from 'sweetalert2/dist/sweetalert2.js';
import 'sweetalert2/dist/sweetalert2.css';

// 소켓 통신
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

// Constants
import { AXIOS_DEFAULT_HEADER } from '../constants/constants';

// CSS
import '../styles/pages/ChattingPage.css';

// .env 환경변수 사용
dotenv.config();

// 쿠키 생성
const cookies = new Cookies();

// setTimeout 변수 (다른 페이지로 이동할 때 setTimeout 실행을 막아줌)
let timeouts;

// 채팅 방 컴포넌트
export function ChattingPage(props) {
  // 상태관리(ChatDetail)
  const [chattingLog, setChattingLog] = useState([]); // 채팅 로그
  const [isLoading, setIsLoding] = useState(false); // 로딩 여부

  const history = useHistory();
  const cookies = new Cookies();
  const scrollRef = useRef();

  // 소켓 통신 객체
  const sock = new SockJS(`${process.env.REACT_APP_HTTPS_URL}/chatting`);
  const ws = Stomp.over(sock);

  // 토큰
  const token = cookies.get('refreshToken');

  // 새로고침 시, 웹 소켓 연결 유지
  useEffect(() => {
    if (cookies.get('chatRoomid')) {
      if (window.performance) {
        if (performance.navigation.type === 1) {
          cookies.set('chatRoomid', '');

          Swal.fire({ title: `채팅을 종료하였습니다.`, confirmButtonText: '확인' }).then(function () {
            history.push('/');
            wsDisConnectUnsubscribe();
          });
        }
      }

      // 다른 컴포넌트로 이동할 때 실행되는 함수(마운트가 끝날 때)
      return () => {
        // console.log(ws.ws.readyState);
        if (ws.ws.readyState === 1) {
          wsDisConnectUnsubscribe();
        }
        clearTimeout(timeouts);
      };
    }
  }, [cookies.get('chatRoomid')]);

  // 새로고침 시, 방 목록 가져오기
  useEffect(() => {
    // token이 없으면 로그인 페이지로 이동
    if (!token) {
      Swal.fire({ title: '회원 전용 페이지입니다. 로그인 해주세요.', confirmButtonText: '확인' });
      history.push('/');
    }

    if (cookies.get('chatRoomid')) {
      // console.log(ws.ws.readyState);
      if (ws.ws.readyState === 0 && !cookies.get('enterroom')) {
        wsConnectSubscribe(); // 연결 함수
        cookies.set('enterroom', 'true');
      }
    }
  }, []);

  useEffect(() => {
    scrollBottom();
  }, [chattingLog]);

  // 채팅로그가 변할 때 스크롤 맨 아래로 이동
  const scrollBottom = () => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth', block: 'end' });
  };

  // 방 만들기(대기 중인 방 찾기)
  const handleCreateRoom = () => {
    setIsLoding(true);

    timeouts = setTimeout(function () {
      axios(`${process.env.REACT_APP_HTTPS_URL}/chat/room`, {
        method: 'GET',
        headers: AXIOS_DEFAULT_HEADER,
      })
        .then((res) => {
          Swal.fire({ title: `${res.data.message}`, confirmButtonText: '확인' }).then(function () {
            // 쿠키에 생성된 방 id user count 넣기
            cookies.set('chatRoomid', res.data.roomId);

            // console.log(res.data);
            window.location.replace(`/chat`);
          });
        })
        .catch((err) => {});
    }, 5000);
  };

  // 방 나가기
  const handleExitRoom = () => {
    cookies.set('chatRoomid', '');
    cookies.remove('enterroom');
    Swal.fire({ title: `채팅을 종료하였습니다.`, confirmButtonText: '확인' }).then(function () {
      history.push('/');
    });
  };

  // 웹소켓 연결, 구독
  function wsConnectSubscribe() {
    try {
      ws.connect(
        {
          token: cookies.get('refreshToken'),
        },
        () => {
          ws.subscribe(
            `/sub/chat/room/${cookies.get('chatRoomid')}`,
            (data) => {
              const newMessage = JSON.parse(data.body);
              addMessage(newMessage);
            },
            { token: cookies.get('refreshToken') }
          );
        }
      );
    } catch (error) {
      // console.log(error);
    }
  }

  // 메시지 추가
  const addMessage = (message) => {
    setChattingLog((prev) => [...prev, message]);
  };

  // 연결해제, 구독해제
  function wsDisConnectUnsubscribe() {
    try {
      ws.disconnect(
        () => {
          ws.unsubscribe('sub-0');
        },
        { token: cookies.get('refreshToken') }
      );
    } catch (error) {
      // console.log(error);
    }
  }

  // 웹소켓이 연결될 때까지 실행하는 함수
  function waitForConnection(ws, callback) {
    setTimeout(
      function () {
        // 연결되었을 때 콜백함수 실행
        if (ws.ws.readyState === 1) {
          callback();
          // 연결이 안 되었으면 재호출
        } else {
          waitForConnection(ws, callback);
        }
      },
      1 // 밀리초 간격으로 실행
    );
  }

  // 메시지 보내기
  function sendMessage(e) {
    if (e.key === 'Enter') {
      try {
        // send할 데이터
        const newMessage = {
          type: 'TALK',
          roomId: cookies.get('chatRoomid'),
          userId: props.userInfo.id,
          sender: props.userInfo.nickname,
          message: e.target.value,
          profileImage: props.userInfo.profileImage,
        };
        waitForConnection(ws, function () {
          if (ws.ws.readyState === WebSocket.OPEN) {
            ws.send('/pub/chat/message', { token: cookies.get('refreshToken') }, JSON.stringify(newMessage));
            // console.log(ws.ws.readyState);

            e.target.value = ''; // 입력 창 초기화
          }
        });
      } catch (error) {
        // console.log(error);
        // console.log(ws.ws.readyState);
      }
    }
  }

  return (
    <>
      <div className="chatting__page">
        <section className="chatting__wrapper">
          {cookies.get('chatRoomid') ? (
            <>
              <div className="chat__detail">
                <ScrollContainer className="scroll__container" horizontal={false} hideScrollbars>
                  <div className="chat__container">
                    <div className="chat__contents" ref={scrollRef}>
                      {chattingLog.map((message) =>
                        message.type === 'ENTER' ? (
                          <div className="chat__messages__container__center">
                            <div className="chat__messages__center__enter">
                              <span>
                                {message.sender} {message.message}
                              </span>
                            </div>
                          </div>
                        ) : message.type === 'QUIT' ? (
                          <div className="chat__messages__container__center">
                            <div className="chat__messages__center__quit">
                              <span>
                                {message.sender} {message.message}
                              </span>
                            </div>
                          </div>
                        ) : message.sender === props.userInfo.nickname ? (
                          <div className="chat__messages__container__right">
                            <div className="chat__messages__right">
                              <div className="chat__messages__right__contents">
                                <div className="chat__messages__right__image__contents">
                                  <img src={props.userInfo.profileImage} alt="user images right" />
                                </div>
                                <div className="chat__messages__right__describe__contents">
                                  <div className="chat__messages__right__describe__message">
                                    <span>{message.message}</span>
                                  </div>
                                  <div className="chat__messages__right__describe__createdat">
                                    <span>{message.createdAt.slice(11, message.createdAt.length)}</span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        ) : (
                          <div className="chat__messages__container__left">
                            <div className="chat__messages__left">
                              <div className="chat__messages__left__contents">
                                <div className="chat__messages__left__image__contents">
                                  <img src={message.profileImage} alt="user images left" />
                                </div>
                                <div className="chat__messages__left__describe__contents">
                                  <div className="chat__messages__left__describe__message">
                                    <span>{message.message}</span>
                                  </div>
                                  <div className="chat__messages__left__describe__createdat">
                                    <span>{message.createdAt.slice(11, message.createdAt.length)}</span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        )
                      )}
                    </div>
                  </div>
                </ScrollContainer>
                <input
                  type="text"
                  onKeyPress={sendMessage}
                  className="chat__input"
                  placeholder="메세지를 입력해주세요"
                ></input>
                <button className="chat__exit__btn" onClick={handleExitRoom}>
                  방 나가기
                </button>
              </div>
            </>
          ) : (
            <div className="chat__detail" style={{ 'align-items': 'center' }}>
              {!cookies.get('chatRoomid') && isLoading === false ? (
                <>
                  <img className="chat__main__img" src="images/chat_main_image.jpg" alt="채팅방 메인 이미지" />
                  <button className="chat__match__btn" onClick={handleCreateRoom}>
                    랜덤 매칭
                  </button>
                </>
              ) : (
                <>
                  <img className="chat__loading__img" src="images/loading.gif" alt="채팅방 로딩 이미지" />
                  <span className="chat__loading__text">대기 중인 유저를 찾고 있습니다.</span>
                </>
              )}
            </div>
          )}
        </section>
      </div>
    </>
  );
}
