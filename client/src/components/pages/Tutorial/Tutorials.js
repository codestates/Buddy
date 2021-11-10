export function Tutorials(props) {
  return (
    <>
      {/* 튜토리얼 */}
      <div id="tutorial__container">
        <div
          id="tutorial__wrapper"
          data-aos={props.data_aos}
          data-aos-duration={props.data_aos_duration}
          data-aos-easing={props.data_aos_easing}
        >
          <div id="tutorial__image">
            <img src={props.src} alt={props.alt} />
          </div>
          <div id="tutorial__text">
            <h1>{props.h1text01}</h1>
            <h1>{props.h1text02}</h1>
            <span>{props.spantext01}</span>
            <span>{props.spantext02}</span>
          </div>
        </div>
      </div>
    </>
  );
}
