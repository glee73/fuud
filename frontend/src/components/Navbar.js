import { Link, useHistory } from 'react-router-dom';
import logo from "../imgs/fuud.svg";
import React, {useEffect, useState} from "react";

function Navbar(props) {

    let history = useHistory();


    let [page, setPage] = useState("myfeed");

    function logout() {
        props.logout();
        return (history.push("/"));
    }

    useEffect(() => {
       if (document.getElementById(page) === null ||
           document.getElementById(props.page) === null) {
            return;
       }

        document.getElementById(page).classList.remove("navbarLinkClicked");
        document.getElementById(props.page).classList.add("navbarLinkClicked");
        setPage(props.page);
    }, [page])

    return (
        <div>
            <div className="header">
                <Link to="/myfeed">
                    <img src={logo} alt={"fuud logo"} className={"logo"}/>
                </Link>
                <div className="topNavBar">
                    <Link className="edit" to={"/editprofile"}>edit profile</Link>
                    <div className="link" onClick={logout}> logout </div>
                </div>

            </div>
            <div className="navbarContainer">
                <div className="navLinkFlex">
                    <Link id={"myfeed"} className="navbarLink" to={"/myfeed"}>my feed</Link>
                </div>
                <div className="navLinkFlex">
                    <Link id={"explore"} className={"navbarLink"} to={"/explore"}> explore </Link>
                </div>
                <div className="navLinkFlex">
                    <Link id={"mypinned"} className={"navbarLink"} to={"/mypinned"}> my pinned </Link>
                </div>
                <div className="navLinkFlex">
                    <Link id={"recommended"} className="navbarLink" to="/recommended">recommended</Link>
                </div>
                <div className="navLinkFlex">
                    <Link id={"myprofile"} className="navbarLink" to="/myprofile">my profile</Link>
                </div>
            </div>
            <div className="newPostFixed">
                <Link className="newPostButton" to="/newpost">+</Link>
                <div className={"newPostTag"}> new post </div>
            </div>
        </div>

    );
}

export default Navbar;
