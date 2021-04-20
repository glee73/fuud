import axios from "axios";
import React, {useEffect, useState} from "react";
import { useHistory } from "react-router-dom";
import Navbar from "../components/Navbar";

function MakeNewPost(props) {

    let [searchResult, setSearchResult] = useState(null);
    let [msg, setMsg] = useState(null);
    let [confirmRestaurant, setConfirmRestaurant] = useState(null);
    let [confirmedStatus, setConfirmed] = useState(false);
    const userName = localStorage.getItem("user");
    let restaurantName;
    let history = useHistory();

    const img = document.createElement('img')
    const canvas = document.createElement('canvas')

    const handleImageUpload = (event) => {
        img.setAttribute('src', URL.createObjectURL(event.target.files[0]))
    }

    function sendRestaurantName(e) {
        e.preventDefault();

        let restaurantName = document.getElementById(
            'restaurantName').value;

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
            'http://localhost:4567/searchtopost',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["success"]);
                setSearchResult(response.data["success"]);
                if (response.data["success"]) {
                    setConfirmRestaurant(response.data["restaurant"]);
                }
                return response.data["success"];
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    const confirmRestaurantName = (restName) => {
        //document.getElementById("confirmButton").classList.add("confirmButtonClicked");
        //document.getElementById("confirmButton").innerText = "confirmed";
        console.log("confirm before");
        setConfirmed(true);
        console.log("confirm after");
        // restaurantName = restName;
        // console.log(restaurantName);

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
            // return (
            //     <div className="newPostResult">
            //         Restaurant found! Please continue.
            //     </div>);
            console.log(confirmRestaurant);
            if (confirmRestaurant === null) {
                return (<p>Unable to find restaurant</p>);
            } else {
                console.log(confirmRestaurant);
                console.log("1: " + confirmedStatus);
                return displaySearchResults();
                console.log("hi");
            }
        }
    }
    const displaySearchResults = () => {
        let content = [];
        confirmRestaurant.map((rest, idx) => (
            content.push(
                <div className="confirmRestaurant shadow" key={idx}>
                    <div className="confirmFlex">
                        <p className="confirmRestName">{rest.name}</p>
                        <button className="confirmButton" id="confirmButton" type="button" onClick={() => confirmRestaurantName(rest.name)}>confirm</button>
                    </div>
                    <p>{rest.address}</p>
                </div>
            )
            )
        );
        console.log(content);
        return (<div className="makeNewPostSearchResults">
            {content}
        </div>
        );
    }
    function makePost(e) {

        e.preventDefault();
        console.log("2: " + confirmedStatus);
        if (!confirmedStatus) {
            setMsg(<p className="newPostResult"> please confirm restaurant before submission </p>)
            return;
        }
        if (!searchResult && !confirmedStatus) {
            setMsg(<p className="newPostResult"> please enter valid restaurant before submission </p>)
            return;
        }

        // let restaurantName = document.getElementById('restaurantName').value;
        let restaurantName = confirmRestaurant[0].name;
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
                    console.log("3: " + confirmedStatus);
                    console.log(restaurantName);
                    // if (!confirmedStatus) {
                    //     setMsg(<p className="newPostResult"> please confirm restaurant. </p>)
                    // }
                    setMsg(<p className="newPostResult"> post failed. please try again. </p>)
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    useEffect(() => {
        props.getUser();
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
                    <form className="formStyle ratingForm">
                        <label htmlFor="rating">Rating (out of 10):</label>
                        <input className={"shadow"} type="number" id="rating" name="rating" min="0" max="10" required/>
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
                        <label htmlFor="fileUpload">Upload an image:</label>
                        <input className={"shadow"} type="file" id="fileUpload" name="fileUpload" onChange={handleImageUpload} accept="image/jpeg, image/png" required/>
                    </form>
                </div>
                {msg}
                <button className="submitButton" type="submit" onClick={makePost} action={"/myprofile"}>submit</button>
            </div>
        </div>

    );
}

export default MakeNewPost;
