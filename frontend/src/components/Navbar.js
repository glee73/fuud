import { Link, useHistory } from 'react-router-dom';
import logo from "../imgs/fuud.svg";

function Navbar(props) {

    let history = useHistory();

    function logout() {
        props.logout();
        return (history.push("/"));
    }

    return (
        <div>
            <div className="header">
                <Link to="/feed">
                    <img src={logo} alt={"fuud logo"} className={"logo"}/>
                </Link>
                <div className="link" onClick={logout}> logout </div>
            </div>
            <div className="navbarContainer">
                <div className="navLinkFlex">
                    <div className="orangeCircle"/>
                    <Link className="navbarLink" to={"/myfeed"}>my feed</Link>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"/>
                    <Link className={"navbarLink"} to={"/explore"}> explore </Link>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"/>
                    <Link className={"navbarLink"} to={"/mypinned"}> my pinned </Link>
                </div>
                <div className="navLinkFlex">
                    <div className="orangeCircle"/>
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
