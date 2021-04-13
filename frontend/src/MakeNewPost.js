import axios from "axios";
import React, {useState, useEffect, useRef} from "react";
import { useHistory } from "react-router-dom";
import Navbar from "./Navbar";

function MakeNewPost() {

    let [searchResult, setSearchResult] = useState(null);
    let [submitResult, setSubmitResult] = useState(null);
    let userName = "ethan";

    let history = useHistory();


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
        let pics = [];

        const toSend = {
            "restaurantName": restaurantName,
            "text": text,
            "review": review,
            "username": userName,
            "timestamp": timestamp,
            "pictures": pics
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

    return (

        <div>
            <Navbar/>
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
                        <input className={"shadow"} type="text" id="fileUpload" name="fileUpload" placeholder="Please provide a public Google Drive link"/>
                    </form>
                </div>
                <button className="submitButton" type="submit" onClick={makePost} action={"/myprofile"}>submit</button>
            </div>
        </div>
    );
}

export default MakeNewPost;
