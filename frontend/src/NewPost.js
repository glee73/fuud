import axios from "axios";
import React, {useState, useEffect, useRef} from "react";

function NewPost(props) {

    let resID = props.resID;
    let [resName, setResName] = useState("");

    function getResName() {

        const toSend = {
            "id": resID
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        console.log("before");
        axios.post(
            'http://localhost:4567/restaurantbyid',
            toSend,
            config
        )
            .then(response => {
                setResName(response.data["restaurant"].name)
                return response.data["restaurant"].name;
            })
            .catch(function (error) {
                console.log(error);
            });
        console.log("after");
    }

    getResName();

    return (
        <div className="postContainer">
            <div className="postHeader">
                <div className="greyCircle"></div>
                <div className="userInfo">
                    <p className="userName">{props.user}</p>
                    <p>{resName}</p>
                    <p className="stars"> {props.rating}</p>
                    <p> {props.time} </p>
                </div>
            </div>
            <div className="postContent">
                {/*<img className="postImage" src={props.imgURL} />*/}
                <p className="postDesc">{props.desc}</p>
            </div>
        </div>
    );
}

export default NewPost;

