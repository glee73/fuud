import React, {useEffect, useState} from "react";
import axios from "axios";

function FollowButton(props) {

    let [following, setFollowing] = useState(props.following);
    let currUser = props.currUser;
    let searchedUser = props.searchedUser;


    useEffect(() => {
        setFollowing(props.following)
    }, [searchedUser])

    useEffect(() => {
        props.update(following);
    }, [following]);

    function follow() {
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
                    setFollowing(true);
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function unfollow() {
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
            'http://localhost:4567/unfollow',
            toSend,
            config
        )
            .then((response) => {
                console.log(response.data)
                if (response.data["success"]) {
                    setFollowing(false);
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    if (following) {
        return <button className={"alreadyPinned"} onClick={unfollow}>unfollow</button>
    } else {
        return <button className={"pinButton"} onClick={follow}> follow </button>
    }
}

export default FollowButton;
