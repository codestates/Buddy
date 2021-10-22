import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import GlobalStyles from './styles/globalStyles';
import { LandingPage } from './pages/LandingPage';
import { TermPage } from './pages/TermPage';
import { PrivacyPage } from './pages/PrivacyPage';

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
          <Route exact path="/"></Route>
          <Route exact path="/chat"></Route>
          <Route exact path="/demo"></Route>
          <Route exact path="/term">
            <TermPage />
          </Route>
          <Route exact path="/privacy">
            <PrivacyPage />
          </Route>
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
