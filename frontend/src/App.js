import logo from './logo.svg';
import './App.css';
import Navbar from './Navbar';
import Feed from './Feed.js';
import Profile from './Profile';
import { Route, Switch } from 'react-router-dom';
import RestaurantSearch from "./RestaurantSearch.js";


function App() {
    return (
            <div className="App">
                <Navbar></Navbar>
                <Switch>
                    <Route exact path="/myprofile" component={Profile}/>
                    <Route exact path="/explore" component={Feed}/>
                    <Route exact path="/" component={RestaurantSearch}/>
                </Switch>
            </div>
    );
}

export default App;
