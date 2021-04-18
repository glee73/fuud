import React, {useEffect, useState} from "react";
import axios from "axios";

function Button(props) {

    let searchedUser = props.searchedUser;
    let currUser = props.currUser;
    console.log(searchedUser.followers.length)
    let [followers, setFollowers] = useState(searchedUser.followers.length);

    useEffect(() => {
        setFollowers(searchedUser.followers.length)
    }, [searchedUser])

    

    return (
        <button className={"pinButton"} onClick={follow}>click to follow</button>
    );
}

export default Button;
