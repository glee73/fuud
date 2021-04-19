import axios from 'axios';
import React, {useState, useEffect} from "react";
import '../css/index.css';
import Navbar from "../components/Navbar.js"
import ProfilePic from "../components/ProfilePic";

function EditProfile(props) {

    let [userData, setUserData] = useState(null);
    let [newPic, setNewPic] = useState(null);
    const img = document.createElement('img');
    const canvas = document.createElement('canvas');
    const userName = localStorage.getItem("user");

    function getUserData() {
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
                console.log("Here");
                let data = response.data["user"];
                console.log(data)
                setUserData(data);
                setNewPic(data["pic"]);
                return true;
            })
            .catch(function (error) {
                console.log(error);
            });

    }
    function sendProfPic() {
        console.log("sending profile pic");
        canvas.height = img.height
        canvas.width = img.width
        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0)
        const data = canvas.toDataURL("image/png")
        console.log(data)

        const toSend = {
            "username": userName,
            "pic": data
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/profilepic',
            toSend,
            config
        )
            .then(response => {
                console.log("sent prof pic")
                setNewPic(data);
                return true;
            })
            .catch(function (error) {
                console.log(error);
            });

    }
    function sendBio() {
        let newBio = document.getElementById('newBio').value;
        console.log("sending bio");
        const toSend = {
            "username": userName,
            "bio": newBio
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/bio',
            toSend,
            config
        )
            .then(response => {
                console.log("sent bio")
                sendConfirmation();
                return true;
            })
            .catch(function (error) {
                console.log(error);
            });

    }
    useEffect(() => {
        props.getUser();
        getUserData();
    }, []);
    const rerenderpic = () => {
        sendProfPic();
        // getUserData();
        sendConfirmation();
    }
    const handleImageUpload = (event) => {
        img.setAttribute('src', URL.createObjectURL(event.target.files[0]))
    }
    const sendConfirmation = () => {
        return <p>Successfully updated!</p>
    }

    if (userData === null) {
        return <Navbar logout={props.logout}/>
    } else {
        return (
            <div>
                <Navbar logout={props.logout}/>
                <div className="editProfile">
                    <p className="pageTitle">Change Profile Picture</p>
                    <div className="profPicFlex">
                        <div className="newProfPic">
                            <ProfilePic data={newPic}/>
                            <div className="profPicFileUpload">
                                <input className={"shadow"} type="file" id="fileUpload" name="fileUpload" onChange={handleImageUpload}/>
                                <button className="submitButton changeProfPicButton" type="submit" onClick={rerenderpic}>change photo</button>
                            </div>
                        </div>
                    </div>

                    <p className="pageTitle">Edit Bio</p>
                    <div className="bioFlex">
                        <div className="newBioInput">
                            <label htmlFor="bio">New Bio: </label>
                            <input className={"shadow"} type="text" id="newBio" name="bio" placeholder={userData["bio"]}/>
                        </div>
                        <button className="submitButton changeBioButton" type="submit" onClick={sendBio}>change bio</button>
                    </div>
                </div>

            </div>
        );
    }

}

export default EditProfile;
