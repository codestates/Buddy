import React, { useState } from 'react';
import '../../styles/chatting/ChatDetail.css';

export default function ChatDetail(props) {
  // 채팅메시지에서 enter 키를 누르면 메시지 value 초기화
  const handleChattingEnter = (e) => {
    if (e.key === 'Enter') {
      props.setChattingMessage('');
    }
  };

  // 채팅메시지 value값 저장
  const handleChattingChange = (e) => {
    props.setChattingMessage(e.target.value);
    console.log(props.chattingMessage);
  };

  return (
    <>
      <div className="chat__detail">
        <div className="chat__container">
          <div className="chat__log">안녕하세요</div>
        </div>
        <input
          type="text"
          onKeyPress={handleChattingEnter}
          onChange={handleChattingChange}
          value={props.chattingMessage}
          className="chat__input"
          placeholder="메세지를 입력해주세요"
        ></input>
      </div>
    </>
  );
}
