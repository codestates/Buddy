import React from 'react';
import '../styles/Footer.css';

export default function Footer() {
  const teamName = [
    { name: '황재성', src: 'https://github.com/shreder0804' },
    { name: '남궁민', src: 'https://github.com/nmin11' },
    { name: '남수연', src: 'https://github.com/namtndus' },
  ];

  const githubLink = teamName.map((el) => {
    return (
      <div className="github" key={el.name}>
        <a className="github__link" href={el.src}>
          <img src="/images/github_icon.png" alt="github icon" />
          <span>{el.name}</span>
        </a>
      </div>
    );
  });

  return (
    <>
      <div className="body__wrapper">
        <div className="body__content">
          <footer className="footer">
            <div className="footer__wrapper">{githubLink}</div>
            <span>©️2021. Buddy. All rights reserved..</span>
          </footer>
        </div>
      </div>
    </>
  );
}
