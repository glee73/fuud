import axios from "axios";
import React, {useState} from "react";
import BinImage from "./BinImage";

function Post(props) {

    let resID = props.resID;
    let [resName, setResName] = useState("");

    function parseURL(url) {
        const newURL = url[0].replace("file/d/", "uc?export=view&id=");
        const finalURL = newURL.substring(0, newURL.length - 5);
        console.log(finalURL);
        return finalURL;
    }

    function isValidURL(url) {
        if (url === 'undefined') {
            return ("");
        } else {
            if (url.length !== 0) {
                return (<img src={parseURL(url)} className="postImage"/>);
            } else {
                return ("");
            }

        }
    }

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
                <div className="greyCircle"/>
                <div className="userInfo">
                    <p className="userName">
                        <em>{props.user} rates {resName.toLowerCase()} {props.rating} out of 10 </em>
                    </p>
                    <p className="postTime"> posted @ {props.time} </p>
                </div>
            </div>
            <div className="postContent">
                {isValidURL(props.pic)}
                <p className="postDesc">{props.desc}</p>
            </div>
        </div>
    );
}

export default Post;
