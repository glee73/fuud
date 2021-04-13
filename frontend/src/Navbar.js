import { Link } from 'react-router-dom';
import logo from "./fuud.svg";

function Navbar() {
    return (
        <div>
            <div className="header">
                <Link to="/explore">
                    <img src={logo} alt={"fuud logo"} className={"logo"}/>
                </Link>

                <input className="searchBar shadow" type="text" placeholder="looking for something?"/>
                <div className="greyCircle"></div>
            </div>
            <div className="navbarContainer">
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    <Link className="navbarLink" to="/explore">explore</Link>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    <a className="navbarLink" href="">my pinned</a>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    <Link className="navbarLink" to="/myprofile">my profile</Link>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"></div>
                    <a className="navbarLink" href="">my friends</a>
                </div>
            </div>
            <div className="newPostFixed">
                <Link className="newPostButton" to="/newPost">+</Link>
            </div>
        </div>

    );
}

export default Navbar;
