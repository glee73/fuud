import '../css/App.css';
import Feed from './Feed.js';
import Profile from './Profile';
import MakeNewPost from "./MakeNewPost.js";
import {Route, Switch,useHistory} from 'react-router-dom';
import RestaurantSearch from "./RestaurantSearch.js";
import Login from "./Login.js"
import Signup from "./Signup.js"
import Explore from "./Explore.js"
import Pinned from "./Pinned.js"

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
                    <Route exact path="/newPost"
                           render={(props) => (
                               <MakeNewPost {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/"
                           render={(props) => (
                               <Login {...props} getUser={getUser} /> )} />
                    <Route exact path="/search"
                           render={(props) => (
                               <RestaurantSearch {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/register" component={Signup}/>
                </Switch>
            </div>
    );
}

export default App;
