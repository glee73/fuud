import { Link, useHistory } from 'react-router-dom';
import logo from "./fuud.svg";
import axios from "axios";
import React, {useEffect, useState} from "react";

function Navbar(props) {

    let history = useHistory();

    function logout() {
        props.logout();
        return (history.push("/"));
    }
    
    function searchBy(e) {
        e.preventDefault();

        let searchFor = document.getElementById('searchBar');

        if (searchFor == null) {
            return;
        } else {
            searchFor = searchFor.value;
        }
        
        const toSend = {
            "searchBar": searchFor,
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

    return (
        <div>
            <div className="header">
                <Link to="/explore">
                    <img src={logo} alt={"fuud logo"} className={"logo"}/>
                </Link>
                <form onSubmit={searchBy}>
                    <input className="searchBar shadow" type="text" placeholder="looking for something?"/>
                </form>
                <div className="link" onClick={logout}> logout </div>
            </div>
            <div className="navbarContainer">
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    <Link className="navbarLink" to="/explore">explore</Link>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    {/*<a className="navbarLink" href="">my pinned</a>*/}
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    <Link className="navbarLink" to="/myprofile">my profile</Link>
                </div>
            </div>
            <div className="newPostFixed">
                <Link className="newPostButton" to="/newPost">+</Link>
            </div>
        </div>

    );
}

export default Navbar;
