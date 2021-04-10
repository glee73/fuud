import { Link } from 'react-router-dom';

function Navbar() {
    return (
        <div>
            <div className="header">
                <h1>LOGO</h1>
                <input className="searchBar" type="text" placeholder="looking for something?"/>
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
        </div>

    );
}

export default Navbar;
