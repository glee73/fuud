import logo from './logo.svg';
import './App.css';
import Navbar from './Navbar';
import Profile from './Profile';
import { Route, Switch } from 'react-router-dom';

function App() {
    return (
        <div className="App">
            <Navbar></Navbar>
            <Profile></Profile>
            <Switch>
                <Route path="/myprofile" component={Profile}/>
            </Switch>
        </div>
    );
}

export default App;
