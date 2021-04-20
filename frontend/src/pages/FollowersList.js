import React, {useEffect, useState} from "react";
import axios from "axios";
import UserListing from "../components/UserListing";
import Navbar from "../components/Navbar";
import Loading from "../components/Loading";

function FollowersList(props) {
    let userName = localStorage.getItem("user");
    let [followers, setFollowing] = useState(null);

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
            'http://localhost:4567/getfollowers',
            toSend,
            config
        )
            .then(response => {
                let data = response.data["followers"];
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

        if (followers.length === 0) {
            return <p> No users to show.</p>
        }

        followers.map((usr, idx) => (
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
    }, []);


    if (followers === null || followers === undefined) {
        return (
            <div>
                <Navbar logout={props.logout}/>
                <Loading text={<p className="pageTitle">followers</p>}/>
            </div>
        );
    }


    return (
        <div>
            <Navbar logout={props.logout}/>
            <div className="feed">
                <p className="pageTitle">followers</p>
                {displayFollowing()}
            </div>
        </div>
    );
}

export default FollowersList;