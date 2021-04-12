import axios from 'axios';
import Post from './NewPost';
import React, {useState, useEffect, useRef} from "react";

function Profile() {

    const userName = "ethan";

    const userData = () => {
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
                console.log(response.data["user"]);
                return response.data["user"];
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    const userPosts = () => {
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
                console.log(response.data["posts"]);
                return response.data["posts"];
            })
            .catch(function (error) {
                console.log(error);
            });

    }
    return (
        <div className="profile">
            <div className="profileHeader">
                <div className="profilePic"></div>
                <p className="username">{userName}</p>
                <p className="bio">{userData()}</p>
            </div>
            <div className="profileGrid">
                {userPosts()[0]}
                {/*{userPosts().map((post, idx) => (*/}
                {/*    <Post> className={"profileItem"} key={idx}*/}
                {/*        <img src={post.pic} alt={"picture of food"}/>*/}
                {/*    </Post>*/}
                ))}

                {/*<div className="profileItem pic1"></div>*/}
                {/*<div className="profileItem pic2"></div>*/}
                {/*<div className="profileItem pic3"></div>*/}
                {/*<div className="profileItem pic4"></div>*/}
                {/*<div className="profileItem pic5"></div>*/}
                {/*<div className="profileItem pic6"></div>*/}
                {/*<div className="profileItem pic7"></div>*/}
                {/*<div className="profileItem pic8"></div>*/}
                {/*<div className="profileItem pic9"></div>*/}
            </div>
        </div>
    )
    ;
}

export default Profile;
