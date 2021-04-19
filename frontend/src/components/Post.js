import axios from "axios";
import React, {useState} from "react";
import ProfilePic from "./ProfilePic";
import PostImage from "./PostImage";

function Post(props) {

    let resID = props.resID;
    let [resName, setResName] = useState("");

    //might wantto return restaurant objinsteadfor tags

    function getResName() {

        const toSend = {
            "id": resID
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/restaurantbyid',
            toSend,
            config
        )
            .then(response => {
                setResName(response.data["restaurant"].name);
                return response.data["restaurant"].name;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    getResName();

    return (

        // <div className="postContainer shadow">
        //     <div className="postHeader">
        //         <ProfilePic data={props.profPic}/>
        //         <div className="userInfo">
        //             <p className="userName">
        //                 <em>{props.user} rates {resName.toLowerCase()} {props.rating} out of 10 </em>
        //             </p>
        //             <p className="postTime"> posted @ {props.time} </p>
        //         </div>
        //     </div>
        //     <div className="postContent">
        //         <PostImage data={props.pic}/>
        //         <p className="postDesc">{props.desc}</p>
        //     </div>
        // </div>

        <div className="postContainer shadow">
            <div className="postHeader">
                <div className={"userIcon"}>
                    <ProfilePic data={props.profPic}/>
                    <div> {props.user} </div>
                </div>
                <div className={"deletePost"}> <img className={"garbage"} alt="svgImg" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgiIGhlaWdodD0iNDgiCnZpZXdCb3g9IjAgMCAxNzIgMTcyIgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0ibm9uemVybyIgc3Ryb2tlPSJub25lIiBzdHJva2Utd2lkdGg9IjEiIHN0cm9rZS1saW5lY2FwPSJidXR0IiBzdHJva2UtbGluZWpvaW49Im1pdGVyIiBzdHJva2UtbWl0ZXJsaW1pdD0iMTAiIHN0cm9rZS1kYXNoYXJyYXk9IiIgc3Ryb2tlLWRhc2hvZmZzZXQ9IjAiIGZvbnQtZmFtaWx5PSJub25lIiBmb250LXdlaWdodD0ibm9uZSIgZm9udC1zaXplPSJub25lIiB0ZXh0LWFuY2hvcj0ibm9uZSIgc3R5bGU9Im1peC1ibGVuZC1tb2RlOiBub3JtYWwiPjxwYXRoIGQ9Ik0wLDE3MnYtMTcyaDE3MnYxNzJ6IiBmaWxsPSJub25lIj48L3BhdGg+PGcgZmlsbD0iI2ZmNjIyNSI+PHBhdGggZD0iTTcxLjY2NjY3LDE0LjMzMzMzbC03LjE2NjY3LDcuMTY2NjdoLTI4LjY2NjY3Yy00LjMsMCAtNy4xNjY2NywyLjg2NjY3IC03LjE2NjY3LDcuMTY2NjdjMCw0LjMgMi44NjY2Nyw3LjE2NjY3IDcuMTY2NjcsNy4xNjY2N2gxNC4zMzMzM2g3MS42NjY2N2gxNC4zMzMzM2M0LjMsMCA3LjE2NjY3LC0yLjg2NjY3IDcuMTY2NjcsLTcuMTY2NjdjMCwtNC4zIC0yLjg2NjY3LC03LjE2NjY3IC03LjE2NjY3LC03LjE2NjY3aC0yOC42NjY2N2wtNy4xNjY2NywtNy4xNjY2N3pNMzUuODMzMzMsNTAuMTY2Njd2OTMuMTY2NjdjMCw3Ljg4MzMzIDYuNDUsMTQuMzMzMzMgMTQuMzMzMzMsMTQuMzMzMzNoNzEuNjY2NjdjNy44ODMzMywwIDE0LjMzMzMzLC02LjQ1IDE0LjMzMzMzLC0xNC4zMzMzM3YtOTMuMTY2Njd6TTY0LjUsNjQuNWM0LjMsMCA3LjE2NjY3LDIuODY2NjcgNy4xNjY2Nyw3LjE2NjY3djY0LjVjMCw0LjMgLTIuODY2NjcsNy4xNjY2NyAtNy4xNjY2Nyw3LjE2NjY3Yy00LjMsMCAtNy4xNjY2NywtMi44NjY2NyAtNy4xNjY2NywtNy4xNjY2N3YtNjQuNWMwLC00LjMgMi44NjY2NywtNy4xNjY2NyA3LjE2NjY3LC03LjE2NjY3ek0xMDcuNSw2NC41YzQuMywwIDcuMTY2NjcsMi44NjY2NyA3LjE2NjY3LDcuMTY2Njd2NjQuNWMwLDQuMyAtMi44NjY2Nyw3LjE2NjY3IC03LjE2NjY3LDcuMTY2NjdjLTQuMywwIC03LjE2NjY3LC0yLjg2NjY3IC03LjE2NjY3LC03LjE2NjY3di02NC41YzAsLTQuMyAyLjg2NjY3LC03LjE2NjY3IDcuMTY2NjcsLTcuMTY2Njd6Ij48L3BhdGg+PC9nPjwvZz48L3N2Zz4="/> </div>
            </div>

            <div className={"postBody"}>
                <div className={"postImg"}>
                    <PostImage data={props.pic}/>
                </div>

                <div className={"postContent"}>
                    <div className={"contentSpecs"}>
                        <div className={"rating"}>
                            <span className="numericRating">{props.rating}</span>
                             <span className="outOf10">/ 10</span>
                        </div>
                        <div className={"postInfo"}>
                            <p className="restName"> <em> {resName.toLowerCase()} </em> </p>
                            <p className="postTime"> posted @ {props.time} </p>
                        </div>
                    </div>
                    <div className="postDescContainer">
                        <p className="postDesc">{props.desc}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Post;

