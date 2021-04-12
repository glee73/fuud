import './App.css';
import Navbar from './Navbar';
import Feed from './Feed.js';
import Profile from './Profile';
import MakeNewPost from "./MakeNewPost.js";
import { Route, Switch } from 'react-router-dom';
import RestaurantSearch from "./RestaurantSearch.js";


function App() {
    function parseURL(url) {
        const newURL = url.replace("file/d/", "uc?export=view&id=");
        const finalURL = newURL.substring(0, newURL.length - 5);
        return finalURL;
    }
    parseURL("https://drive.google.com/file/d/1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf/view");
    return (
            <div className="App">
                <Navbar></Navbar>
                {/*https://drive.google.com/file/d/0B6wwyazyzml-OGQ3VUo0Z2thdmc/view*/}
                {/*https://drive.google.com/uc?export=view&id=0B6wwyazyzml-OGQ3VUo0Z2thdmc*/}

                {/*https://drive.google.com/file/d/1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf/view*/}
                {/*https://drive.google.com/uc?export=view&id=1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf*/}
                {/*<img width="640" height="auto" src="https://drive.google.com/uc?export=view&id=1vv3VhsvphsHV2qUaC-3bteBcToFwxCXf"/>*/}
                <Switch>
                    <Route exact path="/myprofile" component={Profile}/>
                    <Route exact path="/explore" component={Feed}/>
                    <Route exact path="/" component={RestaurantSearch}/>
                    <Route exact path="/newPost" component={MakeNewPost}/>
                </Switch>
            </div>
    );
}

export default App;
