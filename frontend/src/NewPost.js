
function NewPost(props) {
    return (
        <div className="postContainer">
            <div className="postHeader">
                <div className="greyCircle"></div>
                <div className="userInfo">
                    <p className="userName">{props.user}</p>
                    <p className="stars"> {props.rating}</p>
                    <p> {props.time} </p>
                </div>
            </div>
            <div className="postContent">
                {/*<img className="postImage" src={props.imgURL} />*/}
                <p className="postDesc">{props.desc}</p>
            </div>
        </div>
    );
}

export default NewPost;

