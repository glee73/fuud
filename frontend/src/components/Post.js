import axios from "axios";
import React, {useState} from "react";
import ProfilePic from "./ProfilePic";
import PostImage from "./PostImage";

function Post(props) {

    let resID = props.resID;
    let [resName, setResName] = useState("");

    function getResName() {

        const toSend = {
            "id": resID
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/restaurantbyid',
            toSend,
            config
        )
            .then(response => {
                setResName(response.data["restaurant"].name);
                return response.data["restaurant"].name;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    getResName();

    return (
        <div className="postContainer shadow">
            <div className="postHeader">
                <ProfilePic data={props.profPic}/>
                <div className="userInfo">
                    <p className="userName">
                        <em>{props.user} rates {resName.toLowerCase()} {props.rating} out of 10 </em>
                    </p>
                    <p className="postTime"> posted @ {props.time} </p>
                </div>
            </div>
            <div className="postContent">
                <PostImage data={props.pic}/>
                <p className="postDesc">{props.desc}</p>
            </div>
        </div>
    );
}

export default Post;

