import React, {useEffect, useState} from "react";
import axios from "axios";

function UserListing(props) {

    let searchedUser = props.searchedUser;
    let currUser = props.currUser;
    console.log(searchedUser.followers.length)
    let [followers, setFollowers] = useState(searchedUser.followers.length);

    useEffect(() => {
        setFollowers(searchedUser.followers.length)
    }, [searchedUser])

    function follow() {
        console.log("hi")
        const toSend = {
            "follower": currUser,
            "followed": searchedUser.username
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/addfollower',
            toSend,
            config
        )
            .then((response) => {
                console.log(response.data)
                if (response.data["success"]) {
                    console.log(followers++)
                    setFollowers(followers++);
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <p className="restaurantTitle">{searchedUser.username}</p>
                <p> {followers} followers &emsp;|&emsp; {searchedUser.following.length} following
                </p>
                <button className={"pinButton"} onClick={follow}>click to follow</button>
            </div>
        </div>
    );
}

export default UserListing;
