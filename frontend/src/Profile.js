import axios from 'axios';
import Post from './NewPost';
import React, {useState, useEffect, useRef} from "react";
import './Profile.css';

function Profile() {

    let [loading, setLoading] = useState(true);
    let [userData, setUserData] = useState({'username': '', 'password': '', 'followers': [], 'following': []});
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
            output()
        }, 1000)
        return () => clearTimeout(interval)
    }

    useEffect(() => {
        getUserData()
        getUserPosts()
        runInterval()
    }, [])


    function output() {
        document.getElementById('usernameplace').innerText = userName
        if (userData['following'][0] !== undefined) {
            document.getElementById('bioplace').innerText = userData["following"][0]
        }
        const pg = Object.keys(userPosts).map((post, idx) => (
            <Post className={"profileItem"} key={idx}> </Post>
        ))
        document.getElementById('pg').innerHTML = pg
    }


    return (<div className={"profile"} id = "a"><div className="profileHeader">
        <div className="profilePic"></div>
        <p className="username" id="usernameplace"></p>
        <p className="bio" id={'bioplace'}></p>
    </div>
        <div className="profileGrid" id={'pg'}>
        </div></div>);
}

export default Profile;
