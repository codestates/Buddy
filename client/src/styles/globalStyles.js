import { createGlobalStyle } from 'styled-components';
import reset from 'styled-reset';

// 전역으로 css를 reset시키는 컴포넌트
const globalStyles = createGlobalStyle`
    ${reset};
  
    @font-face {
      font-family: 'Noto Sans KR';
      font-style: normal;
      font-weight: 100;
      src: url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Thin.woff2) format('woff2'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Thin.woff) format('woff'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Thin.otf) format('opentype');
    }
    @font-face {
      font-family: 'Noto Sans KR';
      font-style: normal;
      font-weight: 300;
      src: url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Light.woff2) format('woff2'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Light.woff) format('woff'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Light.otf) format('opentype');
    }
    @font-face {
      font-family: 'Noto Sans KR';
      font-style: normal;
      font-weight: 400;
      src: url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Regular.woff2) format('woff2'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Regular.woff) format('woff'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Regular.otf) format('opentype');
    }
    @font-face {
      font-family: 'Noto Sans KR';
      font-style: normal;
      font-weight: 500;
      src: url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Medium.woff2) format('woff2'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Medium.woff) format('woff'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Medium.otf) format('opentype');
    }
    @font-face {
      font-family: 'Noto Sans KR';
      font-style: normal;
      font-weight: 700;
      src: url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Bold.woff2) format('woff2'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Bold.woff) format('woff'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Bold.otf) format('opentype');
    }
    @font-face {
      font-family: 'Noto Sans KR';
      font-style: normal;
      font-weight: 900;
      src: url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Black.woff2) format('woff2'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Black.woff) format('woff'),
        url(//fonts.gstatic.com/ea/notosanskr/v2/NotoSansKR-Black.otf) format('opentype');
    }


    /* sweetalert2 */
    .swal2-title {
      font-family: 'Noto Sans KR';
      position: relative;
      max-width: 100%;
      margin: 0;
      margin-top: 10px;
      padding: 0.8em 1em 0;
      color: #595959;
      font-size: 1.1em;
      font-weight: 600;
      text-align: center;
      text-transform: none;
      word-wrap: break-word;
    }

    .swal2-styled.swal2-confirm {
      border: 0;
      border-radius: 0.25em;
      background: initial;
      background-color: #56bb67;
      color: #fff;
      font-size: 0.9em;
    }


`;

export default globalStyles;
