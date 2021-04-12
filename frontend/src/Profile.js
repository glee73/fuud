import axios from 'axios';
import Post from './NewPost';
import React, {useState, useEffect, useRef} from "react";
import './Profile.css';

function Profile() {

    let [loading, setLoading] = useState(true);
    let [userData, setUserData] = useState({});
    let [userPosts, setUserPosts] = useState([]);
    let [counter, setCounter] = useState(0);

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
                setCounter(counter++);
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
                setCounter(counter++);
                return response.data["posts"];
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    const runInterval = () => {
        const interval =setTimeout(()=>{
            getUserData();
            getUserPosts();
        }, 1000)
        return () => clearTimeout(interval)
    }

    useEffect(() => {
        runInterval()
    }, [])


    function output() {
        let content = document.getElementsByClassName("profile");
        if (Object.keys(userData).length === 0 || userPosts.length === 0) {
            content.innerHTML = "";
            return content;
        } else {
            content.innerHTML = (`<div className="profileHeader">
                        <div className="profilePic"></div>
                        <p className="username">{userName}</p>
                        <p className="bio">{userData["following"][0]}</p>
                    </div>
                    <div className="profileGrid">
                        {Object.keys(userPosts).map((post, idx) => (
                            <Post className={"profileItem"} key={idx}> </Post>
                        ))}
                    </div>`
            );
            console.log(content);
            return content;
        }
    }


    return (<div className={"profile"}> {output()} </div>);
}

export default Profile;
