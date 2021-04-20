import React, {useEffect, useState} from "react";
import FollowButton from "./FollowButton";

function UserListing(props) {

    let searchedUser = props.searchedUser;
    let currUser = props.currUser;
    let [following, setFollowing] = useState(searchedUser.followers.includes(currUser));
    let [followers, setFollowers] = useState(searchedUser.followers.length);

    useEffect(() => {
        setFollowers(searchedUser.followers.length);
        // setFollowing(searchedUser.followers.includes(currUser));
    }, [searchedUser])

    function updateFollowers(status) {
        setFollowing(status);
        if (status) {
            setFollowers(followers + 1);
        } else {
            setFollowers(followers - 1);
        }
    }

    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <p className="restaurantTitle">{searchedUser.username}</p>
                <p> {followers} followers &emsp;|&emsp; {searchedUser.following.length} following
                </p>
                <FollowButton following={searchedUser.followers.includes(currUser)} update={updateFollowers}
                              currUser={currUser} searchedUser={searchedUser}/>
            </div>
        </div>
    );
}

export default UserListing;
