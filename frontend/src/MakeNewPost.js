import axios from "axios";
import React, {useEffect, useState} from "react";
import { useHistory } from "react-router-dom";
import Navbar from "./Navbar";

function MakeNewPost(props) {

    let [searchResult, setSearchResult] = useState(null);
    let [submitResult, setSubmitResult] = useState(null);

    const userName = props.user;

    console.log(userName);

    let history = useHistory();

    const img = document.createElement('img')
    const canvas = document.createElement('canvas')

    const handleImageUpload = (event) => {
        img.setAttribute('src', URL.createObjectURL(event.target.files[0]))
    }

    function sendRestaurantName() {
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

        console.log("before");
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

        if (searchResult === false) {
            return (
                <div className="searchResult">
                    Restaurant name not found. Please try again.
                </div>
            );
        } else {
            return (
                <div className="searchResult">
                    Restaurant found! Please continue.
                </div>);
        }
    }

    function makePost() {

        let restaurantName = document.getElementById('restaurantName').value;
        let text = document.getElementById('caption').value;
        let review = document.getElementById('rating').value;
        let timestamp = new Date().toLocaleString();

        console.log(img)
        console.log(img.height)
        canvas.height = img.height
        canvas.width = img.width
        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0)
        const data = canvas.toDataURL("image/png")

        console.log(canvas)
        console.log(data)

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
                console.log(response.data["success"]);
                setSubmitResult(response.data["success"]);
                history.push('/myprofile');
                return response.data["success"];
            })
            .catch(function (error) {
                console.log(error);
            });
        console.log("after");
    }

    props.redirect();

    return (

        <div>
            <Navbar logout={props.logout}/>
            <div className="makeNewPost">
                <p className="newPostHeader">Make a New Post!</p>
                <div className="step step1">
                    <form className="formStyle restaurantNameForm">
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
                        <label htmlFor="fileUpload">Upload Image (optional):</label>
                        <input className={"shadow"} type="file" id="fileUpload" name="fileUpload" onChange={handleImageUpload}/>
                    </form>
                </div>
                <button className="submitButton" type="submit" onClick={makePost} action={"/myprofile"}>submit</button>
            </div>
        </div>
    );
}

export default MakeNewPost;
