import axios from 'axios';
import Post from '../components/Post';
import React, {useState, useEffect, useRef} from "react";
import '../css/index.css';
import Navbar from "../components/Navbar.js"
import ProfilePic from "../components/ProfilePic";
import { Link } from 'react-router-dom';
import Loading from "../components/Loading";

function Profile(props) {

    let [userData, setUserData] = useState(null);
    let [userPosts, setUserPosts] = useState(null);

    const userName = localStorage.getItem("user");

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
                console.log(data["pic"]);
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
        props.getUser();
        getUserData();
        getUserPosts();
    }, [])

    const displayProfileHeader = () => {
        return (
            <div className="profileHeader shadow">
                <ProfilePic data={userData["pic"]} bigger={true}/>
                <p className="username">{userName}</p>
                <div className={"link"}>
                    <Link to={"/followers"} className={"link"}>
                        {userData["followers"].length} followers
                    </Link>
                </div>

                <div className={"link"}>
                    <Link to={"/following"} className={"link"}>
                        {userData["following"].length} following
                    </Link>
                </div>

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
                      resID={post.restaurantID} pic={post.pic} postID={post.id}
                      profPic={userData["pic"]}>
                </Post>
            )
        ));

        return (<div>
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


    if (userData === null || userPosts === null) {
        return (
            <div>
                <Navbar logout={props.logout} page={"myprofile"}/>
                <Loading text={""}/>
            </div>
        )
    }

    return (
        <div>
            <Navbar logout={props.logout} page={"myprofile"}/>
            {output()}
        </div>
    );
}

export default Profile;
