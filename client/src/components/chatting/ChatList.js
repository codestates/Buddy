import '../../styles/chatting/ChatList.css';
import ScrollContainer from 'react-indiana-drag-scroll';
import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { AXIOS_DEFAULT_HEADER } from '../../constants/constants';

export default function ChatList(props) {
  useEffect(() => {
    getChattingRoomList();
  }, []);

  // 상태관리(ChatList)
  const [chattingRoomList, setChattingRoomList] = useState([]); // 채팅 리스트

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

  const ChattingList = chattingRoomList.map((ele) => (
    <Link className="chattingroomlist__link">
      <div className="chattingroomlist__image">
        <img src="images/github_icon.png" />
      </div>
      <div className="chattingroomlist__description">
        <span className="chattingroomlist__name">{ele.name}</span>
        <span className="chattingroomlist__subject">{ele.subject}</span>
      </div>
    </Link>
  ));

  return (
    <>
      <ScrollContainer className="chat__list">{ChattingList}</ScrollContainer>
    </>
  );
}
