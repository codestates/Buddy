import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { Cookies } from 'react-cookie';
import ChatDetail from '../components/chatting/ChatDetail';
import ChatList from '../components/chatting/ChatList';
import '../styles/ChattingPage.css';

// 소켓 통신
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

// 채팅 방 컴포넌트
export function ChattingPage(props) {
  // 상태관리(chatdetail.js)
  const [chattingMessage, setChattingMessage] = useState(''); // 채팅 메시지

  // 상태관리(chatlist.js)
  const [chattingLog, setChattingLog] = useState([]); // 채팅 로그

  // 소켓 통신 객체
  const sock = new SockJS('http://localhost:8080/chatting');
  const ws = Stomp.over(sock);

  const history = useHistory();
  const cookies = new Cookies();

  // 토큰
  const token = cookies.get('refreshToken');

  // 렌더링 될 때마다 연결,구독 다른 방으로 옮길 때 연결, 구독 해제
  useEffect(() => {
    wsConnectSubscribe();
  }, []);

  // 웹소켓 연결, 구독
  function wsConnectSubscribe() {
    try {
      ws.connect(
        {
          token: token,
        },
        () => {
          ws.subscribe(
            `/sub/chat/room/2`,
            (data) => {
              const newMessage = JSON.parse(data.body);
            },
            { token: token }
          );
        }
      );
    } catch (error) {
      console.log(error);
    }
  }

  // 웹소켓이 연결될 때 까지 실행하는 함수
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
  function sendMessage() {
    try {
      // token이 없으면 로그인 페이지로 이동
      if (!token) {
        alert('토큰이 없습니다. 다시 로그인 해주세요.');
        history.replace('/');
      }
      // send할 데이터
      const data = {
        type: 'TALK',
        roomId: 2,
        userId: 1,
        sender: 'aaa',
        message: 'bbb',
        createdAt: '',
      };

      ws.send('/pub/chat/message', { token: token }, JSON.stringify(data));

      console.log(ws.ws.readyState);
    } catch (error) {
      console.log(error);
      console.log(ws.ws.readyState);
    }
  }

  return (
    <>
      <div className="chatting__page">
        <section className="chatting__wrapper">
          <ChatList chattingLog={chattingLog} setChattingLog={setChattingLog} />
          <ChatDetail chattingMessage={chattingMessage} setChattingMessage={setChattingMessage} />
        </section>
        <button onClick={sendMessage}>메세지 보내기</button>
      </div>
    </>
  );
}
