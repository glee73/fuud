import axios from 'axios';
import Post from './Post';
import React, {useState, useEffect, useRef} from "react";
import './index.css';
import Navbar from "./Navbar.js"
import BinImage from "./BinImage";

function Profile() {

    let [userData, setUserData] = useState({});
    let [userPosts, setUserPosts] = useState([]);

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
                setUserData(response.data["user"]);
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
                setUserPosts(response.data["posts"]);
                console.log(response.data["posts"]);
                return response.data["posts"];
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
                <p className="bio">{userData["bio"]}</p>
            </div>
        );
    }

    const displayPosts = () => {
        let posts = [];

        userPosts.reverse().map((post, idx) => (
            posts.push(
                <Post className={"profileItem"} key={idx}
                      user={post.user} rating={post.reviewOutOfTen}
                      desc={post.description} time={post.timestamp}
                      resID={post.restaurantID} pic={post.pictures}>
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
        if (Object.keys(userData).length === 0 || userPosts.length === 0) {
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


    return (
        <div>
            <Navbar/>
            {output()}
        </div>
    );
}

export default Profile;
