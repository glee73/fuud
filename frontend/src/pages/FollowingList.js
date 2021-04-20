import axios from 'axios';
import Post from '../components/Post';
import React, {useState, useEffect, useRef} from "react";
import '../css/index.css';
import Navbar from "../components/Navbar.js"
import ProfilePic from "../components/ProfilePic";
import UserListing from "../components/UserListing";
import Loading from "../components/Loading";

function FollowingList(props) {
    let userName = localStorage.getItem("user");
    let [following, setFollowing] = useState(null);

    function getFollowing() {
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
            'http://localhost:4567/getfollowing',
            toSend,
            config
        )
            .then(response => {
                let data = response.data["following"];
                console.log(data);
                setFollowing(data);
                return true;
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    function displayFollowing() {
        let content = [];

        if (following.length === 0) {
            return <p> No users to show.</p>
        }

        following.map((usr, idx) => (
            content.push(
                <UserListing searchedUser={usr} currUser={userName}
                             key={idx}/>
            )));

        return (<div className="profileGrid">
                {content}
            </div>
        );
    }

    useEffect(() => {
        getFollowing();
        props.getUser();
    },[]);


    if (following === null || following === undefined) {
        return (
            <div>
                <Navbar logout={props.logout}/>
                <Loading text={<p className="pageTitle">following</p>}/>
            </div>
        );
    }


    return (
        <div>
            <Navbar logout={props.logout}/>
            <div className="feed">
                <p className="pageTitle">following</p>
                {displayFollowing()}
            </div>
        </div>
    );
}

export default FollowingList;