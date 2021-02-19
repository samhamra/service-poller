import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from 'react-router-dom';
import Services from "./Services";

export default function App() {
  return (
      <Router>
      <header></header>
        <Switch>
          <Route path="/:username" component={() => <Services/>}/>
          <Route exact path="/">
            <Redirect to="/kry" />
          </Route>
        </Switch>
      </Router>
  );
}