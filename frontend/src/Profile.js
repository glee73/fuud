import axios from 'axios';
import Post from './NewPost';
import React, {useState, useEffect, useRef} from "react";
import './Profile.css';

function Profile() {

    let [loading, setLoading] = useState(true);
    let [userData, setUserData] = useState({});
    let [userPosts, setUserPosts] = useState([]);
    let gotInfo = 0;

    const userName = "ethan";

    function getUserData() {
        console.log("getting user data");

        const toSend = {
            "username": userName
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/user',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["user"]);
                setUserData(response.data["user"]);
                gotInfo++;
                return response.data["user"];
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    function getUserPosts() {
        console.log("getting user post");

        const toSend = {
            "username": userName
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/userposts',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["posts"]);
                setUserPosts(response.data["posts"]);
                gotInfo++;
                return response.data["posts"];
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    useEffect(() => {
        window.setInterval( () => {
            let wait = document.getElementsByClassName("waiting")[0];
            if (wait.innerHTML.length > 2)
                wait.innerHTML = "";
            else
                wait.innerHTML += ".";
        }, 200);
        getUserData();
        getUserPosts();
    }, [])


    if (gotInfo < 2) {
        return (
            <div className={"waiting"}> </div>
        )
    }

    return (
        <div className="profile">
            <div className="profileHeader">
                <div className="profilePic"></div>
                <p className="username">{userName}</p>
                <p className="bio">{userData["following"][0]}</p>
            </div>
            <div className="profileGrid">
                {Object.keys(userPosts).map((post, idx) => (
                    <Post> className={"profileItem"} key={idx}
                        <img src={post["pictures"][idx]} alt={"picture of" +
                        " food"}/>
                    </Post>
                ))}

                {/*<div className="profileItem pic1"></div>*/}
                {/*<div className="profileItem pic2"></div>*/}
                {/*<div className="profileItem pic3"></div>*/}
                {/*<div className="profileItem pic4"></div>*/}
                {/*<div className="profileItem pic5"></div>*/}
                {/*<div className="profileItem pic6"></div>*/}
                {/*<div className="profileItem pic7"></div>*/}
                {/*<div className="profileItem pic8"></div>*/}
                {/*<div className="profileItem pic9"></div>*/}
            </div>
        </div>
    );
}

export default Profile;
