import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { Cookies } from 'react-cookie';
import { AXIOS_DEFAULT_HEADER } from '../constants/constants';
import ScrollContainer from 'react-indiana-drag-scroll';
import axios from 'axios';
import dotenv from 'dotenv';
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
  const [currentRoomid, setCurrentRoomId] = useState(''); // 현재 방 id

  // 상태관리(ChatList)
  const [chattingRoomList, setChattingRoomList] = useState([]); // 채팅 리스트

  const history = useHistory();
  const cookies = new Cookies();

  // 토큰
  const token = cookies.get('refreshToken');

  // 소켓 통신 객체
  const sock = new SockJS(`${process.env.REACT_APP_API_URL}/chatting`);
  const ws = Stomp.over(sock);

  useEffect(() => {
    wsConnectSubscribe();
    console.log(chatRoomInfo);
    return () => {
      wsDisConnectUnsubscribe();
    };
  }, [currentRoomid]);

  // 새로고침 시, 방 목록 가져오기
  useEffect(() => {
    // token이 없으면 로그인 페이지로 이동
    if (!token) {
      alert('회원 전용 페이지입니다. 로그인해 주세요.');
      history.push('/');
    }
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

    axios(`${process.env.REACT_APP_API_URL}/chat/room`, {
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

  // 채팅메시지 value값 저장
  const handleChattingChange = (e) => {
    setChattingMessage(e.target.value);
    console.log(chattingMessage);
  };

  // 방 목록 받아오기
  const getChattingRoomList = () => {
    axios(`${process.env.REACT_APP_API_URL}/chat/room`, {
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
            `/sub/chat/room/${currentRoomid}`,
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

  // 연결해제, 구독해제
  function wsDisConnectUnsubscribe() {
    try {
      ws.disconnect(
        () => {
          ws.unsubscribe('sub-0');
        },
        { token: token }
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
  function sendMessage(e) {
    if (e.key === 'Enter') {
      try {
        // send할 데이터
        const data = {
          type: 'TALK',
          roomId: currentRoomid,
          userId: props.userInfo.id,
          sender: props.userInfo.nickname,
          message: chattingMessage,
          createdAt: '',
        };
        ws.send('/pub/chat/message', { token: token }, JSON.stringify(data));
        console.log(ws.ws.readyState);
        setChattingMessage('');
      } catch (error) {}
    }
  }

  const ChattingList = chattingRoomList.map((ele) => (
    <Link
      className="chattingroomlist__link"
      to={`/chat?roomid=${ele.roomId}`}
      onClick={() => {
        // 구독 채널 바꾸기
        setCurrentRoomId(ele.roomId);

        ws.unsubscribe('sub-0');
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
        <img src="images/github_icon.png" alt="Chatting Room Image" />
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
          <div>
            <div className="chat__list__maintitle">Room List</div>
            <ScrollContainer className="chat__list">{ChattingList}</ScrollContainer>
            <div className="chat__list__btnlist">
              <button onClick={handleCreateRoom}>방 만들기</button>
            </div>
          </div>
          {currentRoomid !== '' ? (
            <div className="chat__detail">
              <div className="chat__container">
                <div className="chat__log">채팅로그박스</div>
              </div>
              <input
                type="text"
                onKeyPress={sendMessage}
                onChange={handleChattingChange}
                value={chattingMessage}
                className="chat__input"
                placeholder="메세지를 입력해주세요"
              ></input>
            </div>
          ) : (
            <div className="chat__detail">
              <img src="images/chat_main_image.jpg" />
            </div>
          )}
        </section>
      </div>
    </>
  );
}
