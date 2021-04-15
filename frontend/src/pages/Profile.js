import axios from 'axios';
import Post from '../components/Post';
import React, {useState, useEffect, useRef} from "react";
import '../css/index.css';
import Navbar from "../components/Navbar.js"
import BinImage from "../components/BinImage";

function Profile(props) {

    let [userData, setUserData] = useState(null);
    let [userPosts, setUserPosts] = useState(null);

    const userName = props.user;

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
                let data = response.data["user"];
                setUserData(data);
                return true;
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
                setUserPosts(response.data["posts"]);
                return true;
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    useEffect(() => {
        getUserData();
        getUserPosts();
    }, [])

    const displayProfileHeader = () => {
        return (
            <div className="profileHeader">
                <div className="profilePic"/>
                <p className="username">{userName}</p>
                <p> {userData["followers"].length} followers &emsp;|&emsp; {userData["following"].length} following
                </p>
                <p className="bio">{userData["bio"]}</p>
            </div>
        );
    }

    const displayPosts = () => {
        let posts = [];

        if (userPosts.length === 0) {
            return (
                <p> You have made no posts. Click the button on the bottom right to get started!
                </p>
            );
        }

        userPosts.reverse().map((post, idx) => (
            posts.push(
                <Post className={"profileItem"} key={idx}
                      user={post.user} rating={post.reviewOutOfTen}
                      desc={post.description} time={post.timestamp}
                      resID={post.restaurantID} pic={post.pic}>
                </Post>
            )
        ));

        return (<div className="profileGrid">
                <p className={"pageTitle"}> your posts </p>
                {posts}
        </div>
        );
    }

    function output() {
        if (userData === null || userPosts === null) {
            return "";
        } else {
            return (
                <div className={"profile"}>
                    {displayProfileHeader()}
                    {displayPosts()}
                </div>
            );
        }
    }

    props.redirect();


    return (
        <div>
            <Navbar logout={props.logout}/>
            {output()}
        </div>
    );
}

export default Profile;