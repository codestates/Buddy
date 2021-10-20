import Image from 'next/image';
import styles from '../styles/Footer.module.css';

function Footer() {
  const teamName = [
    { name: '황재성', src: 'https://github.com/shreder0804' },
    { name: '남궁민', src: 'https://github.com/nmin11' },
    { name: '남수연', src: 'https://github.com/namtndus' },
  ];

  const githubLink = teamName.map((el) => {
    return (
      <div className={styles.github} key={el.name}>
        <a className={styles.github_link} href={el.src}>
          <img src="/images/github_icon.png" alt="github icon" />
          <span>{el.name}</span>
        </a>
      </div>
    );
  });

  return (
    <>
      <div className={styles.body_wrapper}>
        <div className={styles.body_content}>
          <footer>
            <div className={styles.footer_wrapper}>{githubLink}</div>
            <span>©️2021. Buddy. All rights reserved.</span>
          </footer>
        </div>
      </div>
    </>
  );
}

export default Footer;
