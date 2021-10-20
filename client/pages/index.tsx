import type { NextPage } from 'next';
import Head from 'next/head';
import Footer from '../components/Footer';
import Header from '../components/Header';
import styles from '../styles/Home.module.css';

const Home: NextPage = () => {
  return (
    <>
      <Header />
      <div className={styles.container}>Hello World!</div>
      <Footer />
    </>
  );
};

export default Home;
