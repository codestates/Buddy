import React from 'react';
import { Link } from 'react-router-dom';
import '../../styles/layout/Footer.css';

export default function Footer() {
  const teamName = [
    { name: '황재성', src: 'https://github.com/shreder0804' },
    { name: '남궁민', src: 'https://github.com/nmin11' },
    { name: '남수연', src: 'https://github.com/namtndus' },
  ];

  const githubLink = teamName.map((el) => {
    return (
      <div className="github" key={el.name}>
        <a className="github__link" href={el.src} target="_blank">
          <img src="/images/github_icon.png" alt="github icon" />
          <span>{el.name}</span>
        </a>
      </div>
    );
  });

  return (
    <>
      <footer id="footers">
        <div id="footer__container">
          <div id="footer__wrapper">
            <div id="footer__menu">
              <ul id="footer__ul">
                <li className="footer__li">
                  <Link className="footer__link" to="/term">
                    이용약관
                  </Link>
                </li>
                <li className="footer__li">
                  <Link className="footer__link" to="/privacy">
                    개인정보처리방침
                  </Link>
                </li>
              </ul>
            </div>
          </div>
          <div id="footer__description">
            <span>(주)YANA | 공동대표 : 황재성 남궁민 남수연 | 사업자등록번호 : 012-34-56789</span>
            <span>yana1234@yana-buddy.com | 서울시 동작구 나들로12길 34 야나빌딩 5층 (01234)</span>
          </div>
          <div id="github__container">{githubLink}</div>
          <span id="footer__copyright">ⓒ2021 Buddy All rights reserved.</span>
        </div>
      </footer>
    </>
  );
}
