import axios from 'axios';
import Post from '../components/Post';
import React, {useState, useEffect, useRef} from "react";
import '../css/index.css';
import Navbar from "../components/Navbar.js"
import ProfilePic from "../components/ProfilePic";
import UserListing from "../components/UserListing";

function FollowingList(props) {
    let userName = localStorage.getItem("user");
    let [userData, setUserData] = useState(null);

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
                console.log(data);
                setUserData(data);
                return true;
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    function displayFollowing() {
        let following = [];

        if (userData.following.length === 0) {
            return <p> No followers to show.</p>
        }

        userData.following.map((usr, idx) => (
            following.push(
                <UserListing searchedUser={usr} currUser={userName}
                             key={idx}/>
            )));

        return (<div className="profileGrid">
                {following}
            </div>
        );
    }

    useEffect(() => {
        getUserData();
        props.getUser();
    },[]);


    if (userData === null || userData === undefined) {
        return "";
    }


    return (
        <div>
            <Navbar logout={props.logout}/>
            <div className="feed">
                <p className="feedTitle pageTitle">your followers</p>
                {displayFollowing()}
            </div>
        </div>
    );
}

export default FollowingList;