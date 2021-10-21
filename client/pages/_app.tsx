import '../styles/globals.css';
import type { AppProps } from 'next/app';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { useState } from 'react';

function MyApp({ Component, pageProps }: AppProps) {
  const [modalOn, setModalOn] = useState(false);

  return (
    <div>
      <Header {...pageProps} modalOn={modalOn} setModalOn={setModalOn} />
      <Component {...pageProps} modalOn={modalOn} />
      <Footer />
    </div>
  );
}
export default MyApp;
