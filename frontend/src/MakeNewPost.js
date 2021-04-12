import axios from "axios";
import React, {useState, useEffect, useRef} from "react";
import { useHistory } from "react-router-dom";

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
        if (searchResult === false) {
            return (
                <div className="searchResult">
                    Restaurant name not found. Please try again.
                </div>
            );
        } else {
            return (<div className="searchResult"/>);
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
        <div className="makeNewPost">
            <h1 className="newPostHeader">Make a New Post!</h1>
            <div className="step step1">
                <h1 className="stepHeading">Step 1:</h1>
                <form className="formStyle restaurantNameForm">
                    <label htmlFor="restaurantName">Restaurant Name:</label>
                    <input type="text" id="restaurantName" name="restaurantName" required/>
                    <button className="submitButton searchButton" type="button" onClick={sendRestaurantName}>Search</button>
                </form>
                {showSearchResult()}
            </div>
            <div className="step step2">
                <h1 className="stepHeading">Step 2:</h1>
                <form className="formStyle rating">
                    <label htmlFor="rating">Rating (out of 10):</label>
                    <input type="number" id="rating" name="rating" required/>
                </form>
            </div>
            <div className="step step3">
                <h1 className="stepHeading">Step 3:</h1>
                <form className="formStyle caption">
                    <label htmlFor="caption">Caption:</label>
                    <input type="text" id="caption" name="caption" required/>
                </form>
            </div>
            <div className="step step4">
                <h1 className="stepHeading">Step 4:</h1>
                <form className="formStyle caption">
                    <label htmlFor="fileUpload">Upload Image (optional):</label>
                    <input type="text" id="fileUpload" name="fileUpload" placeholder="Please provide a public Google Drive link"/>
                </form>
            </div>
            <button className="submitButton" type="submit" onClick={makePost} action={"/myprofile"}>Submit</button>
        </div>
    );
}

export default MakeNewPost;
