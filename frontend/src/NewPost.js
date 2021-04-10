
function NewPost(props) {
    return (
        <div className="postContainer">
            <div className="postHeader">
                <div className="greyCircle"></div>
                <div className="userInfo">
                    <p className="userName">{props.username}</p>
                    <img className="stars" src="https://ecodri.com.au/wp-content/uploads/2019/08/5-Star-rating.png" />
                </div>
            </div>
            <div className="postContent">
                <img className="postImage" src={props.imgURL} />
                <p className="postDesc">blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah!</p>
            </div>
        </div>
    );
}

export default NewPost;
