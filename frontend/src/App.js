import './css/App.css';
import Feed from './pages/Feed.js';
import Profile from './pages/Profile';
import MakeNewPost from "./pages/MakeNewPost.js";
import {Route, Switch,useHistory} from 'react-router-dom';
import RestaurantSearch from "./pages/RestaurantSearch.js";
import Login from "./pages/Login.js"
import Signup from "./pages/Signup.js"
import Explore from "./pages/Explore.js"
import Pinned from "./pages/Pinned.js"
import Recommendations from "./pages/Recommendations";

function App() {

    let history = useHistory();
    let user = null;

    const getUser = (username) => {
        user = username;
    }

    const clearUser = () => {
        user = null;
    }

    function redirect() {
        if (user === null) {
            return history.push("/");
        }
    }


    return (
            <div className="App">
                <Switch>
                    <Route exact path="/myprofile"
                           render={(props) => (
                                <Profile {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/explore"
                           render={(props) => (
                               <Explore {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/myfeed"
                           render={(props) => (
                               <Feed {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/mypinned"
                           render={(props) => (
                               <Pinned {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/newpost"
                           render={(props) => (
                               <MakeNewPost {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/"
                           render={(props) => (
                               <Login {...props} getUser={getUser} /> )} />
                    <Route exact path="/search"
                           render={(props) => (
                               <RestaurantSearch {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/register" component={Signup}/>
                    <Route exact path="/recommended"
                           render={(props) => (
                               <Recommendations {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                </Switch>
            </div>
    );
}

export default App;
