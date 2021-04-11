import axios from 'axios';
import Post from './NewPost';

function Profile() {

    const userName = () => {
        return "ckim60";
    }

    const userData = () => {

        const toSend = {
            user: userName
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/nearest',
            toSend,
            config
        )
            .then(response => {
                return response.data;
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    const userPosts = () => {

        const toSend = {
            user: userName
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/nearest',
            toSend,
            config
        )
            .then(response => {
                return response.data;
            })
            .catch(function (error) {
                console.log(error);
            });

    }




    return (
        <div className="profile">
            <div className="profileHeader">
                <div className="profilePic"></div>
                <p className="username">{userName}</p>
                <p className="bio">{userData.bio}</p>
            </div>
            <div className="profileGrid">

                {userPosts.map((post, idx) => (
                    <Post> className={"profileItem"} key={idx}
                        <img src={post.pic} alt={"picture of food"}/>
                    </Post>
                ))}

                {/*<div className="profileItem pic1"></div>*/}
                {/*<div className="profileItem pic2"></div>*/}
                {/*<div className="profileItem pic3"></div>*/}
                {/*<div className="profileItem pic4"></div>*/}
                {/*<div className="profileItem pic5"></div>*/}
                {/*<div className="profileItem pic6"></div>*/}
                {/*<div className="profileItem pic7"></div>*/}
                {/*<div className="profileItem pic8"></div>*/}
                {/*<div className="profileItem pic9"></div>*/}
            </div>
        </div>
    )
    ;
}

export default Profile;
