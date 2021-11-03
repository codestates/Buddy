import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { Cookies } from 'react-cookie';
import dotenv from 'dotenv';
import { AXIOS_DEFAULT_HEADER } from '../constants/constants';
import axios from 'axios';
import ScrollContainer from 'react-indiana-drag-scroll';
import '../styles/ChattingPage.css';

// 소켓 통신
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

// .env 환경변수 사용
dotenv.config();

// 채팅 방 컴포넌트
export function ChattingPage(props) {
  // 상태관리(ChatDetail)
  const [chattingMessage, setChattingMessage] = useState(''); // 채팅 메시지
  const [chattingLog, setChattingLog] = useState([]); // 채팅 로그

  // 상태관리(ChattingPage)
  const [chatRoomInfo, setChatRoomInfo] = useState([]); // 채팅방 정보

  // 상태관리(ChatList)
  const [chattingRoomList, setChattingRoomList] = useState([]); // 채팅 리스트

  const history = useHistory();
  const cookies = new Cookies();

  // 토큰
  const token = cookies.get('refreshToken');

  // 소켓 통신 객체
  const sock = new SockJS(`http://localhost:8080/chatting`);
  const ws = Stomp.over(sock);

  // 렌더링 될 때마다 연결,구독 다른 방으로 옮길 때 연결, 구독 해제
  useEffect(() => {
    wsConnectSubscribe();
    console.log(chatRoomInfo);
  }, [chatRoomInfo]);

  // 새로고침 시, 방 목록 가져오기
  useEffect(() => {
    getChattingRoomList();
  }, []);

  // 방 만들기
  const handleCreateRoom = () => {
    const createRoomUserInfo = {
      name: '채팅방 테스트',
      image: '#',
      subject: '일상생활',
      userId: props.userInfo.id,
    };

    axios(`http://localhost:8080/chat/room`, {
      method: 'POST',
      data: createRoomUserInfo,
      headers: AXIOS_DEFAULT_HEADER,
    })
      .then((res) => {
        alert('방이 생성되었습니다');
        console.log(res.data);
        setChatRoomInfo(res.data);
        window.location.replace('/chat');
      })
      .catch((err) => {
        alert('방 생성에 실패하였습니다');
      });
  };

  const handleChattingEnter = (e) => {
    if (e.key === 'Enter') {
      setChattingMessage('');
    }
  };

  // 채팅메시지 value값 저장
  const handleChattingChange = (e) => {
    props.setChattingMessage(e.target.value);
    console.log(chattingMessage);
  };

  // 방 목록 받아오기
  const getChattingRoomList = () => {
    axios(`http://localhost:8080/chat/room`, {
      method: 'GET',
      headers: AXIOS_DEFAULT_HEADER,
    })
      .then((res) => {
        console.log(res.data);
        setChattingRoomList(res.data);
      })
      .catch((err) => {});
  };

  // 웹소켓 연결, 구독
  function wsConnectSubscribe() {
    try {
      ws.connect(
        {
          token: token,
        },
        () => {
          ws.subscribe(
            `/sub/chat/room/${chatRoomInfo.roomId}`,
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
        history.push('/');
      }
      // send할 데이터
      const data = {
        type: 'TALK',
        roomId: 'undefined',
        chatUserId: props.userInfo.id,
        sender: props.userInfo.nickname,
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

  const ChattingList = chattingRoomList.map((ele) => (
    <Link
      className="chattingroomlist__link"
      to={`/chat?roomid=${ele.roomId}`}
      onClick={() => {
        // 구독 채널 바꾸기
        ws.subscribe(
          `/sub/chat/room/${ele.roomId}`,
          (data) => {
            const newMessage = JSON.parse(data.body);
          },
          { token: token }
        );
      }}
    >
      <div className="chattingroomlist__image">
        <img src="images/github_icon.png" />
      </div>
      <div className="chattingroomlist__description">
        <span className="chattingroomlist__name">{ele.name}</span>
        <span className="chattingroomlist__subject">{ele.subject}</span>
        <span className="chattingroomlist__nickname">{ele.subject}</span>
      </div>
    </Link>
  ));

  return (
    <>
      <div className="chatting__page">
        <section className="chatting__wrapper">
          <ScrollContainer className="chat__list">{ChattingList}</ScrollContainer>
          <div className="chat__detail">
            <div className="chat__container">
              <div className="chat__log">안녕하세요</div>
            </div>
            <input
              type="text"
              onKeyPress={handleChattingEnter}
              onChange={handleChattingChange}
              value={chattingMessage}
              className="chat__input"
              placeholder="메세지를 입력해주세요"
            ></input>
          </div>
        </section>
        <button onClick={handleCreateRoom}>방 만들기</button>
        <button onClick={sendMessage}>메시지 보내기</button>
      </div>
    </>
  );
}
