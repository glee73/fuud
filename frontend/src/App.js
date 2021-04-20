import './css/App.css';
import Feed from './pages/Feed.js';
import Profile from './pages/Profile';
import MakeNewPost from "./pages/MakeNewPost.js";
import {Route, Switch, useHistory} from 'react-router-dom';
import Login from "./pages/Login.js"
import Signup from "./pages/Signup.js"
import Explore from "./pages/Explore.js"
import Pinned from "./pages/Pinned.js"
import Recommendations from "./pages/Recommendations";
import EditProfile from "./pages/EditProfile";
import {useEffect} from "react";
import FollowersList from "./pages/FollowersList";
import FollowingList from "./pages/FollowingList";

function App() {

    let history = useHistory();

    useEffect(() => {
        getUser();
    }, [])

    const getUser = () => {
        const loggedInUser = localStorage.getItem("user");
        if (!loggedInUser) {
            history.push("/")
        }
    }

    const clearUser = () => {
        localStorage.clear();
    }

    window.setInterval( function() {
        let dots = document.getElementById("dots");
        if (dots === null) {
            return;
        }
        if (dots.innerHTML.length > 13) {
            dots.innerHTML = "loading";
        } else {
            dots.innerHTML += ".";
        }
    }, 100);

    return (
            <div className="App">
                <Switch>
                    <Route exact path="/myprofile"
                           render={(props) => (
                                <Profile {...props} getUser={getUser} logout={clearUser}/> )} />
                    <Route exact path="/explore"
                           render={(props) => (
                               <Explore {...props} getUser={getUser} logout={clearUser} /> )} />
                    <Route exact path="/myfeed"
                           render={(props) => (
                               <Feed {...props} getUser={getUser} logout={clearUser} /> )} />
                    <Route exact path="/mypinned"
                           render={(props) => (
                               <Pinned {...props} getUser={getUser} logout={clearUser} /> )} />
                    <Route exact path="/newpost"
                           render={(props) => (
                               <MakeNewPost {...props} getUser={getUser} logout={clearUser} /> )} />
                    <Route exact path="/"
                           render={(props) => (
                               <Login {...props} /> )} />
                    <Route exact path="/register" component={Signup}/>
                    <Route exact path="/recommended"
                           render={(props) => (
                               <Recommendations {...props}  getUser={getUser} logout={clearUser} /> )} />
                    <Route exact path="/editprofile"
                           render={(props) => (
                               <EditProfile {...props} getUser={getUser} logout={clearUser}/> )} />
                    <Route exact path="/followers"
                           render={(props) => (
                               <FollowersList {...props} getUser={getUser} logout={clearUser}/> )} />
                    <Route exact path="/following"
                           render={(props) => (
                               <FollowingList {...props} getUser={getUser} logout={clearUser}/> )} />
                </Switch>
            </div>
    );
}

export default App;
