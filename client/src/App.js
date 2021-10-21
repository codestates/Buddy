import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import GlobalStyles from './styles/globalStyles';
import { LandingPage } from './pages/LandingPage';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <GlobalStyles />
        <Header />
        <Switch>
          <Route exact path="/">
            <LandingPage />
          </Route>
          <Route exact path="/chat"></Route>
          <Route exact path="/demo"></Route>
          <Route exact path="/"></Route>
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
