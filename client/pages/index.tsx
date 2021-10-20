import type { NextPage } from 'next';
import React from 'react';
import Footer from '../components/Footer';
import Header from '../components/Header';
import styles from '../styles/Home.module.css';

export default function Home() {
  return (
    <div>
      <div className={styles.container}>Hello World!</div>
    </div>
  );
}
