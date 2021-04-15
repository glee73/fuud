import './App.css';
import Feed from './Feed.js';
import Profile from './Profile';
import MakeNewPost from "./MakeNewPost.js";
import {Route, Switch,useHistory} from 'react-router-dom';
import RestaurantSearch from "./RestaurantSearch.js";
import Login from "./Login.js"
import Signup from "./Signup.js"

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
                {/*https://drive.google.com/file/d/0B6wwyazyzml-OGQ3VUo0Z2thdmc/view*/}
                {/*https://drive.google.com/uc?export=view&id=0B6wwyazyzml-OGQ3VUo0Z2thdmc*/}

                {/*https://drive.google.com/file/d/1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf/view*/}
                {/*https://drive.google.com/uc?export=view&id=1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf*/}
                {/*<img width="640" height="auto" src="https://drive.google.com/uc?export=view&id=1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf"/>*/}
                <Switch>
                    <Route exact path="/myprofile"
                           render={(props) => (
                                <Profile {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/explore"
                           render={(props) => (
                               <Feed {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/newPost"
                           render={(props) => (
                               <MakeNewPost {...props} user={user} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/"
                           render={(props) => (
                               <Login {...props} getUser={getUser} /> )} />
                    <Route exact path="/search"
                           render={(props) => (
                               <RestaurantSearch {...props} getUser={getUser} logout={clearUser} redirect={redirect}/> )} />
                    <Route exact path="/register" component={Signup}/>
                </Switch>
            </div>
    );
}

export default App;
