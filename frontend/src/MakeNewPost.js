import axios from "axios";
import React, {useEffect, useState} from "react";
import { useHistory } from "react-router-dom";
import Navbar from "./Navbar";

function MakeNewPost(props) {

    let [searchResult, setSearchResult] = useState(null);
    let [msg, setMsg] = useState(null);

    const userName = props.user;

    console.log(userName);

    let history = useHistory();

    const img = document.createElement('img')
    const canvas = document.createElement('canvas')

    const handleImageUpload = (event) => {
        img.setAttribute('src', URL.createObjectURL(event.target.files[0]))
    }

    function sendRestaurantName(e) {
        e.preventDefault();
        let restaurantName = document.getElementById('restaurantName');

        if (restaurantName == null) {
            return;
        } else {
            restaurantName = restaurantName.value;
        }

        const toSend = {
            "restaurantName": restaurantName
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/search',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["success"]);
                setSearchResult(response.data["success"]);
                return response.data["success"];
            })
            .catch(function (error) {
                console.log(error);
            });
        console.log("after");
    }

    function showSearchResult() {
        if (searchResult === null) {
            return;
        }

        if (!searchResult) {
            return (
                <div className="newPostResult">
                    Restaurant name not found. Please try again.
                </div>
            );
        } else {
            return (
                <div className="newPostResult">
                    Restaurant found! Please continue.
                </div>);
        }
    }

    function makePost(e) {

        e.preventDefault();

        if (!searchResult) {
            setMsg(<p className="newPostResult"> please enter valid restaurant before submission </p>)
            return;
        }

        let restaurantName = document.getElementById('restaurantName').value;
        let text = document.getElementById('caption').value;
        let review = document.getElementById('rating').value;
        let timestamp = new Date().toLocaleString();

        canvas.height = img.height
        canvas.width = img.width
        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0)
        const data = canvas.toDataURL("image/png")

        const toSend = {
            "restaurantName": restaurantName,
            "text": text,
            "review": review,
            "username": userName,
            "timestamp": timestamp,
            "pic": data
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/addpost',
            toSend,
            config
        )
            .then(response => {
                if (response.data["success"]) {
                    history.push('/myprofile');
                } else {
                    setMsg(<p className="newPostResult"> post failed. please try again. </p>)
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    useEffect(() => {
        props.redirect();
    }, [])

    return (

        <div>
            <Navbar logout={props.logout}/>
            <div className="makeNewPost">
                <p className="newPostHeader">Make a New Post!</p>
                <div className="step step1">
                    <form className="formStyle restaurantNameForm" onSubmit={sendRestaurantName}>
                        <label htmlFor="restaurantName">Restaurant Name:</label>
                        <input className={"shadow"} type="text" id="restaurantName" name="restaurantName" required/>
                        <button className="submitButton searchButton" type="button" onClick={sendRestaurantName}>search</button>
                    </form>
                    {showSearchResult()}
                </div>
                <div className="step step2">
                    <form className="formStyle rating">
                        <label htmlFor="rating">Rating (out of 10):</label>
                        <input className={"shadow"} type="number" id="rating" name="rating" required/>
                    </form>
                </div>
                <div className="step step3">
                    <form className="formStyle caption">
                        <label htmlFor="caption">Caption:</label>
                        <input className={"shadow"} type="text" id="caption" name="caption" required/>
                    </form>
                </div>
                <div className="step step4">
                    <form className="formStyle caption">
                        <label htmlFor="fileUpload">Upload an image (optional):</label>
                        <input className={"shadow"} type="file" id="fileUpload" name="fileUpload" onChange={handleImageUpload}/>
                    </form>
                </div>
                {msg}
                <button className="submitButton" type="submit" onClick={makePost} action={"/myprofile"}>submit</button>
            </div>
        </div>

    );
}

export default MakeNewPost;
